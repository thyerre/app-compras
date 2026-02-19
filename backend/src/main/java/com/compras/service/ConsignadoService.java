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
public class ConsignadoService {

    private final ConsignadoRepository consignadoRepository;
    private final ServidorRepository servidorRepository;
    private final EventoFolhaRepository eventoFolhaRepository;

    @Transactional(readOnly = true)
    public Page<ConsignadoListDTO> findAll(Long servidorId, String status, String consignataria, Pageable pageable) {
        Specification<Consignado> spec = buildFilterSpec(servidorId, status, consignataria);
        Page<Consignado> page = consignadoRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<Consignado> buildFilterSpec(Long servidorId, String status, String consignataria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (servidorId != null) {
                predicates.add(cb.equal(root.get("servidor").get("id"), servidorId));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (consignataria != null && !consignataria.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("consignataria")), "%" + consignataria.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public ConsignadoResponseDTO findById(Long id) {
        Consignado e = consignadoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_CONSIGNADO_NOT_FOUND", "Consignado não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public ConsignadoResponseDTO create(ConsignadoRequestDTO dto) {
        Consignado e = toEntity(dto);
        e = consignadoRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public ConsignadoResponseDTO update(Long id, ConsignadoRequestDTO dto) {
        Consignado e = consignadoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_CONSIGNADO_NOT_FOUND", "Consignado não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(e, dto);
        e = consignadoRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!consignadoRepository.existsById(id)) {
            throw new BusinessException("ME_CONSIGNADO_NOT_FOUND", "Consignado não encontrado", HttpStatus.NOT_FOUND);
        }
        consignadoRepository.deleteById(id);
    }

    private ConsignadoListDTO toListDTO(Consignado e) {
        return ConsignadoListDTO.builder()
                .id(e.getId())
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .servidorMatricula(e.getServidor() != null ? e.getServidor().getMatricula() : null)
                .consignataria(e.getConsignataria())
                .contrato(e.getContrato())
                .parcelaAtual(e.getParcelaAtual())
                .parcelaTotal(e.getParcelaTotal())
                .valorParcela(e.getValorParcela())
                .status(e.getStatus())
                .build();
    }

    private ConsignadoResponseDTO toResponseDTO(Consignado e) {
        return ConsignadoResponseDTO.builder()
                .id(e.getId())
                .servidorId(e.getServidor() != null ? e.getServidor().getId() : null)
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .eventoFolhaId(e.getEventoFolha() != null ? e.getEventoFolha().getId() : null)
                .eventoFolhaDescricao(e.getEventoFolha() != null ? e.getEventoFolha().getDescricao() : null)
                .consignataria(e.getConsignataria())
                .contrato(e.getContrato())
                .parcelaAtual(e.getParcelaAtual())
                .parcelaTotal(e.getParcelaTotal())
                .valorParcela(e.getValorParcela())
                .valorTotal(e.getValorTotal())
                .dataInicio(e.getDataInicio())
                .dataFim(e.getDataFim())
                .status(e.getStatus())
                .observacoes(e.getObservacoes())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private Consignado toEntity(ConsignadoRequestDTO dto) {
        Consignado e = new Consignado();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(Consignado e, ConsignadoRequestDTO dto) {
        Servidor servidor = servidorRepository.findById(dto.getServidorId())
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        e.setServidor(servidor);

        if (dto.getEventoFolhaId() != null) {
            EventoFolha ef = eventoFolhaRepository.findById(dto.getEventoFolhaId())
                    .orElseThrow(() -> new BusinessException("ME_EVENTO_FOLHA_NOT_FOUND", "Evento de folha não encontrado", HttpStatus.NOT_FOUND));
            e.setEventoFolha(ef);
        } else {
            e.setEventoFolha(null);
        }

        e.setConsignataria(dto.getConsignataria());
        e.setContrato(dto.getContrato());
        e.setParcelaAtual(dto.getParcelaAtual() != null ? dto.getParcelaAtual() : 0);
        e.setParcelaTotal(dto.getParcelaTotal());
        e.setValorParcela(dto.getValorParcela());
        e.setValorTotal(dto.getValorTotal());
        e.setDataInicio(dto.getDataInicio());
        e.setDataFim(dto.getDataFim());
        e.setStatus(dto.getStatus() != null ? dto.getStatus() : "ATIVO");
        e.setObservacoes(dto.getObservacoes());
    }
}
