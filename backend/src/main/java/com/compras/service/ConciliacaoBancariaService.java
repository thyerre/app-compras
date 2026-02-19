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
public class ConciliacaoBancariaService {

    private final ConciliacaoBancariaRepository conciliacaoBancariaRepository;
    private final ContaBancariaRepository contaBancariaRepository;

    @Transactional(readOnly = true)
    public Page<ConciliacaoBancariaListDTO> findAll(Integer anoReferencia, String status,
                                                      Pageable pageable) {
        Page<ConciliacaoBancaria> page = conciliacaoBancariaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (anoReferencia != null) {
                predicates.add(cb.equal(root.get("anoReferencia"), anoReferencia));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<ConciliacaoBancariaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ConciliacaoBancariaResponseDTO findById(Long id) {
        ConciliacaoBancaria conciliacao = conciliacaoBancariaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_CONCILIACAO_NOT_FOUND",
                        "Conciliação bancária não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(conciliacao);
    }

    @Transactional
    public ConciliacaoBancariaResponseDTO create(ConciliacaoBancariaRequestDTO dto) {
        ConciliacaoBancaria conciliacao = new ConciliacaoBancaria();
        updateEntity(conciliacao, dto);
        return toResponseDTO(conciliacaoBancariaRepository.save(conciliacao));
    }

    @Transactional
    public ConciliacaoBancariaResponseDTO update(Long id, ConciliacaoBancariaRequestDTO dto) {
        ConciliacaoBancaria conciliacao = conciliacaoBancariaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_CONCILIACAO_NOT_FOUND",
                        "Conciliação bancária não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(conciliacao, dto);
        return toResponseDTO(conciliacaoBancariaRepository.save(conciliacao));
    }

    @Transactional
    public void delete(Long id) {
        if (!conciliacaoBancariaRepository.existsById(id)) {
            throw new BusinessException("MC_CONCILIACAO_NOT_FOUND",
                    "Conciliação bancária não encontrada", HttpStatus.NOT_FOUND);
        }
        conciliacaoBancariaRepository.deleteById(id);
    }

    private void updateEntity(ConciliacaoBancaria c, ConciliacaoBancariaRequestDTO dto) {
        c.setContaBancaria(contaBancariaRepository.findById(dto.getContaBancariaId())
                .orElseThrow(() -> new BusinessException("MC_CONCILIACAO_CONTA_NOT_FOUND",
                        "Conta bancária não encontrada")));
        c.setMesReferencia(dto.getMesReferencia());
        c.setAnoReferencia(dto.getAnoReferencia());
        c.setSaldoExtrato(dto.getSaldoExtrato());
        c.setSaldoContabil(dto.getSaldoContabil());
        c.setDiferenca(dto.getSaldoExtrato().subtract(dto.getSaldoContabil()));
        c.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDENTE");
        c.setObservacoes(dto.getObservacoes());
        c.setResponsavel(dto.getResponsavel());
        c.setDataConciliacao(dto.getDataConciliacao());
    }

    private ConciliacaoBancariaListDTO toListDTO(ConciliacaoBancaria c) {
        return ConciliacaoBancariaListDTO.builder()
                .id(c.getId())
                .contaBancariaDescricao(c.getContaBancaria() != null ? c.getContaBancaria().getDescricao() : null)
                .mesReferencia(c.getMesReferencia())
                .anoReferencia(c.getAnoReferencia())
                .saldoExtrato(c.getSaldoExtrato())
                .saldoContabil(c.getSaldoContabil())
                .diferenca(c.getDiferenca())
                .status(c.getStatus())
                .build();
    }

    private ConciliacaoBancariaResponseDTO toResponseDTO(ConciliacaoBancaria c) {
        return ConciliacaoBancariaResponseDTO.builder()
                .id(c.getId())
                .contaBancaria(c.getContaBancaria() != null ? ContaBancariaListDTO.builder()
                        .id(c.getContaBancaria().getId())
                        .bancoCodigo(c.getContaBancaria().getBancoCodigo())
                        .bancoNome(c.getContaBancaria().getBancoNome())
                        .agencia(c.getContaBancaria().getAgencia())
                        .numeroConta(c.getContaBancaria().getNumeroConta())
                        .descricao(c.getContaBancaria().getDescricao())
                        .tipo(c.getContaBancaria().getTipo())
                        .saldoAtual(c.getContaBancaria().getSaldoAtual())
                        .ativo(c.getContaBancaria().getAtivo())
                        .build() : null)
                .mesReferencia(c.getMesReferencia())
                .anoReferencia(c.getAnoReferencia())
                .saldoExtrato(c.getSaldoExtrato())
                .saldoContabil(c.getSaldoContabil())
                .diferenca(c.getDiferenca())
                .status(c.getStatus())
                .observacoes(c.getObservacoes())
                .responsavel(c.getResponsavel())
                .dataConciliacao(c.getDataConciliacao())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
