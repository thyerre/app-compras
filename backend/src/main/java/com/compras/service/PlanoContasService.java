package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.PlanoContas;
import com.compras.exception.BusinessException;
import com.compras.repository.PlanoContasRepository;
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
public class PlanoContasService {

    private final PlanoContasRepository planoContasRepository;

    @Transactional(readOnly = true)
    public Page<PlanoContasListDTO> findAll(String codigo, String descricao,
                                             Short classe, String tipo, Pageable pageable) {
        Page<PlanoContas> page = planoContasRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (codigo != null && !codigo.isBlank()) {
                predicates.add(cb.like(root.get("codigo"), codigo + "%"));
            }
            if (descricao != null && !descricao.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
            }
            if (classe != null) {
                predicates.add(cb.equal(root.get("classe"), classe));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<PlanoContasListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PlanoContasResponseDTO findById(Long id) {
        PlanoContas pc = planoContasRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_PLANO_CONTAS_NOT_FOUND",
                        "Conta contábil não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(pc);
    }

    @Transactional
    public PlanoContasResponseDTO create(PlanoContasRequestDTO dto) {
        if (planoContasRepository.existsByCodigo(dto.getCodigo())) {
            throw new BusinessException("MC_PLANO_CONTAS_DUPLICATE",
                    "Já existe uma conta com este código");
        }
        PlanoContas pc = new PlanoContas();
        updateEntity(pc, dto);
        return toResponseDTO(planoContasRepository.save(pc));
    }

    @Transactional
    public PlanoContasResponseDTO update(Long id, PlanoContasRequestDTO dto) {
        PlanoContas pc = planoContasRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_PLANO_CONTAS_NOT_FOUND",
                        "Conta contábil não encontrada", HttpStatus.NOT_FOUND));
        if (planoContasRepository.existsByCodigoAndIdNot(dto.getCodigo(), id)) {
            throw new BusinessException("MC_PLANO_CONTAS_DUPLICATE",
                    "Já existe uma conta com este código");
        }
        updateEntity(pc, dto);
        return toResponseDTO(planoContasRepository.save(pc));
    }

    @Transactional
    public void delete(Long id) {
        if (!planoContasRepository.existsById(id)) {
            throw new BusinessException("MC_PLANO_CONTAS_NOT_FOUND",
                    "Conta contábil não encontrada", HttpStatus.NOT_FOUND);
        }
        planoContasRepository.deleteById(id);
    }

    private void updateEntity(PlanoContas pc, PlanoContasRequestDTO dto) {
        pc.setCodigo(dto.getCodigo());
        pc.setDescricao(dto.getDescricao());
        pc.setClasse(dto.getClasse());
        pc.setNaturezaSaldo(dto.getNaturezaSaldo());
        pc.setTipo(dto.getTipo());
        pc.setNivel(dto.getNivel());
        if (dto.getParentId() != null) {
            pc.setParent(planoContasRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new BusinessException("MC_PLANO_CONTAS_PARENT_NOT_FOUND",
                            "Conta pai não encontrada")));
        } else {
            pc.setParent(null);
        }
        pc.setEscrituravel(dto.getEscrituravel() != null ? dto.getEscrituravel() : false);
        pc.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }

    private PlanoContasListDTO toListDTO(PlanoContas pc) {
        return PlanoContasListDTO.builder()
                .id(pc.getId()).codigo(pc.getCodigo()).descricao(pc.getDescricao())
                .classe(pc.getClasse()).naturezaSaldo(pc.getNaturezaSaldo())
                .tipo(pc.getTipo()).nivel(pc.getNivel())
                .escrituravel(pc.getEscrituravel()).ativo(pc.getAtivo())
                .build();
    }

    private PlanoContasResponseDTO toResponseDTO(PlanoContas pc) {
        return PlanoContasResponseDTO.builder()
                .id(pc.getId()).codigo(pc.getCodigo()).descricao(pc.getDescricao())
                .classe(pc.getClasse()).naturezaSaldo(pc.getNaturezaSaldo())
                .tipo(pc.getTipo()).nivel(pc.getNivel())
                .parent(pc.getParent() != null ? toListDTO(pc.getParent()) : null)
                .escrituravel(pc.getEscrituravel()).ativo(pc.getAtivo())
                .createdAt(pc.getCreatedAt()).updatedAt(pc.getUpdatedAt())
                .build();
    }
}
