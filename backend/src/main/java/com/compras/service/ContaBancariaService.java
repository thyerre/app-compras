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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    private final ContaBancariaRepository contaBancariaRepository;
    private final FonteRecursoRepository fonteRecursoRepository;

    @Transactional(readOnly = true)
    public Page<ContaBancariaListDTO> findAll(String descricao, String tipo,
                                               Boolean ativo, Pageable pageable) {
        Page<ContaBancaria> page = contaBancariaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (descricao != null && !descricao.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<ContaBancariaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ContaBancariaResponseDTO findById(Long id) {
        ContaBancaria cb = contaBancariaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_CONTA_NOT_FOUND",
                        "Conta bancária não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(cb);
    }

    @Transactional
    public ContaBancariaResponseDTO create(ContaBancariaRequestDTO dto) {
        ContaBancaria cb = new ContaBancaria();
        updateEntity(cb, dto);
        return toResponseDTO(contaBancariaRepository.save(cb));
    }

    @Transactional
    public ContaBancariaResponseDTO update(Long id, ContaBancariaRequestDTO dto) {
        ContaBancaria cb = contaBancariaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_CONTA_NOT_FOUND",
                        "Conta bancária não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(cb, dto);
        return toResponseDTO(contaBancariaRepository.save(cb));
    }

    @Transactional
    public void delete(Long id) {
        if (!contaBancariaRepository.existsById(id)) {
            throw new BusinessException("MC_CONTA_NOT_FOUND",
                    "Conta bancária não encontrada", HttpStatus.NOT_FOUND);
        }
        contaBancariaRepository.deleteById(id);
    }

    private void updateEntity(ContaBancaria cb, ContaBancariaRequestDTO dto) {
        cb.setBancoCodigo(dto.getBancoCodigo());
        cb.setBancoNome(dto.getBancoNome());
        cb.setAgencia(dto.getAgencia());
        cb.setNumeroConta(dto.getNumeroConta());
        cb.setDigito(dto.getDigito());
        cb.setDescricao(dto.getDescricao());
        cb.setTipo(dto.getTipo() != null ? dto.getTipo() : "MOVIMENTO");
        if (dto.getFonteRecursoId() != null) {
            cb.setFonteRecurso(fonteRecursoRepository.findById(dto.getFonteRecursoId())
                    .orElseThrow(() -> new BusinessException("MC_FONTE_NOT_FOUND", "Fonte de recurso não encontrada")));
        }
        if (dto.getSaldoAtual() != null) cb.setSaldoAtual(dto.getSaldoAtual());
        cb.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }

    private ContaBancariaListDTO toListDTO(ContaBancaria cb) {
        return ContaBancariaListDTO.builder()
                .id(cb.getId()).bancoCodigo(cb.getBancoCodigo()).bancoNome(cb.getBancoNome())
                .agencia(cb.getAgencia()).numeroConta(cb.getNumeroConta())
                .descricao(cb.getDescricao()).tipo(cb.getTipo())
                .saldoAtual(cb.getSaldoAtual()).ativo(cb.getAtivo())
                .build();
    }

    private ContaBancariaResponseDTO toResponseDTO(ContaBancaria cb) {
        return ContaBancariaResponseDTO.builder()
                .id(cb.getId()).bancoCodigo(cb.getBancoCodigo()).bancoNome(cb.getBancoNome())
                .agencia(cb.getAgencia()).numeroConta(cb.getNumeroConta())
                .digito(cb.getDigito()).descricao(cb.getDescricao()).tipo(cb.getTipo())
                .fonteRecurso(cb.getFonteRecurso() != null ? FonteRecursoDTO.builder()
                        .id(cb.getFonteRecurso().getId())
                        .codigo(cb.getFonteRecurso().getCodigo())
                        .descricao(cb.getFonteRecurso().getDescricao())
                        .build() : null)
                .saldoAtual(cb.getSaldoAtual()).ativo(cb.getAtivo())
                .createdAt(cb.getCreatedAt()).updatedAt(cb.getUpdatedAt())
                .build();
    }
}
