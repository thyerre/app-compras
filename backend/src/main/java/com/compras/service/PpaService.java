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
public class PpaService {

    private final PpaRepository ppaRepository;
    private final ProgramaRepository programaRepository;

    @Transactional(readOnly = true)
    public Page<PpaListDTO> findAll(String status, Integer exercicio, Pageable pageable) {
        Page<Ppa> page = ppaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (exercicio != null) {
                predicates.add(cb.and(
                        cb.lessThanOrEqualTo(root.get("anoInicio"), exercicio),
                        cb.greaterThanOrEqualTo(root.get("anoFim"), exercicio)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<PpaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PpaResponseDTO findById(Long id) {
        Ppa ppa = ppaRepository.findByIdWithProgramas(id)
                .orElseThrow(() -> new BusinessException("ME_PPA_NOT_FOUND",
                        "PPA não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(ppa);
    }

    @Transactional
    public PpaResponseDTO create(PpaRequestDTO dto) {
        Ppa ppa = Ppa.builder()
                .anoInicio(dto.getExercicioInicio())
                .anoFim(dto.getExercicioFim())
                .descricao(dto.getDescricao())
                .status(dto.getStatus() != null ? dto.getStatus() : "ELABORACAO")
                .observacoes(dto.getObservacoes())
                .build();

        ppa = ppaRepository.save(ppa);
        syncProgramas(ppa, dto.getProgramas());
        return toResponseDTO(ppaRepository.save(ppa));
    }

    @Transactional
    public PpaResponseDTO update(Long id, PpaRequestDTO dto) {
        Ppa ppa = ppaRepository.findByIdWithProgramas(id)
                .orElseThrow(() -> new BusinessException("ME_PPA_NOT_FOUND",
                        "PPA não encontrado", HttpStatus.NOT_FOUND));

        ppa.setAnoInicio(dto.getExercicioInicio());
        ppa.setAnoFim(dto.getExercicioFim());
        ppa.setDescricao(dto.getDescricao());
        if (dto.getStatus() != null) ppa.setStatus(dto.getStatus());
        ppa.setObservacoes(dto.getObservacoes());

        syncProgramas(ppa, dto.getProgramas());
        return toResponseDTO(ppaRepository.save(ppa));
    }

    @Transactional
    public void delete(Long id) {
        if (!ppaRepository.existsById(id)) {
            throw new BusinessException("ME_PPA_NOT_FOUND",
                    "PPA não encontrado", HttpStatus.NOT_FOUND);
        }
        ppaRepository.deleteById(id);
    }

    private void syncProgramas(Ppa ppa, List<PpaProgramaDTO> dtos) {
        if (dtos == null) return;
        ppa.getProgramas().clear();
        for (PpaProgramaDTO dto : dtos) {
            Programa programa = programaRepository.findById(dto.getProgramaId())
                    .orElseThrow(() -> new BusinessException("ME_PROGRAMA_NOT_FOUND", "Programa não encontrado"));

            PpaPrograma pp = PpaPrograma.builder()
                    .ppa(ppa)
                    .programa(programa)
                    .valorGlobal(dto.getValorPrevisto() != null ? dto.getValorPrevisto() : java.math.BigDecimal.ZERO)
                    .observacoes(null)
                    .build();

            if (dto.getMetas() != null) {
                for (PpaProgramaMetaDTO metaDTO : dto.getMetas()) {
                    PpaProgramaMeta meta = PpaProgramaMeta.builder()
                            .ppaPrograma(pp)
                            .exercicio(metaDTO.getExercicio())
                            .valorPrevisto(metaDTO.getValorPrevisto())
                            .metaFisica(metaDTO.getQuantidadePrevista())
                            .observacoes(metaDTO.getDescricao())
                            .build();
                    pp.getMetas().add(meta);
                }
            }
            ppa.getProgramas().add(pp);
        }
    }

    private PpaListDTO toListDTO(Ppa p) {
        return PpaListDTO.builder()
                .id(p.getId())
                .exercicioInicio(p.getAnoInicio())
                .exercicioFim(p.getAnoFim())
                .descricao(p.getDescricao())
                .status(p.getStatus())
                .build();
    }

    private PpaResponseDTO toResponseDTO(Ppa p) {
        return PpaResponseDTO.builder()
                .id(p.getId())
                .exercicioInicio(p.getAnoInicio())
                .exercicioFim(p.getAnoFim())
                .descricao(p.getDescricao())
                .status(p.getStatus())
                .dataAprovacao(p.getLeiData())
                .observacoes(p.getObservacoes())
                .createdAt(p.getCreatedAt())
                .programas(p.getProgramas() != null ? p.getProgramas().stream()
                        .map(pp -> PpaProgramaResponseDTO.builder()
                                .id(pp.getId())
                                .programa(ProgramaListDTO.builder()
                                        .id(pp.getPrograma().getId())
                                        .codigo(pp.getPrograma().getCodigo())
                                        .nome(pp.getPrograma().getNome())
                                        .build())
                                .valorPrevisto(pp.getValorGlobal())
                                .indicador(null)
                                .indiceRecente(null)
                                .indiceDesejado(null)
                                .metas(pp.getMetas() != null ? pp.getMetas().stream()
                                        .map(m -> PpaProgramaMetaDTO.builder()
                                                .id(m.getId())
                                                .descricao(m.getObservacoes())
                                                .exercicio(m.getExercicio())
                                                .valorPrevisto(m.getValorPrevisto())
                                                .unidadeMedida(null)
                                                .quantidadePrevista(m.getMetaFisica())
                                                .build())
                                        .collect(Collectors.toList()) : new ArrayList<>())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
