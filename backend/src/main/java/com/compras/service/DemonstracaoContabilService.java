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
public class DemonstracaoContabilService {

    private final DemonstracaoContabilRepository demonstracaoContabilRepository;

    @Transactional(readOnly = true)
    public Page<DemonstracaoContabilListDTO> findAll(Integer exercicio, String tipo,
                                                       String status, Pageable pageable) {
        Page<DemonstracaoContabil> page = demonstracaoContabilRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<DemonstracaoContabilListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public DemonstracaoContabilResponseDTO findById(Long id) {
        DemonstracaoContabil demonstracao = demonstracaoContabilRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_DEMONSTRACAO_NOT_FOUND",
                        "Demonstração contábil não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(demonstracao);
    }

    @Transactional
    public DemonstracaoContabilResponseDTO create(DemonstracaoContabilRequestDTO dto) {
        DemonstracaoContabil demonstracao = new DemonstracaoContabil();
        updateEntity(demonstracao, dto);
        return toResponseDTO(demonstracaoContabilRepository.save(demonstracao));
    }

    @Transactional
    public DemonstracaoContabilResponseDTO update(Long id, DemonstracaoContabilRequestDTO dto) {
        DemonstracaoContabil demonstracao = demonstracaoContabilRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_DEMONSTRACAO_NOT_FOUND",
                        "Demonstração contábil não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(demonstracao, dto);
        return toResponseDTO(demonstracaoContabilRepository.save(demonstracao));
    }

    @Transactional
    public void delete(Long id) {
        if (!demonstracaoContabilRepository.existsById(id)) {
            throw new BusinessException("MC_DEMONSTRACAO_NOT_FOUND",
                    "Demonstração contábil não encontrada", HttpStatus.NOT_FOUND);
        }
        demonstracaoContabilRepository.deleteById(id);
    }

    private void updateEntity(DemonstracaoContabil d, DemonstracaoContabilRequestDTO dto) {
        d.setExercicio(dto.getExercicio());
        d.setTipo(dto.getTipo());
        d.setPeriodoInicio(dto.getPeriodoInicio());
        d.setPeriodoFim(dto.getPeriodoFim());
        d.setStatus(dto.getStatus() != null ? dto.getStatus() : "RASCUNHO");
        d.setResponsavel(dto.getResponsavel());
        d.setObservacoes(dto.getObservacoes());
    }

    private DemonstracaoContabilListDTO toListDTO(DemonstracaoContabil d) {
        return DemonstracaoContabilListDTO.builder()
                .id(d.getId())
                .exercicio(d.getExercicio())
                .tipo(d.getTipo())
                .periodoInicio(d.getPeriodoInicio())
                .periodoFim(d.getPeriodoFim())
                .status(d.getStatus())
                .build();
    }

    private DemonstracaoContabilResponseDTO toResponseDTO(DemonstracaoContabil d) {
        return DemonstracaoContabilResponseDTO.builder()
                .id(d.getId())
                .exercicio(d.getExercicio())
                .tipo(d.getTipo())
                .periodoInicio(d.getPeriodoInicio())
                .periodoFim(d.getPeriodoFim())
                .status(d.getStatus())
                .dataGeracao(d.getDataGeracao())
                .dataPublicacao(d.getDataPublicacao())
                .responsavel(d.getResponsavel())
                .observacoes(d.getObservacoes())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
