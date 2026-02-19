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
public class FolhaPagamentoService {

    private final FolhaPagamentoRepository folhaPagamentoRepository;

    @Transactional(readOnly = true)
    public Page<FolhaPagamentoListDTO> findAll(String competencia, String tipo, String status, Pageable pageable) {
        Specification<FolhaPagamento> spec = buildFilterSpec(competencia, tipo, status);
        Page<FolhaPagamento> page = folhaPagamentoRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<FolhaPagamento> buildFilterSpec(String competencia, String tipo, String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (competencia != null && !competencia.isBlank()) {
                predicates.add(cb.like(root.get("competencia"), "%" + competencia + "%"));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public FolhaPagamentoResponseDTO findById(Long id) {
        FolhaPagamento f = folhaPagamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_FOLHA_NOT_FOUND", "Folha de pagamento não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(f);
    }

    @Transactional
    public FolhaPagamentoResponseDTO create(FolhaPagamentoRequestDTO dto) {
        FolhaPagamento f = toEntity(dto);
        f = folhaPagamentoRepository.save(f);
        return toResponseDTO(f);
    }

    @Transactional
    public FolhaPagamentoResponseDTO update(Long id, FolhaPagamentoRequestDTO dto) {
        FolhaPagamento f = folhaPagamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_FOLHA_NOT_FOUND", "Folha de pagamento não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(f, dto);
        f = folhaPagamentoRepository.save(f);
        return toResponseDTO(f);
    }

    @Transactional
    public void delete(Long id) {
        if (!folhaPagamentoRepository.existsById(id)) {
            throw new BusinessException("ME_FOLHA_NOT_FOUND", "Folha de pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        folhaPagamentoRepository.deleteById(id);
    }

    private FolhaPagamentoListDTO toListDTO(FolhaPagamento f) {
        return FolhaPagamentoListDTO.builder()
                .id(f.getId())
                .competencia(f.getCompetencia())
                .tipo(f.getTipo())
                .totalProventos(f.getTotalProventos())
                .totalDescontos(f.getTotalDescontos())
                .totalLiquido(f.getTotalLiquido())
                .quantidadeServidores(f.getQuantidadeServidores())
                .status(f.getStatus())
                .build();
    }

    private FolhaPagamentoResponseDTO toResponseDTO(FolhaPagamento f) {
        return FolhaPagamentoResponseDTO.builder()
                .id(f.getId())
                .competencia(f.getCompetencia())
                .tipo(f.getTipo())
                .dataPagamento(f.getDataPagamento() != null ? f.getDataPagamento().toString() : null)
                .totalProventos(f.getTotalProventos())
                .totalDescontos(f.getTotalDescontos())
                .totalLiquido(f.getTotalLiquido())
                .quantidadeServidores(f.getQuantidadeServidores())
                .status(f.getStatus())
                .observacoes(f.getObservacoes())
                .createdAt(f.getCreatedAt())
                .updatedAt(f.getUpdatedAt())
                .build();
    }

    private FolhaPagamento toEntity(FolhaPagamentoRequestDTO dto) {
        FolhaPagamento f = new FolhaPagamento();
        updateEntity(f, dto);
        return f;
    }

    private void updateEntity(FolhaPagamento f, FolhaPagamentoRequestDTO dto) {
        f.setCompetencia(dto.getCompetencia());
        f.setTipo(dto.getTipo() != null ? dto.getTipo() : "NORMAL");
        if (dto.getDataPagamento() != null && !dto.getDataPagamento().isBlank()) {
            f.setDataPagamento(java.time.LocalDate.parse(dto.getDataPagamento()));
        }
        f.setStatus(dto.getStatus() != null ? dto.getStatus() : "ABERTA");
        f.setObservacoes(dto.getObservacoes());
    }
}
