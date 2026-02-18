package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.*;
import com.compras.exception.BusinessException;
import com.compras.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
public class EmpenhoService {

    private final EmpenhoRepository empenhoRepository;
    private final ProcessoCompraRepository processoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final DotacaoOrcamentariaRepository dotacaoRepository;

    @Transactional(readOnly = true)
    public Page<EmpenhoListDTO> findAll(String numeroEmpenho, Integer exercicio,
                                         Long processoId, Long fornecedorId,
                                         String status, Pageable pageable) {
        Specification<Empenho> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (numeroEmpenho != null && !numeroEmpenho.isBlank()) {
                predicates.add(cb.like(root.get("numeroEmpenho"), "%" + numeroEmpenho + "%"));
            }
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (processoId != null) {
                predicates.add(cb.equal(root.get("processo").get("id"), processoId));
            }
            if (fornecedorId != null) {
                predicates.add(cb.equal(root.get("fornecedor").get("id"), fornecedorId));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Empenho> page = empenhoRepository.findAll(spec, pageable);
        if (page.isEmpty()) return page.map(e -> null);

        List<Long> ids = page.getContent().stream().map(Empenho::getId).toList();
        List<Empenho> empenhos = empenhoRepository.findByIdsWithAssociations(ids);
        Map<Long, Empenho> empenhoMap = empenhos.stream()
                .collect(Collectors.toMap(Empenho::getId, Function.identity()));

        return page.map(e -> toListDTO(empenhoMap.get(e.getId())));
    }

    @Transactional(readOnly = true)
    public EmpenhoResponseDTO findById(Long id) {
        Empenho empenho = empenhoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("ME_EMPENHO_NOT_FOUND",
                        "Empenho não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(empenho);
    }

    @Transactional
    public EmpenhoResponseDTO create(EmpenhoRequestDTO dto) {
        validateNumeroEmpenhoUnique(dto.getNumeroEmpenho(), null);
        Empenho empenho = new Empenho();
        updateEntity(empenho, dto);
        empenho = empenhoRepository.save(empenho);
        return toResponseDTO(empenhoRepository.findByIdWithAssociations(empenho.getId()).orElse(empenho));
    }

    @Transactional
    public EmpenhoResponseDTO update(Long id, EmpenhoRequestDTO dto) {
        Empenho empenho = empenhoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_EMPENHO_NOT_FOUND",
                        "Empenho não encontrado", HttpStatus.NOT_FOUND));
        validateNumeroEmpenhoUnique(dto.getNumeroEmpenho(), id);
        updateEntity(empenho, dto);
        empenho = empenhoRepository.save(empenho);
        return toResponseDTO(empenhoRepository.findByIdWithAssociations(empenho.getId()).orElse(empenho));
    }

    @Transactional
    public void delete(Long id) {
        if (!empenhoRepository.existsById(id)) {
            throw new BusinessException("ME_EMPENHO_NOT_FOUND",
                    "Empenho não encontrado", HttpStatus.NOT_FOUND);
        }
        empenhoRepository.deleteById(id);
    }

    private void validateNumeroEmpenhoUnique(String numeroEmpenho, Long excludeId) {
        boolean exists = excludeId == null
                ? empenhoRepository.existsByNumeroEmpenho(numeroEmpenho)
                : empenhoRepository.existsByNumeroEmpenhoAndIdNot(numeroEmpenho, excludeId);
        if (exists) {
            throw new BusinessException("ME_EMPENHO_NUMERO_DUPLICATE",
                    "Já existe um empenho com este número");
        }
    }

    private void updateEntity(Empenho e, EmpenhoRequestDTO dto) {
        e.setNumeroEmpenho(dto.getNumeroEmpenho());
        e.setExercicio(dto.getExercicio());
        e.setProcesso(processoRepository.findById(dto.getProcessoId())
                .orElseThrow(() -> new BusinessException("ME_PROCESSO_NOT_FOUND", "Processo não encontrado")));
        e.setFornecedor(fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new BusinessException("ME_FORNECEDOR_NOT_FOUND", "Fornecedor não encontrado")));
        e.setDotacao(dotacaoRepository.findById(dto.getDotacaoId())
                .orElseThrow(() -> new BusinessException("ME_DOTACAO_NOT_FOUND",
                        "Dotação orçamentária não encontrada")));
        e.setValor(dto.getValor());
        if (dto.getValorLiquidado() != null) e.setValorLiquidado(dto.getValorLiquidado());
        if (dto.getValorPago() != null) e.setValorPago(dto.getValorPago());
        e.setDataEmpenho(dto.getDataEmpenho());
        e.setDataLiquidacao(dto.getDataLiquidacao());
        e.setDataPagamento(dto.getDataPagamento());
        e.setTipoEmpenho(dto.getTipoEmpenho() != null ? dto.getTipoEmpenho() : "ORDINARIO");
        e.setStatus(dto.getStatus() != null ? dto.getStatus() : "ATIVO");
        e.setDescricao(dto.getDescricao());
        e.setObservacao(dto.getObservacao());
    }

    private EmpenhoListDTO toListDTO(Empenho e) {
        return EmpenhoListDTO.builder()
                .id(e.getId())
                .numeroEmpenho(e.getNumeroEmpenho())
                .exercicio(e.getExercicio())
                .numeroProcesso(e.getProcesso() != null ? e.getProcesso().getNumeroProcesso() : null)
                .fornecedorNome(e.getFornecedor() != null ? e.getFornecedor().getRazaoSocial() : null)
                .fornecedorCnpjCpf(e.getFornecedor() != null ? e.getFornecedor().getCnpjCpf() : null)
                .valor(e.getValor())
                .valorLiquidado(e.getValorLiquidado())
                .valorPago(e.getValorPago())
                .dataEmpenho(e.getDataEmpenho())
                .tipoEmpenho(e.getTipoEmpenho())
                .status(e.getStatus())
                .build();
    }

    private EmpenhoResponseDTO toResponseDTO(Empenho e) {
        return EmpenhoResponseDTO.builder()
                .id(e.getId())
                .numeroEmpenho(e.getNumeroEmpenho())
                .exercicio(e.getExercicio())
                .processo(e.getProcesso() != null ? ProcessoCompraListDTO.builder()
                        .id(e.getProcesso().getId())
                        .numeroProcesso(e.getProcesso().getNumeroProcesso())
                        .exercicio(e.getProcesso().getAno())
                        .modalidade(e.getProcesso().getModalidade() != null ?
                                e.getProcesso().getModalidade().getNome() : null)
                        .build() : null)
                .fornecedor(e.getFornecedor() != null ? FornecedorListDTO.builder()
                        .id(e.getFornecedor().getId())
                        .razaoSocial(e.getFornecedor().getRazaoSocial())
                        .cnpjCpf(e.getFornecedor().getCnpjCpf())
                        .build() : null)
                .dotacao(e.getDotacao() != null ? DotacaoOrcamentariaListDTO.builder()
                        .id(e.getDotacao().getId())
                        .valorInicial(e.getDotacao().getValorInicial())
                        .saldoDisponivel(e.getDotacao().getSaldoDisponivel())
                        .build() : null)
                .valor(e.getValor())
                .valorLiquidado(e.getValorLiquidado())
                .valorPago(e.getValorPago())
                .dataEmpenho(e.getDataEmpenho())
                .dataLiquidacao(e.getDataLiquidacao())
                .dataPagamento(e.getDataPagamento())
                .tipoEmpenho(e.getTipoEmpenho())
                .status(e.getStatus())
                .descricao(e.getDescricao())
                .observacao(e.getObservacao())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
