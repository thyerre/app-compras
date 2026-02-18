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
public class LoaService {

    private final LoaRepository loaRepository;
    private final LdoRepository ldoRepository;

    @Transactional(readOnly = true)
    public Page<LoaListDTO> findAll(Integer exercicio, String status, Pageable pageable) {
        Page<Loa> page = loaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<LoaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public LoaResponseDTO findById(Long id) {
        Loa loa = loaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_LOA_NOT_FOUND",
                        "LOA não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(loa);
    }

    @Transactional
    public LoaResponseDTO create(LoaRequestDTO dto) {
        Ldo ldo = ldoRepository.findById(dto.getLdoId())
                .orElseThrow(() -> new BusinessException("ME_LDO_NOT_FOUND", "LDO não encontrada"));

        Loa loa = Loa.builder()
                .exercicio(dto.getExercicio())
                .ldo(ldo)
                .descricao(dto.getDescricao())
                .valorTotalReceita(dto.getValorTotalReceita())
                .valorTotalDespesa(dto.getValorTotalDespesa())
                .status(dto.getStatus() != null ? dto.getStatus() : "ELABORACAO")
                .observacoes(dto.getObservacoes())
                .build();

        return toResponseDTO(loaRepository.save(loa));
    }

    @Transactional
    public LoaResponseDTO update(Long id, LoaRequestDTO dto) {
        Loa loa = loaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_LOA_NOT_FOUND",
                        "LOA não encontrada", HttpStatus.NOT_FOUND));

        Ldo ldo = ldoRepository.findById(dto.getLdoId())
                .orElseThrow(() -> new BusinessException("ME_LDO_NOT_FOUND", "LDO não encontrada"));

        loa.setExercicio(dto.getExercicio());
        loa.setLdo(ldo);
        loa.setDescricao(dto.getDescricao());
        loa.setValorTotalReceita(dto.getValorTotalReceita());
        loa.setValorTotalDespesa(dto.getValorTotalDespesa());
        if (dto.getStatus() != null) loa.setStatus(dto.getStatus());
        loa.setObservacoes(dto.getObservacoes());

        return toResponseDTO(loaRepository.save(loa));
    }

    @Transactional
    public void delete(Long id) {
        if (!loaRepository.existsById(id)) {
            throw new BusinessException("ME_LOA_NOT_FOUND",
                    "LOA não encontrada", HttpStatus.NOT_FOUND);
        }
        loaRepository.deleteById(id);
    }

    private LoaListDTO toListDTO(Loa l) {
        return LoaListDTO.builder()
                .id(l.getId()).exercicio(l.getExercicio())
                .descricao(l.getDescricao())
                .valorTotalReceita(l.getValorTotalReceita())
                .valorTotalDespesa(l.getValorTotalDespesa())
                .status(l.getStatus())
                .build();
    }

    private LoaResponseDTO toResponseDTO(Loa l) {
        return LoaResponseDTO.builder()
                .id(l.getId()).exercicio(l.getExercicio())
                .ldo(l.getLdo() != null ? LdoListDTO.builder()
                        .id(l.getLdo().getId())
                        .exercicio(l.getLdo().getExercicio())
                        .descricao(l.getLdo().getDescricao())
                        .status(l.getLdo().getStatus())
                        .build() : null)
                .descricao(l.getDescricao())
                .valorTotalReceita(l.getValorTotalReceita())
                .valorTotalDespesa(l.getValorTotalDespesa())
                .status(l.getStatus())
                .dataAprovacao(l.getDataAprovacao())
                .observacoes(l.getObservacoes())
                .createdAt(l.getCreatedAt())
                .build();
    }
}
