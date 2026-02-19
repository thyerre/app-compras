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
public class HistoricoFuncionalService {

    private final HistoricoFuncionalRepository historicoFuncionalRepository;
    private final ServidorRepository servidorRepository;
    private final CargoRepository cargoRepository;
    private final OrgaoRepository orgaoRepository;
    private final UnidadeRepository unidadeRepository;

    @Transactional(readOnly = true)
    public Page<HistoricoFuncionalListDTO> findAll(Long servidorId, String tipoEvento, Pageable pageable) {
        Specification<HistoricoFuncional> spec = buildFilterSpec(servidorId, tipoEvento);
        Page<HistoricoFuncional> page = historicoFuncionalRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<HistoricoFuncional> buildFilterSpec(Long servidorId, String tipoEvento) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (servidorId != null) {
                predicates.add(cb.equal(root.get("servidor").get("id"), servidorId));
            }
            if (tipoEvento != null && !tipoEvento.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("tipoEvento")), "%" + tipoEvento.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public HistoricoFuncionalResponseDTO findById(Long id) {
        HistoricoFuncional e = historicoFuncionalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_HISTORICO_NOT_FOUND", "Histórico funcional não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public HistoricoFuncionalResponseDTO create(HistoricoFuncionalRequestDTO dto) {
        HistoricoFuncional e = toEntity(dto);
        e = historicoFuncionalRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public HistoricoFuncionalResponseDTO update(Long id, HistoricoFuncionalRequestDTO dto) {
        HistoricoFuncional e = historicoFuncionalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_HISTORICO_NOT_FOUND", "Histórico funcional não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(e, dto);
        e = historicoFuncionalRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!historicoFuncionalRepository.existsById(id)) {
            throw new BusinessException("ME_HISTORICO_NOT_FOUND", "Histórico funcional não encontrado", HttpStatus.NOT_FOUND);
        }
        historicoFuncionalRepository.deleteById(id);
    }

    private HistoricoFuncionalListDTO toListDTO(HistoricoFuncional e) {
        return HistoricoFuncionalListDTO.builder()
                .id(e.getId())
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .servidorMatricula(e.getServidor() != null ? e.getServidor().getMatricula() : null)
                .tipoEvento(e.getTipoEvento())
                .dataEvento(e.getDataEvento())
                .descricao(e.getDescricao())
                .build();
    }

    private HistoricoFuncionalResponseDTO toResponseDTO(HistoricoFuncional e) {
        return HistoricoFuncionalResponseDTO.builder()
                .id(e.getId())
                .servidorId(e.getServidor() != null ? e.getServidor().getId() : null)
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .tipoEvento(e.getTipoEvento())
                .dataEvento(e.getDataEvento())
                .numeroAto(e.getNumeroAto())
                .descricao(e.getDescricao())
                .cargoAnteriorId(e.getCargoAnterior() != null ? e.getCargoAnterior().getId() : null)
                .cargoAnteriorDescricao(e.getCargoAnterior() != null ? e.getCargoAnterior().getDescricao() : null)
                .cargoNovoId(e.getCargoNovo() != null ? e.getCargoNovo().getId() : null)
                .cargoNovoDescricao(e.getCargoNovo() != null ? e.getCargoNovo().getDescricao() : null)
                .salarioAnterior(e.getSalarioAnterior())
                .salarioNovo(e.getSalarioNovo())
                .orgaoAnteriorId(e.getOrgaoAnterior() != null ? e.getOrgaoAnterior().getId() : null)
                .orgaoAnteriorNome(e.getOrgaoAnterior() != null ? e.getOrgaoAnterior().getNome() : null)
                .orgaoNovoId(e.getOrgaoNovo() != null ? e.getOrgaoNovo().getId() : null)
                .orgaoNovoNome(e.getOrgaoNovo() != null ? e.getOrgaoNovo().getNome() : null)
                .unidadeAnteriorId(e.getUnidadeAnterior() != null ? e.getUnidadeAnterior().getId() : null)
                .unidadeAnteriorNome(e.getUnidadeAnterior() != null ? e.getUnidadeAnterior().getNome() : null)
                .unidadeNovoId(e.getUnidadeNovo() != null ? e.getUnidadeNovo().getId() : null)
                .unidadeNovoNome(e.getUnidadeNovo() != null ? e.getUnidadeNovo().getNome() : null)
                .observacoes(e.getObservacoes())
                .createdAt(e.getCreatedAt())
                .build();
    }

    private HistoricoFuncional toEntity(HistoricoFuncionalRequestDTO dto) {
        HistoricoFuncional e = new HistoricoFuncional();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(HistoricoFuncional e, HistoricoFuncionalRequestDTO dto) {
        Servidor servidor = servidorRepository.findById(dto.getServidorId())
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        e.setServidor(servidor);
        e.setTipoEvento(dto.getTipoEvento());
        e.setDataEvento(dto.getDataEvento());
        e.setNumeroAto(dto.getNumeroAto());
        e.setDescricao(dto.getDescricao());

        if (dto.getCargoAnteriorId() != null) {
            e.setCargoAnterior(cargoRepository.findById(dto.getCargoAnteriorId()).orElse(null));
        } else {
            e.setCargoAnterior(null);
        }
        if (dto.getCargoNovoId() != null) {
            e.setCargoNovo(cargoRepository.findById(dto.getCargoNovoId()).orElse(null));
        } else {
            e.setCargoNovo(null);
        }

        e.setSalarioAnterior(dto.getSalarioAnterior());
        e.setSalarioNovo(dto.getSalarioNovo());

        if (dto.getOrgaoAnteriorId() != null) {
            e.setOrgaoAnterior(orgaoRepository.findById(dto.getOrgaoAnteriorId()).orElse(null));
        } else {
            e.setOrgaoAnterior(null);
        }
        if (dto.getOrgaoNovoId() != null) {
            e.setOrgaoNovo(orgaoRepository.findById(dto.getOrgaoNovoId()).orElse(null));
        } else {
            e.setOrgaoNovo(null);
        }
        if (dto.getUnidadeAnteriorId() != null) {
            e.setUnidadeAnterior(unidadeRepository.findById(dto.getUnidadeAnteriorId()).orElse(null));
        } else {
            e.setUnidadeAnterior(null);
        }
        if (dto.getUnidadeNovoId() != null) {
            e.setUnidadeNovo(unidadeRepository.findById(dto.getUnidadeNovoId()).orElse(null));
        } else {
            e.setUnidadeNovo(null);
        }

        e.setObservacoes(dto.getObservacoes());
    }
}
