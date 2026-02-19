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
public class BemPatrimonialService {

    private final BemPatrimonialRepository bemPatrimonialRepository;
    private final OrgaoRepository orgaoRepository;
    private final UnidadeRepository unidadeRepository;
    private final FornecedorRepository fornecedorRepository;
    private final EmpenhoRepository empenhoRepository;
    private final PlanoContasRepository planoContasRepository;

    @Transactional(readOnly = true)
    public Page<BemPatrimonialListDTO> findAll(String descricao, String tipo,
                                                String situacao, Pageable pageable) {
        Page<BemPatrimonial> page = bemPatrimonialRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (descricao != null && !descricao.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (situacao != null && !situacao.isBlank()) {
                predicates.add(cb.equal(root.get("situacao"), situacao));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<BemPatrimonialListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public BemPatrimonialResponseDTO findById(Long id) {
        BemPatrimonial bem = bemPatrimonialRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_BEM_NOT_FOUND",
                        "Bem patrimonial não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(bem);
    }

    @Transactional
    public BemPatrimonialResponseDTO create(BemPatrimonialRequestDTO dto) {
        validateNumeroPatrimonioUnique(dto.getNumeroPatrimonio(), null);
        BemPatrimonial bem = new BemPatrimonial();
        updateEntity(bem, dto);
        return toResponseDTO(bemPatrimonialRepository.save(bem));
    }

    @Transactional
    public BemPatrimonialResponseDTO update(Long id, BemPatrimonialRequestDTO dto) {
        BemPatrimonial bem = bemPatrimonialRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_BEM_NOT_FOUND",
                        "Bem patrimonial não encontrado", HttpStatus.NOT_FOUND));
        validateNumeroPatrimonioUnique(dto.getNumeroPatrimonio(), id);
        updateEntity(bem, dto);
        return toResponseDTO(bemPatrimonialRepository.save(bem));
    }

    @Transactional
    public void delete(Long id) {
        if (!bemPatrimonialRepository.existsById(id)) {
            throw new BusinessException("MC_BEM_NOT_FOUND",
                    "Bem patrimonial não encontrado", HttpStatus.NOT_FOUND);
        }
        bemPatrimonialRepository.deleteById(id);
    }

    private void validateNumeroPatrimonioUnique(String numeroPatrimonio, Long excludeId) {
        boolean exists = excludeId == null
                ? bemPatrimonialRepository.existsByNumeroPatrimonio(numeroPatrimonio)
                : bemPatrimonialRepository.existsByNumeroPatrimonioAndIdNot(numeroPatrimonio, excludeId);
        if (exists) {
            throw new BusinessException("MC_BEM_NUMERO_DUPLICATE",
                    "Já existe um bem patrimonial com este número de patrimônio");
        }
    }

    private void updateEntity(BemPatrimonial b, BemPatrimonialRequestDTO dto) {
        b.setOrgao(orgaoRepository.findById(dto.getOrgaoId())
                .orElseThrow(() -> new BusinessException("MC_BEM_ORGAO_NOT_FOUND", "Órgão não encontrado")));
        if (dto.getUnidadeId() != null) {
            b.setUnidade(unidadeRepository.findById(dto.getUnidadeId())
                    .orElseThrow(() -> new BusinessException("MC_BEM_UNIDADE_NOT_FOUND", "Unidade não encontrada")));
        }
        if (dto.getFornecedorId() != null) {
            b.setFornecedor(fornecedorRepository.findById(dto.getFornecedorId())
                    .orElseThrow(() -> new BusinessException("MC_BEM_FORNECEDOR_NOT_FOUND", "Fornecedor não encontrado")));
        }
        if (dto.getEmpenhoId() != null) {
            b.setEmpenho(empenhoRepository.findById(dto.getEmpenhoId())
                    .orElseThrow(() -> new BusinessException("MC_BEM_EMPENHO_NOT_FOUND", "Empenho não encontrado")));
        }
        if (dto.getPlanoContaId() != null) {
            b.setPlanoConta(planoContasRepository.findById(dto.getPlanoContaId())
                    .orElseThrow(() -> new BusinessException("MC_BEM_PLANO_CONTA_NOT_FOUND",
                            "Plano de contas não encontrado")));
        }
        b.setNumeroPatrimonio(dto.getNumeroPatrimonio());
        b.setDescricao(dto.getDescricao());
        b.setTipo(dto.getTipo());
        b.setCategoria(dto.getCategoria());
        b.setDataAquisicao(dto.getDataAquisicao());
        b.setValorAquisicao(dto.getValorAquisicao());
        b.setValorAtual(dto.getValorAtual());
        b.setVidaUtilMeses(dto.getVidaUtilMeses());
        b.setValorResidual(dto.getValorResidual());
        b.setDepreciacaoAcumulada(dto.getDepreciacaoAcumulada());
        b.setLocalizacao(dto.getLocalizacao());
        b.setResponsavel(dto.getResponsavel());
        b.setEstadoConservacao(dto.getEstadoConservacao() != null ? dto.getEstadoConservacao() : "BOM");
        b.setSituacao(dto.getSituacao() != null ? dto.getSituacao() : "ATIVO");
        b.setNotaFiscal(dto.getNotaFiscal());
        b.setObservacoes(dto.getObservacoes());
    }

    private BemPatrimonialListDTO toListDTO(BemPatrimonial b) {
        return BemPatrimonialListDTO.builder()
                .id(b.getId())
                .numeroPatrimonio(b.getNumeroPatrimonio())
                .descricao(b.getDescricao())
                .tipo(b.getTipo())
                .orgaoNome(b.getOrgao() != null ? b.getOrgao().getNome() : null)
                .dataAquisicao(b.getDataAquisicao())
                .valorAtual(b.getValorAtual())
                .estadoConservacao(b.getEstadoConservacao())
                .situacao(b.getSituacao())
                .build();
    }

    private BemPatrimonialResponseDTO toResponseDTO(BemPatrimonial b) {
        return BemPatrimonialResponseDTO.builder()
                .id(b.getId())
                .numeroPatrimonio(b.getNumeroPatrimonio())
                .descricao(b.getDescricao())
                .tipo(b.getTipo())
                .categoria(b.getCategoria())
                .orgaoNome(b.getOrgao() != null ? b.getOrgao().getNome() : null)
                .unidadeNome(b.getUnidade() != null ? b.getUnidade().getNome() : null)
                .dataAquisicao(b.getDataAquisicao())
                .valorAquisicao(b.getValorAquisicao())
                .valorAtual(b.getValorAtual())
                .vidaUtilMeses(b.getVidaUtilMeses())
                .valorResidual(b.getValorResidual())
                .depreciacaoAcumulada(b.getDepreciacaoAcumulada())
                .localizacao(b.getLocalizacao())
                .responsavel(b.getResponsavel())
                .estadoConservacao(b.getEstadoConservacao())
                .situacao(b.getSituacao())
                .notaFiscal(b.getNotaFiscal())
                .fornecedor(b.getFornecedor() != null ? FornecedorListDTO.builder()
                        .id(b.getFornecedor().getId())
                        .razaoSocial(b.getFornecedor().getRazaoSocial())
                        .cnpjCpf(b.getFornecedor().getCnpjCpf())
                        .build() : null)
                .empenho(b.getEmpenho() != null ? EmpenhoListDTO.builder()
                        .id(b.getEmpenho().getId())
                        .numeroEmpenho(b.getEmpenho().getNumeroEmpenho())
                        .exercicio(b.getEmpenho().getExercicio())
                        .valor(b.getEmpenho().getValor())
                        .status(b.getEmpenho().getStatus())
                        .build() : null)
                .planoConta(b.getPlanoConta() != null ? PlanoContasListDTO.builder()
                        .id(b.getPlanoConta().getId())
                        .codigo(b.getPlanoConta().getCodigo())
                        .descricao(b.getPlanoConta().getDescricao())
                        .build() : null)
                .observacoes(b.getObservacoes())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build();
    }
}
