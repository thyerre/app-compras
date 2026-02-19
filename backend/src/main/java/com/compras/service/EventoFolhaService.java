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
public class EventoFolhaService {

    private final EventoFolhaRepository eventoFolhaRepository;

    @Transactional(readOnly = true)
    public Page<EventoFolhaListDTO> findAll(String descricao, String tipo, Boolean ativo, Pageable pageable) {
        Specification<EventoFolha> spec = buildFilterSpec(descricao, tipo, ativo);
        Page<EventoFolha> page = eventoFolhaRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<EventoFolha> buildFilterSpec(String descricao, String tipo, Boolean ativo) {
        return (root, query, cb) -> {
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
        };
    }

    @Transactional(readOnly = true)
    public EventoFolhaResponseDTO findById(Integer id) {
        EventoFolha e = eventoFolhaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_EVENTO_FOLHA_NOT_FOUND", "Evento de folha não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public EventoFolhaResponseDTO create(EventoFolhaRequestDTO dto) {
        validateCodigoUnique(dto.getCodigo(), null);
        EventoFolha e = toEntity(dto);
        e = eventoFolhaRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public EventoFolhaResponseDTO update(Integer id, EventoFolhaRequestDTO dto) {
        EventoFolha e = eventoFolhaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_EVENTO_FOLHA_NOT_FOUND", "Evento de folha não encontrado", HttpStatus.NOT_FOUND));
        validateCodigoUnique(dto.getCodigo(), id);
        updateEntity(e, dto);
        e = eventoFolhaRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Integer id) {
        if (!eventoFolhaRepository.existsById(id)) {
            throw new BusinessException("ME_EVENTO_FOLHA_NOT_FOUND", "Evento de folha não encontrado", HttpStatus.NOT_FOUND);
        }
        eventoFolhaRepository.deleteById(id);
    }

    private void validateCodigoUnique(String codigo, Integer excludeId) {
        boolean exists = excludeId == null
                ? eventoFolhaRepository.existsByCodigo(codigo)
                : eventoFolhaRepository.existsByCodigoAndIdNot(codigo, excludeId);
        if (exists) {
            throw new BusinessException("ME_EVENTO_FOLHA_CODIGO_DUPLICATE", "Já existe um evento de folha com este código");
        }
    }

    private EventoFolhaListDTO toListDTO(EventoFolha e) {
        return EventoFolhaListDTO.builder()
                .id(e.getId())
                .codigo(e.getCodigo())
                .descricao(e.getDescricao())
                .tipo(e.getTipo())
                .percentual(e.getPercentual())
                .valorFixo(e.getValorFixo())
                .ativo(e.getAtivo())
                .build();
    }

    private EventoFolhaResponseDTO toResponseDTO(EventoFolha e) {
        return EventoFolhaResponseDTO.builder()
                .id(e.getId())
                .codigo(e.getCodigo())
                .descricao(e.getDescricao())
                .tipo(e.getTipo())
                .incidenciaInss(e.getIncidenciaInss())
                .incidenciaIrrf(e.getIncidenciaIrrf())
                .incidenciaFgts(e.getIncidenciaFgts())
                .automatico(e.getAutomatico())
                .formula(e.getFormula())
                .percentual(e.getPercentual())
                .valorFixo(e.getValorFixo())
                .tipoCalculo(e.getTipoCalculo())
                .aplicaMensal(e.getAplicaMensal())
                .aplicaFerias(e.getAplicaFerias())
                .aplica13(e.getAplica13())
                .aplicaRescisao(e.getAplicaRescisao())
                .ativo(e.getAtivo())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private EventoFolha toEntity(EventoFolhaRequestDTO dto) {
        EventoFolha e = new EventoFolha();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(EventoFolha e, EventoFolhaRequestDTO dto) {
        e.setCodigo(dto.getCodigo());
        e.setDescricao(dto.getDescricao());
        e.setTipo(dto.getTipo());
        e.setIncidenciaInss(dto.getIncidenciaInss() != null ? dto.getIncidenciaInss() : false);
        e.setIncidenciaIrrf(dto.getIncidenciaIrrf() != null ? dto.getIncidenciaIrrf() : false);
        e.setIncidenciaFgts(dto.getIncidenciaFgts() != null ? dto.getIncidenciaFgts() : false);
        e.setAutomatico(dto.getAutomatico() != null ? dto.getAutomatico() : false);
        e.setFormula(dto.getFormula());
        e.setPercentual(dto.getPercentual());
        e.setValorFixo(dto.getValorFixo());
        e.setTipoCalculo(dto.getTipoCalculo());
        e.setAplicaMensal(dto.getAplicaMensal() != null ? dto.getAplicaMensal() : true);
        e.setAplicaFerias(dto.getAplicaFerias() != null ? dto.getAplicaFerias() : false);
        e.setAplica13(dto.getAplica13() != null ? dto.getAplica13() : false);
        e.setAplicaRescisao(dto.getAplicaRescisao() != null ? dto.getAplicaRescisao() : false);
        e.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }
}
