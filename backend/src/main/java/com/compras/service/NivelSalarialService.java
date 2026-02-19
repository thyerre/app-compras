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

@Service
@RequiredArgsConstructor
public class NivelSalarialService {

    private final NivelSalarialRepository nivelSalarialRepository;
    private final CargoRepository cargoRepository;

    @Transactional(readOnly = true)
    public Page<NivelSalarialListDTO> findAll(Integer cargoId, String nivel, Boolean ativo, Pageable pageable) {
        Specification<NivelSalarial> spec = buildFilterSpec(cargoId, nivel, ativo);
        Page<NivelSalarial> page = nivelSalarialRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<NivelSalarial> buildFilterSpec(Integer cargoId, String nivel, Boolean ativo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (cargoId != null) {
                predicates.add(cb.equal(root.get("cargo").get("id"), cargoId));
            }
            if (nivel != null && !nivel.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nivel")), "%" + nivel.toLowerCase() + "%"));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public NivelSalarialResponseDTO findById(Integer id) {
        NivelSalarial e = nivelSalarialRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_NIVEL_SALARIAL_NOT_FOUND", "Nível salarial não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public NivelSalarialResponseDTO create(NivelSalarialRequestDTO dto) {
        NivelSalarial e = toEntity(dto);
        e = nivelSalarialRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public NivelSalarialResponseDTO update(Integer id, NivelSalarialRequestDTO dto) {
        NivelSalarial e = nivelSalarialRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_NIVEL_SALARIAL_NOT_FOUND", "Nível salarial não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(e, dto);
        e = nivelSalarialRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Integer id) {
        if (!nivelSalarialRepository.existsById(id)) {
            throw new BusinessException("ME_NIVEL_SALARIAL_NOT_FOUND", "Nível salarial não encontrado", HttpStatus.NOT_FOUND);
        }
        nivelSalarialRepository.deleteById(id);
    }

    private NivelSalarialListDTO toListDTO(NivelSalarial e) {
        return NivelSalarialListDTO.builder()
                .id(e.getId())
                .cargoDescricao(e.getCargo() != null ? e.getCargo().getDescricao() : null)
                .nivel(e.getNivel())
                .classe(e.getClasse())
                .referencia(e.getReferencia())
                .valorBase(e.getValorBase())
                .vigenciaInicio(e.getVigenciaInicio())
                .vigenciaFim(e.getVigenciaFim())
                .ativo(e.getAtivo())
                .build();
    }

    private NivelSalarialResponseDTO toResponseDTO(NivelSalarial e) {
        return NivelSalarialResponseDTO.builder()
                .id(e.getId())
                .cargoId(e.getCargo() != null ? e.getCargo().getId() : null)
                .cargoDescricao(e.getCargo() != null ? e.getCargo().getDescricao() : null)
                .nivel(e.getNivel())
                .classe(e.getClasse())
                .referencia(e.getReferencia())
                .valorBase(e.getValorBase())
                .vigenciaInicio(e.getVigenciaInicio())
                .vigenciaFim(e.getVigenciaFim())
                .ativo(e.getAtivo())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private NivelSalarial toEntity(NivelSalarialRequestDTO dto) {
        NivelSalarial e = new NivelSalarial();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(NivelSalarial e, NivelSalarialRequestDTO dto) {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new BusinessException("ME_CARGO_NOT_FOUND", "Cargo não encontrado", HttpStatus.NOT_FOUND));
        e.setCargo(cargo);
        e.setNivel(dto.getNivel());
        e.setClasse(dto.getClasse());
        e.setReferencia(dto.getReferencia());
        e.setValorBase(dto.getValorBase());
        e.setVigenciaInicio(dto.getVigenciaInicio());
        e.setVigenciaFim(dto.getVigenciaFim());
        e.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }
}
