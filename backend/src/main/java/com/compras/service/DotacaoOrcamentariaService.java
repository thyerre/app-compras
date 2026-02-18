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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DotacaoOrcamentariaService {

    private final DotacaoOrcamentariaRepository dotacaoRepository;
    private final LoaRepository loaRepository;
    private final OrgaoRepository orgaoRepository;
    private final UnidadeRepository unidadeRepository;
    private final FuncaoRepository funcaoRepository;
    private final SubfuncaoRepository subfuncaoRepository;
    private final ProgramaRepository programaRepository;
    private final AcaoRepository acaoRepository;
    private final NaturezaDespesaRepository naturezaDespesaRepository;
    private final FonteRecursoRepository fonteRecursoRepository;

    @Transactional(readOnly = true)
    public Page<DotacaoOrcamentariaListDTO> findAll(Integer exercicio, Integer orgaoId,
                                                      Integer programaId, Pageable pageable) {
        Specification<DotacaoOrcamentaria> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("loa").get("exercicio"), exercicio));
            }
            if (orgaoId != null) {
                predicates.add(cb.equal(root.get("orgao").get("id"), orgaoId));
            }
            if (programaId != null) {
                predicates.add(cb.equal(root.get("programa").get("id"), programaId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<DotacaoOrcamentaria> page = dotacaoRepository.findAll(spec, pageable);
        if (page.isEmpty()) return page.map(d -> null);

        List<Long> ids = page.getContent().stream().map(DotacaoOrcamentaria::getId).toList();
        List<DotacaoOrcamentaria> dotacoes = dotacaoRepository.findByIdsWithAssociations(ids);
        Map<Long, DotacaoOrcamentaria> dotacaoMap = dotacoes.stream()
                .collect(Collectors.toMap(DotacaoOrcamentaria::getId, Function.identity()));

        return page.map(d -> toListDTO(dotacaoMap.get(d.getId())));
    }

    @Transactional(readOnly = true)
    public DotacaoOrcamentariaResponseDTO findById(Long id) {
        DotacaoOrcamentaria dotacao = dotacaoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("ME_DOTACAO_NOT_FOUND",
                        "Dotação orçamentária não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(dotacao);
    }

    @Transactional
    public DotacaoOrcamentariaResponseDTO create(DotacaoOrcamentariaRequestDTO dto) {
        DotacaoOrcamentaria dotacao = new DotacaoOrcamentaria();
        updateEntity(dotacao, dto);
        dotacao = dotacaoRepository.save(dotacao);
        return toResponseDTO(dotacaoRepository.findByIdWithAssociations(dotacao.getId()).orElse(dotacao));
    }

    @Transactional
    public DotacaoOrcamentariaResponseDTO update(Long id, DotacaoOrcamentariaRequestDTO dto) {
        DotacaoOrcamentaria dotacao = dotacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_DOTACAO_NOT_FOUND",
                        "Dotação orçamentária não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(dotacao, dto);
        dotacao = dotacaoRepository.save(dotacao);
        return toResponseDTO(dotacaoRepository.findByIdWithAssociations(dotacao.getId()).orElse(dotacao));
    }

    @Transactional
    public void delete(Long id) {
        if (!dotacaoRepository.existsById(id)) {
            throw new BusinessException("ME_DOTACAO_NOT_FOUND",
                    "Dotação orçamentária não encontrada", HttpStatus.NOT_FOUND);
        }
        dotacaoRepository.deleteById(id);
    }

    private void updateEntity(DotacaoOrcamentaria d, DotacaoOrcamentariaRequestDTO dto) {
        d.setLoa(loaRepository.findById(dto.getLoaId())
                .orElseThrow(() -> new BusinessException("ME_LOA_NOT_FOUND", "LOA não encontrada")));
        d.setOrgao(orgaoRepository.findById(dto.getOrgaoId())
                .orElseThrow(() -> new BusinessException("ME_ORGAO_NOT_FOUND", "Órgão não encontrado")));
        d.setUnidade(unidadeRepository.findById(dto.getUnidadeId())
                .orElseThrow(() -> new BusinessException("ME_UNIDADE_NOT_FOUND", "Unidade não encontrada")));
        d.setFuncao(funcaoRepository.findById(dto.getFuncaoId())
                .orElseThrow(() -> new BusinessException("ME_FUNCAO_NOT_FOUND", "Função não encontrada")));
        d.setSubfuncao(subfuncaoRepository.findById(dto.getSubfuncaoId())
                .orElseThrow(() -> new BusinessException("ME_SUBFUNCAO_NOT_FOUND", "Subfunção não encontrada")));
        d.setPrograma(programaRepository.findById(dto.getProgramaId())
                .orElseThrow(() -> new BusinessException("ME_PROGRAMA_NOT_FOUND", "Programa não encontrado")));
        d.setAcao(acaoRepository.findById(dto.getAcaoId())
                .orElseThrow(() -> new BusinessException("ME_ACAO_NOT_FOUND", "Ação não encontrada")));
        d.setNaturezaDespesa(naturezaDespesaRepository.findById(dto.getNaturezaDespesaId())
                .orElseThrow(() -> new BusinessException("ME_NATUREZA_NOT_FOUND", "Natureza de despesa não encontrada")));
        d.setFonteRecurso(fonteRecursoRepository.findById(dto.getFonteRecursoId())
                .orElseThrow(() -> new BusinessException("ME_FONTE_NOT_FOUND", "Fonte de recurso não encontrada")));
        d.setValorInicial(dto.getValorInicial());
        if (dto.getValorSuplementado() != null) d.setValorSuplementado(dto.getValorSuplementado());
        if (dto.getValorAnulado() != null) d.setValorAnulado(dto.getValorAnulado());
        if (dto.getValorEmpenhado() != null) d.setValorEmpenhado(dto.getValorEmpenhado());
        d.setDescricao(dto.getDescricao());
    }

    private DotacaoOrcamentariaListDTO toListDTO(DotacaoOrcamentaria d) {
        return DotacaoOrcamentariaListDTO.builder()
                .id(d.getId())
                .exercicio(d.getLoa() != null ? d.getLoa().getExercicio() : null)
                .orgaoNome(d.getOrgao() != null ? d.getOrgao().getNome() : null)
                .unidadeNome(d.getUnidade() != null ? d.getUnidade().getNome() : null)
                .funcaoNome(d.getFuncao() != null ? d.getFuncao().getNome() : null)
                .programaNome(d.getPrograma() != null ? d.getPrograma().getNome() : null)
                .acaoNome(d.getAcao() != null ? d.getAcao().getNome() : null)
                .naturezaDespesaDescricao(d.getNaturezaDespesa() != null ? d.getNaturezaDespesa().getDescricao() : null)
                .fonteRecursoDescricao(d.getFonteRecurso() != null ? d.getFonteRecurso().getDescricao() : null)
                .valorInicial(d.getValorInicial())
                .saldoDisponivel(d.getSaldoDisponivel())
                .build();
    }

    private DotacaoOrcamentariaResponseDTO toResponseDTO(DotacaoOrcamentaria d) {
        return DotacaoOrcamentariaResponseDTO.builder()
                .id(d.getId())
                .loa(d.getLoa() != null ? LoaListDTO.builder()
                        .id(d.getLoa().getId()).exercicio(d.getLoa().getExercicio())
                        .descricao(d.getLoa().getDescricao()).status(d.getLoa().getStatus())
                        .build() : null)
                .orgao(d.getOrgao() != null ? OrgaoDTO.builder()
                        .id(d.getOrgao().getId()).codigo(d.getOrgao().getCodigo())
                        .nome(d.getOrgao().getNome()).build() : null)
                .unidade(d.getUnidade() != null ? UnidadeDTO.builder()
                        .id(d.getUnidade().getId()).codigo(d.getUnidade().getCodigo())
                        .nome(d.getUnidade().getNome()).build() : null)
                .funcao(d.getFuncao() != null ? FuncaoDTO.builder()
                        .id(d.getFuncao().getId()).codigo(d.getFuncao().getCodigo())
                        .nome(d.getFuncao().getNome()).build() : null)
                .subfuncao(d.getSubfuncao() != null ? SubfuncaoDTO.builder()
                        .id(d.getSubfuncao().getId()).codigo(d.getSubfuncao().getCodigo())
                        .nome(d.getSubfuncao().getNome()).build() : null)
                .programa(d.getPrograma() != null ? ProgramaListDTO.builder()
                        .id(d.getPrograma().getId()).codigo(d.getPrograma().getCodigo())
                        .nome(d.getPrograma().getNome()).build() : null)
                .acao(d.getAcao() != null ? AcaoListDTO.builder()
                        .id(d.getAcao().getId()).codigo(d.getAcao().getCodigo())
                        .nome(d.getAcao().getNome()).build() : null)
                .naturezaDespesa(d.getNaturezaDespesa() != null ? NaturezaDespesaDTO.builder()
                        .id(d.getNaturezaDespesa().getId()).codigo(d.getNaturezaDespesa().getCodigo())
                        .descricao(d.getNaturezaDespesa().getDescricao()).build() : null)
                .fonteRecurso(d.getFonteRecurso() != null ? FonteRecursoDTO.builder()
                        .id(d.getFonteRecurso().getId()).codigo(d.getFonteRecurso().getCodigo())
                        .descricao(d.getFonteRecurso().getDescricao()).build() : null)
                .valorInicial(d.getValorInicial())
                .valorSuplementado(d.getValorSuplementado())
                .valorAnulado(d.getValorAnulado())
                .valorEmpenhado(d.getValorEmpenhado())
                .saldoDisponivel(d.getSaldoDisponivel())
                .descricao(d.getDescricao())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
