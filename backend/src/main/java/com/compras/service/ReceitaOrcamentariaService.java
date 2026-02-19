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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceitaOrcamentariaService {

    private final ReceitaOrcamentariaRepository receitaOrcamentariaRepository;
    private final LoaRepository loaRepository;
    private final FonteRecursoRepository fonteRecursoRepository;

    @Transactional(readOnly = true)
    public Page<ReceitaOrcamentariaListDTO> findAll(Integer exercicio, String codigoReceita,
                                                     Pageable pageable) {
        Page<ReceitaOrcamentaria> page = receitaOrcamentariaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (codigoReceita != null && !codigoReceita.isBlank()) {
                predicates.add(cb.like(root.get("codigoReceita"), "%" + codigoReceita + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<ReceitaOrcamentariaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ReceitaOrcamentariaResponseDTO findById(Long id) {
        ReceitaOrcamentaria receita = receitaOrcamentariaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RECEITA_ORC_NOT_FOUND",
                        "Receita orçamentária não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(receita);
    }

    @Transactional
    public ReceitaOrcamentariaResponseDTO create(ReceitaOrcamentariaRequestDTO dto) {
        ReceitaOrcamentaria receita = new ReceitaOrcamentaria();
        updateEntity(receita, dto);
        return toResponseDTO(receitaOrcamentariaRepository.save(receita));
    }

    @Transactional
    public ReceitaOrcamentariaResponseDTO update(Long id, ReceitaOrcamentariaRequestDTO dto) {
        ReceitaOrcamentaria receita = receitaOrcamentariaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RECEITA_ORC_NOT_FOUND",
                        "Receita orçamentária não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(receita, dto);
        return toResponseDTO(receitaOrcamentariaRepository.save(receita));
    }

    @Transactional
    public void delete(Long id) {
        if (!receitaOrcamentariaRepository.existsById(id)) {
            throw new BusinessException("MC_RECEITA_ORC_NOT_FOUND",
                    "Receita orçamentária não encontrada", HttpStatus.NOT_FOUND);
        }
        receitaOrcamentariaRepository.deleteById(id);
    }

    private void updateEntity(ReceitaOrcamentaria r, ReceitaOrcamentariaRequestDTO dto) {
        r.setLoa(loaRepository.findById(dto.getLoaId())
                .orElseThrow(() -> new BusinessException("MC_RECEITA_ORC_LOA_NOT_FOUND", "LOA não encontrada")));
        if (dto.getFonteRecursoId() != null) {
            r.setFonteRecurso(fonteRecursoRepository.findById(dto.getFonteRecursoId())
                    .orElseThrow(() -> new BusinessException("MC_RECEITA_ORC_FONTE_NOT_FOUND",
                            "Fonte de recurso não encontrada")));
        }
        r.setExercicio(dto.getExercicio());
        r.setCodigoReceita(dto.getCodigoReceita());
        r.setDescricao(dto.getDescricao());
        r.setCategoriaEconomica(dto.getCategoriaEconomica());
        r.setOrigem(dto.getOrigem());
        r.setEspecie(dto.getEspecie());
        r.setValorPrevistoInicial(dto.getValorPrevistoInicial() != null ? dto.getValorPrevistoInicial() : BigDecimal.ZERO);
        r.setValorPrevistoAtualizado(dto.getValorPrevistoAtualizado() != null ? dto.getValorPrevistoAtualizado() : BigDecimal.ZERO);
        r.setValorLancado(dto.getValorLancado() != null ? dto.getValorLancado() : BigDecimal.ZERO);
        r.setValorArrecadado(dto.getValorArrecadado() != null ? dto.getValorArrecadado() : BigDecimal.ZERO);
        r.setValorRecolhido(dto.getValorRecolhido() != null ? dto.getValorRecolhido() : BigDecimal.ZERO);
    }

    private ReceitaOrcamentariaListDTO toListDTO(ReceitaOrcamentaria r) {
        return ReceitaOrcamentariaListDTO.builder()
                .id(r.getId())
                .exercicio(r.getExercicio())
                .codigoReceita(r.getCodigoReceita())
                .descricao(r.getDescricao())
                .categoriaEconomica(r.getCategoriaEconomica())
                .valorPrevistoInicial(r.getValorPrevistoInicial())
                .valorArrecadado(r.getValorArrecadado())
                .valorRecolhido(r.getValorRecolhido())
                .build();
    }

    private ReceitaOrcamentariaResponseDTO toResponseDTO(ReceitaOrcamentaria r) {
        return ReceitaOrcamentariaResponseDTO.builder()
                .id(r.getId())
                .exercicio(r.getExercicio())
                .loa(r.getLoa() != null ? LoaListDTO.builder()
                        .id(r.getLoa().getId()).exercicio(r.getLoa().getExercicio())
                        .descricao(r.getLoa().getDescricao()).status(r.getLoa().getStatus())
                        .build() : null)
                .codigoReceita(r.getCodigoReceita())
                .descricao(r.getDescricao())
                .categoriaEconomica(r.getCategoriaEconomica())
                .origem(r.getOrigem())
                .especie(r.getEspecie())
                .fonteRecurso(r.getFonteRecurso() != null ? FonteRecursoDTO.builder()
                        .id(r.getFonteRecurso().getId()).codigo(r.getFonteRecurso().getCodigo())
                        .descricao(r.getFonteRecurso().getDescricao()).build() : null)
                .valorPrevistoInicial(r.getValorPrevistoInicial())
                .valorPrevistoAtualizado(r.getValorPrevistoAtualizado())
                .valorLancado(r.getValorLancado())
                .valorArrecadado(r.getValorArrecadado())
                .valorRecolhido(r.getValorRecolhido())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
