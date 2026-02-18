package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.Programa;
import com.compras.exception.BusinessException;
import com.compras.repository.ProgramaRepository;
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
public class ProgramaService {

    private final ProgramaRepository programaRepository;

    @Transactional(readOnly = true)
    public Page<ProgramaListDTO> findAll(String nome, Boolean ativo, Pageable pageable) {
        Page<Programa> page = programaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<ProgramaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProgramaResponseDTO findById(Integer id) {
        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_PROGRAMA_NOT_FOUND",
                        "Programa não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(programa);
    }

    @Transactional
    public ProgramaResponseDTO create(ProgramaRequestDTO dto) {
        validateCodigoUnique(dto.getCodigo(), null);
        Programa programa = toEntity(dto);
        return toResponseDTO(programaRepository.save(programa));
    }

    @Transactional
    public ProgramaResponseDTO update(Integer id, ProgramaRequestDTO dto) {
        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_PROGRAMA_NOT_FOUND",
                        "Programa não encontrado", HttpStatus.NOT_FOUND));
        validateCodigoUnique(dto.getCodigo(), id);
        updateEntity(programa, dto);
        return toResponseDTO(programaRepository.save(programa));
    }

    @Transactional
    public void delete(Integer id) {
        if (!programaRepository.existsById(id)) {
            throw new BusinessException("ME_PROGRAMA_NOT_FOUND",
                    "Programa não encontrado", HttpStatus.NOT_FOUND);
        }
        programaRepository.deleteById(id);
    }

    private void validateCodigoUnique(String codigo, Integer excludeId) {
        boolean exists = excludeId == null
                ? programaRepository.existsByCodigo(codigo)
                : programaRepository.existsByCodigoAndIdNot(codigo, excludeId);
        if (exists) {
            throw new BusinessException("ME_PROGRAMA_CODIGO_DUPLICATE",
                    "Já existe um programa com este código");
        }
    }

    private Programa toEntity(ProgramaRequestDTO dto) {
        Programa p = new Programa();
        updateEntity(p, dto);
        return p;
    }

    private void updateEntity(Programa p, ProgramaRequestDTO dto) {
        p.setCodigo(dto.getCodigo());
        p.setNome(dto.getNome());
        p.setObjetivo(dto.getObjetivo());
        p.setPublicoAlvo(dto.getPublicoAlvo());
        p.setExercicioInicio(dto.getExercicioInicio());
        p.setExercicioFim(dto.getExercicioFim());
        p.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }

    private ProgramaListDTO toListDTO(Programa p) {
        return ProgramaListDTO.builder()
                .id(p.getId()).codigo(p.getCodigo()).nome(p.getNome())
                .exercicioInicio(p.getExercicioInicio())
                .exercicioFim(p.getExercicioFim())
                .ativo(p.getAtivo())
                .build();
    }

    private ProgramaResponseDTO toResponseDTO(Programa p) {
        return ProgramaResponseDTO.builder()
                .id(p.getId()).codigo(p.getCodigo()).nome(p.getNome())
                .objetivo(p.getObjetivo()).publicoAlvo(p.getPublicoAlvo())
                .exercicioInicio(p.getExercicioInicio())
                .exercicioFim(p.getExercicioFim())
                .ativo(p.getAtivo())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
