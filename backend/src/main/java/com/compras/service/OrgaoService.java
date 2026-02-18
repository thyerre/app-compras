package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.Orgao;
import com.compras.exception.BusinessException;
import com.compras.repository.OrgaoRepository;
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
public class OrgaoService {

    private final OrgaoRepository orgaoRepository;

    @Transactional(readOnly = true)
    public Page<OrgaoDTO> findAll(String nome, Boolean ativo, Pageable pageable) {
        Page<Orgao> page = orgaoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<OrgaoDTO> dtos = page.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public OrgaoDTO findById(Integer id) {
        Orgao orgao = orgaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_ORGAO_NOT_FOUND",
                        "Órgão não encontrado", HttpStatus.NOT_FOUND));
        return toDTO(orgao);
    }

    @Transactional
    public OrgaoDTO create(OrgaoRequestDTO dto) {
        validateCodigoUnique(dto.getCodigo(), null);
        Orgao orgao = Orgao.builder()
                .codigo(dto.getCodigo())
                .nome(dto.getNome())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();
        return toDTO(orgaoRepository.save(orgao));
    }

    @Transactional
    public OrgaoDTO update(Integer id, OrgaoRequestDTO dto) {
        Orgao orgao = orgaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_ORGAO_NOT_FOUND",
                        "Órgão não encontrado", HttpStatus.NOT_FOUND));
        validateCodigoUnique(dto.getCodigo(), id);
        orgao.setCodigo(dto.getCodigo());
        orgao.setNome(dto.getNome());
        orgao.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : orgao.getAtivo());
        return toDTO(orgaoRepository.save(orgao));
    }

    @Transactional
    public void delete(Integer id) {
        if (!orgaoRepository.existsById(id)) {
            throw new BusinessException("ME_ORGAO_NOT_FOUND",
                    "Órgão não encontrado", HttpStatus.NOT_FOUND);
        }
        orgaoRepository.deleteById(id);
    }

    private void validateCodigoUnique(String codigo, Integer excludeId) {
        boolean exists = excludeId == null
                ? orgaoRepository.existsByCodigo(codigo)
                : orgaoRepository.existsByCodigoAndIdNot(codigo, excludeId);
        if (exists) {
            throw new BusinessException("ME_ORGAO_CODIGO_DUPLICATE",
                    "Já existe um órgão com este código");
        }
    }

    private OrgaoDTO toDTO(Orgao o) {
        return OrgaoDTO.builder()
                .id(o.getId()).codigo(o.getCodigo()).nome(o.getNome()).ativo(o.getAtivo())
                .build();
    }
}
