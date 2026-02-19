package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.*;
import com.compras.exception.BusinessException;
import com.compras.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiquidacaoService {

    private final LiquidacaoRepository liquidacaoRepository;
    private final EmpenhoRepository empenhoRepository;

    @Transactional(readOnly = true)
    public Page<LiquidacaoListDTO> findAll(String numeroLiquidacao, Long empenhoId,
                                            String status, Pageable pageable) {
        Page<Liquidacao> page = liquidacaoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (numeroLiquidacao != null && !numeroLiquidacao.isBlank()) {
                predicates.add(cb.like(root.get("numeroLiquidacao"), "%" + numeroLiquidacao + "%"));
            }
            if (empenhoId != null) {
                predicates.add(cb.equal(root.get("empenho").get("id"), empenhoId));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        if (page.isEmpty()) return page.map(e -> null);

        List<Long> ids = page.getContent().stream().map(Liquidacao::getId).toList();
        List<Liquidacao> liquidacoes = liquidacaoRepository.findByIdsWithAssociations(ids);
        Map<Long, Liquidacao> map = liquidacoes.stream()
                .collect(Collectors.toMap(Liquidacao::getId, Function.identity()));

        return page.map(l -> toListDTO(map.get(l.getId())));
    }

    @Transactional(readOnly = true)
    public LiquidacaoResponseDTO findById(Long id) {
        Liquidacao l = liquidacaoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("MC_LIQUIDACAO_NOT_FOUND",
                        "Liquidação não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(l);
    }

    @Transactional
    public LiquidacaoResponseDTO create(LiquidacaoRequestDTO dto) {
        Liquidacao l = new Liquidacao();
        updateEntity(l, dto);
        l = liquidacaoRepository.save(l);
        return toResponseDTO(liquidacaoRepository.findByIdWithAssociations(l.getId()).orElse(l));
    }

    @Transactional
    public LiquidacaoResponseDTO update(Long id, LiquidacaoRequestDTO dto) {
        Liquidacao l = liquidacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_LIQUIDACAO_NOT_FOUND",
                        "Liquidação não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(l, dto);
        l = liquidacaoRepository.save(l);
        return toResponseDTO(liquidacaoRepository.findByIdWithAssociations(l.getId()).orElse(l));
    }

    @Transactional
    public void delete(Long id) {
        if (!liquidacaoRepository.existsById(id)) {
            throw new BusinessException("MC_LIQUIDACAO_NOT_FOUND",
                    "Liquidação não encontrada", HttpStatus.NOT_FOUND);
        }
        liquidacaoRepository.deleteById(id);
    }

    private void updateEntity(Liquidacao l, LiquidacaoRequestDTO dto) {
        l.setEmpenho(empenhoRepository.findById(dto.getEmpenhoId())
                .orElseThrow(() -> new BusinessException("MC_EMPENHO_NOT_FOUND", "Empenho não encontrado")));
        l.setNumeroLiquidacao(dto.getNumeroLiquidacao());
        l.setDataLiquidacao(dto.getDataLiquidacao());
        l.setValor(dto.getValor());
        l.setDocumentoTipo(dto.getDocumentoTipo());
        l.setDocumentoNumero(dto.getDocumentoNumero());
        l.setDocumentoData(dto.getDocumentoData());
        l.setDescricao(dto.getDescricao());
        l.setStatus(dto.getStatus() != null ? dto.getStatus() : "ATIVA");
    }

    private LiquidacaoListDTO toListDTO(Liquidacao l) {
        return LiquidacaoListDTO.builder()
                .id(l.getId())
                .numeroLiquidacao(l.getNumeroLiquidacao())
                .numeroEmpenho(l.getEmpenho() != null ? l.getEmpenho().getNumeroEmpenho() : null)
                .fornecedorNome(l.getEmpenho() != null && l.getEmpenho().getFornecedor() != null
                        ? l.getEmpenho().getFornecedor().getRazaoSocial() : null)
                .dataLiquidacao(l.getDataLiquidacao())
                .valor(l.getValor())
                .documentoTipo(l.getDocumentoTipo())
                .documentoNumero(l.getDocumentoNumero())
                .status(l.getStatus())
                .build();
    }

    private LiquidacaoResponseDTO toResponseDTO(Liquidacao l) {
        return LiquidacaoResponseDTO.builder()
                .id(l.getId())
                .empenho(l.getEmpenho() != null ? EmpenhoListDTO.builder()
                        .id(l.getEmpenho().getId())
                        .numeroEmpenho(l.getEmpenho().getNumeroEmpenho())
                        .exercicio(l.getEmpenho().getExercicio())
                        .fornecedorNome(l.getEmpenho().getFornecedor() != null
                                ? l.getEmpenho().getFornecedor().getRazaoSocial() : null)
                        .valor(l.getEmpenho().getValor())
                        .status(l.getEmpenho().getStatus())
                        .build() : null)
                .numeroLiquidacao(l.getNumeroLiquidacao())
                .dataLiquidacao(l.getDataLiquidacao())
                .valor(l.getValor())
                .documentoTipo(l.getDocumentoTipo())
                .documentoNumero(l.getDocumentoNumero())
                .documentoData(l.getDocumentoData())
                .descricao(l.getDescricao())
                .status(l.getStatus())
                .createdAt(l.getCreatedAt())
                .updatedAt(l.getUpdatedAt())
                .build();
    }
}
