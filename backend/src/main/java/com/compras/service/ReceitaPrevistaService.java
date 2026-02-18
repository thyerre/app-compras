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
public class ReceitaPrevistaService {

    private final ReceitaPrevistaRepository receitaRepository;
    private final LoaRepository loaRepository;
    private final FonteRecursoRepository fonteRecursoRepository;

    @Transactional(readOnly = true)
    public Page<ReceitaPrevistaListDTO> findAll(Integer exercicio, Pageable pageable) {
        Page<ReceitaPrevista> page = receitaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("loa").get("exercicio"), exercicio));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<ReceitaPrevistaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ReceitaPrevistaResponseDTO findById(Long id) {
        ReceitaPrevista receita = receitaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_RECEITA_NOT_FOUND",
                        "Receita prevista não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(receita);
    }

    @Transactional
    public ReceitaPrevistaResponseDTO create(ReceitaPrevistaRequestDTO dto) {
        ReceitaPrevista receita = new ReceitaPrevista();
        updateEntity(receita, dto);
        return toResponseDTO(receitaRepository.save(receita));
    }

    @Transactional
    public ReceitaPrevistaResponseDTO update(Long id, ReceitaPrevistaRequestDTO dto) {
        ReceitaPrevista receita = receitaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_RECEITA_NOT_FOUND",
                        "Receita prevista não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(receita, dto);
        return toResponseDTO(receitaRepository.save(receita));
    }

    @Transactional
    public void delete(Long id) {
        if (!receitaRepository.existsById(id)) {
            throw new BusinessException("ME_RECEITA_NOT_FOUND",
                    "Receita prevista não encontrada", HttpStatus.NOT_FOUND);
        }
        receitaRepository.deleteById(id);
    }

    private void updateEntity(ReceitaPrevista r, ReceitaPrevistaRequestDTO dto) {
        r.setLoa(loaRepository.findById(dto.getLoaId())
                .orElseThrow(() -> new BusinessException("ME_LOA_NOT_FOUND", "LOA não encontrada")));
        r.setCodigo(dto.getCodigoReceita());
        r.setDescricao(dto.getDescricao());
        r.setFonteRecurso(fonteRecursoRepository.findById(dto.getFonteRecursoId())
                .orElseThrow(() -> new BusinessException("ME_FONTE_NOT_FOUND", "Fonte de recurso não encontrada")));
        r.setValorPrevisto(dto.getValorPrevisto());
        if (dto.getValorArrecadado() != null) r.setValorArrecadado(dto.getValorArrecadado());
    }

    private ReceitaPrevistaListDTO toListDTO(ReceitaPrevista r) {
        return ReceitaPrevistaListDTO.builder()
                .id(r.getId())
                .exercicio(r.getLoa() != null ? r.getLoa().getExercicio() : null)
                .codigoReceita(r.getCodigo())
                .descricao(r.getDescricao())
                .fonteRecursoDescricao(r.getFonteRecurso() != null ? r.getFonteRecurso().getDescricao() : null)
                .valorPrevisto(r.getValorPrevisto())
                .valorArrecadado(r.getValorArrecadado())
                .build();
    }

    private ReceitaPrevistaResponseDTO toResponseDTO(ReceitaPrevista r) {
        return ReceitaPrevistaResponseDTO.builder()
                .id(r.getId())
                .loa(r.getLoa() != null ? LoaListDTO.builder()
                        .id(r.getLoa().getId()).exercicio(r.getLoa().getExercicio())
                        .descricao(r.getLoa().getDescricao()).build() : null)
                .codigoReceita(r.getCodigo())
                .descricao(r.getDescricao())
                .fonteRecurso(r.getFonteRecurso() != null ? FonteRecursoDTO.builder()
                        .id(r.getFonteRecurso().getId()).codigo(r.getFonteRecurso().getCodigo())
                        .descricao(r.getFonteRecurso().getDescricao()).build() : null)
                .valorPrevisto(r.getValorPrevisto())
                .valorArrecadado(r.getValorArrecadado())
                .build();
    }
}
