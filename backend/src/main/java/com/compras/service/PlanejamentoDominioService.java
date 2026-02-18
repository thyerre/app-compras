package com.compras.service;

import com.compras.dto.*;
import com.compras.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanejamentoDominioService {

    private final FuncaoRepository funcaoRepository;
    private final SubfuncaoRepository subfuncaoRepository;
    private final NaturezaDespesaRepository naturezaDespesaRepository;
    private final FonteRecursoRepository fonteRecursoRepository;
    private final OrgaoRepository orgaoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProgramaRepository programaRepository;
    private final LoaRepository loaRepository;

    @Transactional(readOnly = true)
    public List<FuncaoDTO> findAllFuncoes() {
        return funcaoRepository.findAll().stream()
                .map(f -> FuncaoDTO.builder()
                        .id(f.getId()).codigo(f.getCodigo()).nome(f.getNome())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubfuncaoDTO> findSubfuncoesByFuncao(Integer funcaoId) {
        return subfuncaoRepository.findByFuncaoIdOrderByNomeAsc(funcaoId).stream()
                .map(s -> SubfuncaoDTO.builder()
                        .id(s.getId()).codigo(s.getCodigo()).nome(s.getNome())
                        .funcaoId(s.getFuncao().getId())
                        .funcaoNome(s.getFuncao().getNome())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubfuncaoDTO> findAllSubfuncoes() {
        return subfuncaoRepository.findAll().stream()
                .map(s -> SubfuncaoDTO.builder()
                        .id(s.getId()).codigo(s.getCodigo()).nome(s.getNome())
                        .funcaoId(s.getFuncao() != null ? s.getFuncao().getId() : null)
                        .funcaoNome(s.getFuncao() != null ? s.getFuncao().getNome() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NaturezaDespesaDTO> findAllNaturezasDespesa() {
        return naturezaDespesaRepository.findAll().stream()
                .map(n -> NaturezaDespesaDTO.builder()
                        .id(n.getId()).codigo(n.getCodigo()).descricao(n.getDescricao())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FonteRecursoDTO> findAllFontesRecurso() {
        return fonteRecursoRepository.findAll().stream()
                .map(f -> FonteRecursoDTO.builder()
                        .id(f.getId()).codigo(f.getCodigo()).descricao(f.getDescricao())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrgaoDTO> findAllOrgaosAtivos() {
        return orgaoRepository.findByAtivoTrueOrderByNomeAsc().stream()
                .map(o -> OrgaoDTO.builder()
                        .id(o.getId()).codigo(o.getCodigo()).nome(o.getNome()).ativo(o.getAtivo())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UnidadeDTO> findUnidadesByOrgao(Integer orgaoId) {
        return unidadeRepository.findByOrgaoIdOrderByNomeAsc(orgaoId).stream()
                .map(u -> UnidadeDTO.builder()
                        .id(u.getId()).codigo(u.getCodigo()).nome(u.getNome())
                        .orgaoId(u.getOrgao().getId())
                        .orgaoNome(u.getOrgao().getNome())
                        .ativo(u.getAtivo())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProgramaListDTO> findAllProgramasAtivos() {
        return programaRepository.findByAtivoTrueOrderByNomeAsc().stream()
                .map(p -> ProgramaListDTO.builder()
                        .id(p.getId()).codigo(p.getCodigo()).nome(p.getNome())
                        .exercicioInicio(p.getExercicioInicio())
                        .exercicioFim(p.getExercicioFim())
                        .ativo(p.getAtivo())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoaListDTO> findAllLoasSimples() {
        return loaRepository.findAll().stream()
                .map(l -> LoaListDTO.builder()
                        .id(l.getId())
                        .exercicio(l.getExercicio())
                        .descricao(l.getDescricao())
                        .status(l.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
