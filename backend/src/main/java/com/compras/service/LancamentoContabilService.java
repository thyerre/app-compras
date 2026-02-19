package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.*;
import com.compras.exception.BusinessException;
import com.compras.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LancamentoContabilService {

    private final LancamentoContabilRepository lancamentoRepository;
    private final PlanoContasRepository planoContasRepository;
    private final EmpenhoRepository empenhoRepository;
    private final LiquidacaoRepository liquidacaoRepository;
    private final PagamentoRepository pagamentoRepository;

    @Transactional(readOnly = true)
    public Page<LancamentoContabilListDTO> findAll(Integer exercicio, String tipo,
                                                     String origem, Pageable pageable) {
        Page<LancamentoContabil> page = lancamentoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (origem != null && !origem.isBlank()) {
                predicates.add(cb.equal(root.get("origem"), origem));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<LancamentoContabilListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public LancamentoContabilResponseDTO findById(Long id) {
        LancamentoContabil lc = lancamentoRepository.findByIdWithItens(id)
                .orElseThrow(() -> new BusinessException("MC_LANCAMENTO_NOT_FOUND",
                        "Lançamento contábil não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(lc);
    }

    @Transactional
    public LancamentoContabilResponseDTO create(LancamentoContabilRequestDTO dto) {
        validatePartidasDobradas(dto);

        LancamentoContabil lc = new LancamentoContabil();
        updateEntity(lc, dto);

        // Adicionar itens
        if (dto.getItens() != null) {
            for (LancamentoItemDTO itemDto : dto.getItens()) {
                LancamentoItem item = new LancamentoItem();
                item.setLancamento(lc);
                item.setPlanoConta(planoContasRepository.findById(itemDto.getPlanoContaId())
                        .orElseThrow(() -> new BusinessException("MC_CONTA_NOT_FOUND", "Conta não encontrada")));
                item.setTipo(itemDto.getTipo());
                item.setValor(itemDto.getValor());
                item.setHistoricoItem(itemDto.getHistoricoItem());
                lc.getItens().add(item);
            }
        }

        lc = lancamentoRepository.save(lc);
        return toResponseDTO(lancamentoRepository.findByIdWithItens(lc.getId()).orElse(lc));
    }

    @Transactional
    public LancamentoContabilResponseDTO update(Long id, LancamentoContabilRequestDTO dto) {
        validatePartidasDobradas(dto);

        LancamentoContabil lc = lancamentoRepository.findByIdWithItens(id)
                .orElseThrow(() -> new BusinessException("MC_LANCAMENTO_NOT_FOUND",
                        "Lançamento contábil não encontrado", HttpStatus.NOT_FOUND));

        updateEntity(lc, dto);
        lc.getItens().clear();

        if (dto.getItens() != null) {
            for (LancamentoItemDTO itemDto : dto.getItens()) {
                LancamentoItem item = new LancamentoItem();
                item.setLancamento(lc);
                item.setPlanoConta(planoContasRepository.findById(itemDto.getPlanoContaId())
                        .orElseThrow(() -> new BusinessException("MC_CONTA_NOT_FOUND", "Conta não encontrada")));
                item.setTipo(itemDto.getTipo());
                item.setValor(itemDto.getValor());
                item.setHistoricoItem(itemDto.getHistoricoItem());
                lc.getItens().add(item);
            }
        }

        lc = lancamentoRepository.save(lc);
        return toResponseDTO(lancamentoRepository.findByIdWithItens(lc.getId()).orElse(lc));
    }

    @Transactional
    public void delete(Long id) {
        if (!lancamentoRepository.existsById(id)) {
            throw new BusinessException("MC_LANCAMENTO_NOT_FOUND",
                    "Lançamento contábil não encontrado", HttpStatus.NOT_FOUND);
        }
        lancamentoRepository.deleteById(id);
    }

    /**
     * Valida o princípio das partidas dobradas:
     * soma dos débitos = soma dos créditos = valor total
     */
    private void validatePartidasDobradas(LancamentoContabilRequestDTO dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new BusinessException("MC_LANCAMENTO_SEM_ITENS",
                    "O lançamento deve ter ao menos um débito e um crédito");
        }

        BigDecimal totalDebitos = dto.getItens().stream()
                .filter(i -> "D".equals(i.getTipo()))
                .map(LancamentoItemDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCreditos = dto.getItens().stream()
                .filter(i -> "C".equals(i.getTipo()))
                .map(LancamentoItemDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebitos.compareTo(BigDecimal.ZERO) == 0 || totalCreditos.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("MC_LANCAMENTO_PARTIDAS_INVALIDAS",
                    "O lançamento deve ter ao menos um débito e um crédito");
        }

        if (totalDebitos.compareTo(totalCreditos) != 0) {
            throw new BusinessException("MC_LANCAMENTO_DESBALANCEADO",
                    "Total de débitos (R$ " + totalDebitos + ") difere do total de créditos (R$ " + totalCreditos + "). Partidas dobradas exigem igualdade.");
        }
    }

    private void updateEntity(LancamentoContabil lc, LancamentoContabilRequestDTO dto) {
        lc.setExercicio(dto.getExercicio());
        lc.setNumero(dto.getNumero());
        lc.setDataLancamento(dto.getDataLancamento());
        lc.setTipo(dto.getTipo() != null ? dto.getTipo() : "NORMAL");
        lc.setOrigem(dto.getOrigem() != null ? dto.getOrigem() : "MANUAL");
        lc.setHistorico(dto.getHistorico());
        lc.setValorTotal(dto.getValorTotal());
        lc.setDocumentoRef(dto.getDocumentoRef());
        if (dto.getEmpenhoId() != null) {
            lc.setEmpenho(empenhoRepository.findById(dto.getEmpenhoId()).orElse(null));
        }
        if (dto.getLiquidacaoId() != null) {
            lc.setLiquidacao(liquidacaoRepository.findById(dto.getLiquidacaoId()).orElse(null));
        }
        if (dto.getPagamentoId() != null) {
            lc.setPagamento(pagamentoRepository.findById(dto.getPagamentoId()).orElse(null));
        }
        lc.setStatus(dto.getStatus() != null ? dto.getStatus() : "EFETIVADO");
    }

    private LancamentoContabilListDTO toListDTO(LancamentoContabil lc) {
        return LancamentoContabilListDTO.builder()
                .id(lc.getId()).numero(lc.getNumero())
                .dataLancamento(lc.getDataLancamento())
                .tipo(lc.getTipo()).origem(lc.getOrigem())
                .historico(lc.getHistorico())
                .valorTotal(lc.getValorTotal())
                .status(lc.getStatus())
                .build();
    }

    private LancamentoContabilResponseDTO toResponseDTO(LancamentoContabil lc) {
        return LancamentoContabilResponseDTO.builder()
                .id(lc.getId()).exercicio(lc.getExercicio()).numero(lc.getNumero())
                .dataLancamento(lc.getDataLancamento())
                .tipo(lc.getTipo()).origem(lc.getOrigem())
                .historico(lc.getHistorico())
                .valorTotal(lc.getValorTotal())
                .documentoRef(lc.getDocumentoRef())
                .status(lc.getStatus())
                .itens(lc.getItens() != null ? lc.getItens().stream().map(i ->
                        LancamentoItemDTO.builder()
                                .id(i.getId())
                                .planoContaId(i.getPlanoConta().getId())
                                .planoContaCodigo(i.getPlanoConta().getCodigo())
                                .planoContaDescricao(i.getPlanoConta().getDescricao())
                                .tipo(i.getTipo())
                                .valor(i.getValor())
                                .historicoItem(i.getHistoricoItem())
                                .build()
                ).collect(Collectors.toList()) : List.of())
                .createdAt(lc.getCreatedAt()).updatedAt(lc.getUpdatedAt())
                .build();
    }
}
