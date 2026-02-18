package com.compras.service;

import com.compras.dto.UnidadeDTO;
import com.compras.dto.UnidadeRequestDTO;
import com.compras.entity.Orgao;
import com.compras.entity.Unidade;
import com.compras.exception.BusinessException;
import com.compras.repository.OrgaoRepository;
import com.compras.repository.UnidadeRepository;
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
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final OrgaoRepository orgaoRepository;

    @Transactional(readOnly = true)
    public Page<UnidadeDTO> findAll(String nome, Integer orgaoId, Boolean ativo, Pageable pageable) {
        Page<Unidade> page = unidadeRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (orgaoId != null) {
                predicates.add(cb.equal(root.get("orgao").get("id"), orgaoId));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<UnidadeDTO> dtos = page.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public UnidadeDTO findById(Integer id) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_UNIDADE_NOT_FOUND",
                        "Unidade não encontrada", HttpStatus.NOT_FOUND));
        return toDTO(unidade);
    }

    @Transactional
    public UnidadeDTO create(UnidadeRequestDTO dto) {
        validateCodigoUnique(dto.getCodigo(), dto.getOrgaoId(), null);
        Orgao orgao = orgaoRepository.findById(dto.getOrgaoId())
                .orElseThrow(() -> new BusinessException("ME_ORGAO_NOT_FOUND", "Órgão não encontrado"));
        Unidade unidade = Unidade.builder()
                .codigo(dto.getCodigo())
                .nome(dto.getNome())
                .orgao(orgao)
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();
        return toDTO(unidadeRepository.save(unidade));
    }

    @Transactional
    public UnidadeDTO update(Integer id, UnidadeRequestDTO dto) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_UNIDADE_NOT_FOUND",
                        "Unidade não encontrada", HttpStatus.NOT_FOUND));
        validateCodigoUnique(dto.getCodigo(), dto.getOrgaoId(), id);
        Orgao orgao = orgaoRepository.findById(dto.getOrgaoId())
                .orElseThrow(() -> new BusinessException("ME_ORGAO_NOT_FOUND", "Órgão não encontrado"));
        unidade.setCodigo(dto.getCodigo());
        unidade.setNome(dto.getNome());
        unidade.setOrgao(orgao);
        unidade.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : unidade.getAtivo());
        return toDTO(unidadeRepository.save(unidade));
    }

    @Transactional
    public void delete(Integer id) {
        if (!unidadeRepository.existsById(id)) {
            throw new BusinessException("ME_UNIDADE_NOT_FOUND",
                    "Unidade não encontrada", HttpStatus.NOT_FOUND);
        }
        unidadeRepository.deleteById(id);
    }

    private void validateCodigoUnique(String codigo, Integer orgaoId, Integer excludeId) {
        boolean exists = excludeId == null
                ? unidadeRepository.existsByCodigoAndOrgaoId(codigo, orgaoId)
                : unidadeRepository.existsByCodigoAndOrgaoIdAndIdNot(codigo, orgaoId, excludeId);
        if (exists) {
            throw new BusinessException("ME_UNIDADE_CODIGO_DUPLICATE",
                    "Já existe uma unidade com este código neste órgão");
        }
    }

    private UnidadeDTO toDTO(Unidade u) {
        return UnidadeDTO.builder()
                .id(u.getId()).codigo(u.getCodigo()).nome(u.getNome())
                .orgaoId(u.getOrgao() != null ? u.getOrgao().getId() : null)
                .orgaoNome(u.getOrgao() != null ? u.getOrgao().getNome() : null)
                .ativo(u.getAtivo())
                .build();
    }
}
