package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.Acao;
import com.compras.entity.Funcao;
import com.compras.entity.Subfuncao;
import com.compras.exception.BusinessException;
import com.compras.repository.AcaoRepository;
import com.compras.repository.FuncaoRepository;
import com.compras.repository.SubfuncaoRepository;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcaoService {

    private final AcaoRepository acaoRepository;
    private final FuncaoRepository funcaoRepository;
    private final SubfuncaoRepository subfuncaoRepository;

    @Transactional(readOnly = true)
    public Page<AcaoListDTO> findAll(String nome, String tipo, Integer funcaoId, Boolean ativo, Pageable pageable) {
        Specification<Acao> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (funcaoId != null) {
                predicates.add(cb.equal(root.get("funcao").get("id"), funcaoId));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Acao> page = acaoRepository.findAll(spec, pageable);
        if (page.isEmpty()) return page.map(a -> null);

        List<Integer> ids = page.getContent().stream().map(Acao::getId).toList();
        List<Acao> acoes = acaoRepository.findByIdsWithAssociations(ids);
        Map<Integer, Acao> acaoMap = acoes.stream()
                .collect(Collectors.toMap(Acao::getId, Function.identity()));

        return page.map(a -> toListDTO(acaoMap.get(a.getId())));
    }

    @Transactional(readOnly = true)
    public AcaoResponseDTO findById(Integer id) {
        Acao acao = acaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_ACAO_NOT_FOUND",
                        "Ação não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(acao);
    }

    @Transactional
    public AcaoResponseDTO create(AcaoRequestDTO dto) {
        validateCodigoUnique(dto.getCodigo(), null);
        Acao acao = new Acao();
        updateEntity(acao, dto);
        return toResponseDTO(acaoRepository.save(acao));
    }

    @Transactional
    public AcaoResponseDTO update(Integer id, AcaoRequestDTO dto) {
        Acao acao = acaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_ACAO_NOT_FOUND",
                        "Ação não encontrada", HttpStatus.NOT_FOUND));
        validateCodigoUnique(dto.getCodigo(), id);
        updateEntity(acao, dto);
        return toResponseDTO(acaoRepository.save(acao));
    }

    @Transactional
    public void delete(Integer id) {
        if (!acaoRepository.existsById(id)) {
            throw new BusinessException("ME_ACAO_NOT_FOUND",
                    "Ação não encontrada", HttpStatus.NOT_FOUND);
        }
        acaoRepository.deleteById(id);
    }

    private void validateCodigoUnique(String codigo, Integer excludeId) {
        boolean exists = excludeId == null
                ? acaoRepository.existsByCodigo(codigo)
                : acaoRepository.existsByCodigoAndIdNot(codigo, excludeId);
        if (exists) {
            throw new BusinessException("ME_ACAO_CODIGO_DUPLICATE",
                    "Já existe uma ação com este código");
        }
    }

    private void updateEntity(Acao acao, AcaoRequestDTO dto) {
        acao.setCodigo(dto.getCodigo());
        acao.setNome(dto.getNome());
        acao.setTipo(dto.getTipo());
        acao.setDescricao(dto.getDescricao());
        acao.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);

        Funcao funcao = funcaoRepository.findById(dto.getFuncaoId())
                .orElseThrow(() -> new BusinessException("ME_FUNCAO_NOT_FOUND", "Função não encontrada"));
        acao.setFuncao(funcao);

        Subfuncao subfuncao = subfuncaoRepository.findById(dto.getSubfuncaoId())
                .orElseThrow(() -> new BusinessException("ME_SUBFUNCAO_NOT_FOUND", "Subfunção não encontrada"));
        acao.setSubfuncao(subfuncao);
    }

    private AcaoListDTO toListDTO(Acao a) {
        return AcaoListDTO.builder()
                .id(a.getId()).codigo(a.getCodigo()).nome(a.getNome()).tipo(a.getTipo())
                .funcaoNome(a.getFuncao() != null ? a.getFuncao().getNome() : null)
                .subfuncaoNome(a.getSubfuncao() != null ? a.getSubfuncao().getNome() : null)
                .ativo(a.getAtivo())
                .build();
    }

    private AcaoResponseDTO toResponseDTO(Acao a) {
        return AcaoResponseDTO.builder()
                .id(a.getId()).codigo(a.getCodigo()).nome(a.getNome()).tipo(a.getTipo())
                .descricao(a.getDescricao())
                .funcao(a.getFuncao() != null ? FuncaoDTO.builder()
                        .id(a.getFuncao().getId()).codigo(a.getFuncao().getCodigo())
                        .nome(a.getFuncao().getNome()).build() : null)
                .subfuncao(a.getSubfuncao() != null ? SubfuncaoDTO.builder()
                        .id(a.getSubfuncao().getId()).codigo(a.getSubfuncao().getCodigo())
                        .nome(a.getSubfuncao().getNome())
                        .funcaoId(a.getSubfuncao().getFuncao() != null ? a.getSubfuncao().getFuncao().getId() : null)
                        .build() : null)
                .ativo(a.getAtivo())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
