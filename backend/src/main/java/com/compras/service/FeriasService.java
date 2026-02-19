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
public class FeriasService {

    private final FeriasRepository feriasRepository;
    private final ServidorRepository servidorRepository;

    @Transactional(readOnly = true)
    public Page<FeriasListDTO> findAll(Long servidorId, String status, Pageable pageable) {
        Specification<Ferias> spec = buildFilterSpec(servidorId, status);
        Page<Ferias> page = feriasRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<Ferias> buildFilterSpec(Long servidorId, String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (servidorId != null) {
                predicates.add(cb.equal(root.get("servidor").get("id"), servidorId));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public FeriasResponseDTO findById(Long id) {
        Ferias e = feriasRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_FERIAS_NOT_FOUND", "Férias não encontradas", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public FeriasResponseDTO create(FeriasRequestDTO dto) {
        Ferias e = toEntity(dto);
        e = feriasRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public FeriasResponseDTO update(Long id, FeriasRequestDTO dto) {
        Ferias e = feriasRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_FERIAS_NOT_FOUND", "Férias não encontradas", HttpStatus.NOT_FOUND));
        updateEntity(e, dto);
        e = feriasRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!feriasRepository.existsById(id)) {
            throw new BusinessException("ME_FERIAS_NOT_FOUND", "Férias não encontradas", HttpStatus.NOT_FOUND);
        }
        feriasRepository.deleteById(id);
    }

    private FeriasListDTO toListDTO(Ferias e) {
        return FeriasListDTO.builder()
                .id(e.getId())
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .servidorMatricula(e.getServidor() != null ? e.getServidor().getMatricula() : null)
                .periodoAquisitivoInicio(e.getPeriodoAquisitivoInicio())
                .periodoAquisitivoFim(e.getPeriodoAquisitivoFim())
                .periodoGozoInicio(e.getPeriodoGozoInicio())
                .periodoGozoFim(e.getPeriodoGozoFim())
                .diasGozo(e.getDiasGozo())
                .status(e.getStatus())
                .build();
    }

    private FeriasResponseDTO toResponseDTO(Ferias e) {
        return FeriasResponseDTO.builder()
                .id(e.getId())
                .servidorId(e.getServidor() != null ? e.getServidor().getId() : null)
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .periodoAquisitivoInicio(e.getPeriodoAquisitivoInicio())
                .periodoAquisitivoFim(e.getPeriodoAquisitivoFim())
                .periodoGozoInicio(e.getPeriodoGozoInicio())
                .periodoGozoFim(e.getPeriodoGozoFim())
                .diasGozo(e.getDiasGozo())
                .diasAbono(e.getDiasAbono())
                .parcelas(e.getParcelas())
                .valorFerias(e.getValorFerias())
                .valorAbono(e.getValorAbono())
                .valor13Ferias(e.getValor13Ferias())
                .status(e.getStatus())
                .observacoes(e.getObservacoes())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private Ferias toEntity(FeriasRequestDTO dto) {
        Ferias e = new Ferias();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(Ferias e, FeriasRequestDTO dto) {
        Servidor servidor = servidorRepository.findById(dto.getServidorId())
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        e.setServidor(servidor);
        e.setPeriodoAquisitivoInicio(dto.getPeriodoAquisitivoInicio());
        e.setPeriodoAquisitivoFim(dto.getPeriodoAquisitivoFim());
        e.setPeriodoGozoInicio(dto.getPeriodoGozoInicio());
        e.setPeriodoGozoFim(dto.getPeriodoGozoFim());
        e.setDiasGozo(dto.getDiasGozo());
        e.setDiasAbono(dto.getDiasAbono() != null ? dto.getDiasAbono() : 0);
        e.setParcelas(dto.getParcelas() != null ? dto.getParcelas() : 1);
        e.setValorFerias(dto.getValorFerias());
        e.setValorAbono(dto.getValorAbono());
        e.setValor13Ferias(dto.getValor13Ferias());
        e.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDENTE");
        e.setObservacoes(dto.getObservacoes());
    }
}
