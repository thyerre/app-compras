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
public class LdoService {

    private final LdoRepository ldoRepository;
    private final PpaRepository ppaRepository;
    private final ProgramaRepository programaRepository;
    private final AcaoRepository acaoRepository;

    @Transactional(readOnly = true)
    public Page<LdoListDTO> findAll(Integer exercicio, String status, Pageable pageable) {
        Page<Ldo> page = ldoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<LdoListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public LdoResponseDTO findById(Long id) {
        Ldo ldo = ldoRepository.findByIdWithPrioridades(id)
                .orElseThrow(() -> new BusinessException("ME_LDO_NOT_FOUND",
                        "LDO não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(ldo);
    }

    @Transactional
    public LdoResponseDTO create(LdoRequestDTO dto) {
        Ppa ppa = ppaRepository.findById(dto.getPpaId())
                .orElseThrow(() -> new BusinessException("ME_PPA_NOT_FOUND", "PPA não encontrado"));

        Ldo ldo = Ldo.builder()
                .exercicio(dto.getExercicio())
                .ppa(ppa)
                .descricao(dto.getDescricao())
                .status(dto.getStatus() != null ? dto.getStatus() : "ELABORACAO")
                .observacoes(dto.getObservacoes())
                .build();

        ldo = ldoRepository.save(ldo);
        syncPrioridades(ldo, dto.getPrioridades());
        return toResponseDTO(ldoRepository.save(ldo));
    }

    @Transactional
    public LdoResponseDTO update(Long id, LdoRequestDTO dto) {
        Ldo ldo = ldoRepository.findByIdWithPrioridades(id)
                .orElseThrow(() -> new BusinessException("ME_LDO_NOT_FOUND",
                        "LDO não encontrada", HttpStatus.NOT_FOUND));

        Ppa ppa = ppaRepository.findById(dto.getPpaId())
                .orElseThrow(() -> new BusinessException("ME_PPA_NOT_FOUND", "PPA não encontrado"));

        ldo.setExercicio(dto.getExercicio());
        ldo.setPpa(ppa);
        ldo.setDescricao(dto.getDescricao());
        if (dto.getStatus() != null) ldo.setStatus(dto.getStatus());
        ldo.setObservacoes(dto.getObservacoes());

        syncPrioridades(ldo, dto.getPrioridades());
        return toResponseDTO(ldoRepository.save(ldo));
    }

    @Transactional
    public void delete(Long id) {
        if (!ldoRepository.existsById(id)) {
            throw new BusinessException("ME_LDO_NOT_FOUND",
                    "LDO não encontrada", HttpStatus.NOT_FOUND);
        }
        ldoRepository.deleteById(id);
    }

    private void syncPrioridades(Ldo ldo, List<LdoPrioridadeDTO> dtos) {
        if (dtos == null) return;
        ldo.getPrioridades().clear();
        for (LdoPrioridadeDTO dto : dtos) {
            Programa programa = programaRepository.findById(dto.getProgramaId())
                    .orElseThrow(() -> new BusinessException("ME_PROGRAMA_NOT_FOUND", "Programa não encontrado"));
            Acao acao = acaoRepository.findById(dto.getAcaoId())
                    .orElseThrow(() -> new BusinessException("ME_ACAO_NOT_FOUND", "Ação não encontrada"));

            LdoPrioridade prioridade = LdoPrioridade.builder()
                    .ldo(ldo)
                    .programa(programa)
                    .acao(acao)
                    .meta(dto.getMeta())
                    .valorEstimado(dto.getValorEstimado())
                    .prioridade(dto.getPrioridade())
                    .build();
            ldo.getPrioridades().add(prioridade);
        }
    }

    private LdoListDTO toListDTO(Ldo l) {
        return LdoListDTO.builder()
                .id(l.getId())
                .exercicio(l.getExercicio())
                .descricao(l.getDescricao())
                .status(l.getStatus())
                .ppaPeriodo(l.getPpa() != null ?
                        l.getPpa().getAnoInicio() + "-" + l.getPpa().getAnoFim() : null)
                .build();
    }

    private LdoResponseDTO toResponseDTO(Ldo l) {
        return LdoResponseDTO.builder()
                .id(l.getId())
                .exercicio(l.getExercicio())
                .ppa(l.getPpa() != null ? PpaListDTO.builder()
                        .id(l.getPpa().getId())
                        .exercicioInicio(l.getPpa().getAnoInicio())
                        .exercicioFim(l.getPpa().getAnoFim())
                        .descricao(l.getPpa().getDescricao())
                        .status(l.getPpa().getStatus())
                        .build() : null)
                .descricao(l.getDescricao())
                .status(l.getStatus())
                .dataAprovacao(l.getDataAprovacao())
                .observacoes(l.getObservacoes())
                .createdAt(l.getCreatedAt())
                .prioridades(l.getPrioridades() != null ? l.getPrioridades().stream()
                        .map(p -> LdoPrioridadeDTO.builder()
                                .id(p.getId())
                                .programaId(p.getPrograma().getId())
                                .programaNome(p.getPrograma().getNome())
                                .acaoId(p.getAcao().getId())
                                .acaoNome(p.getAcao().getNome())
                                .meta(p.getMeta())
                                .valorEstimado(p.getValorEstimado())
                                .prioridade(p.getPrioridade())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
