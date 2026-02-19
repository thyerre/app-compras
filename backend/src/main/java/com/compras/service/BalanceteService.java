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
public class BalanceteService {

    private final BalanceteRepository balanceteRepository;
    private final PlanoContasRepository planoContasRepository;

    @Transactional(readOnly = true)
    public Page<BalanceteListDTO> findAll(Integer exercicio, Integer mes, Pageable pageable) {
        Page<Balancete> page = balanceteRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (mes != null) {
                predicates.add(cb.equal(root.get("mes"), mes));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<BalanceteListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    private BalanceteListDTO toListDTO(Balancete b) {
        return BalanceteListDTO.builder()
                .id(b.getId())
                .exercicio(b.getExercicio())
                .mes(b.getMes())
                .planoContaCodigo(b.getPlanoConta() != null ? b.getPlanoConta().getCodigo() : null)
                .planoContaDescricao(b.getPlanoConta() != null ? b.getPlanoConta().getDescricao() : null)
                .saldoAnterior(b.getSaldoAnterior())
                .totalDebitos(b.getTotalDebitos())
                .totalCreditos(b.getTotalCreditos())
                .saldoAtual(b.getSaldoAtual())
                .build();
    }
}
