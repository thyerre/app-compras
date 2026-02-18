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
public class ProcessoCompraService {

    private final ProcessoCompraRepository processoRepository;
    private final ModalidadeLicitacaoRepository modalidadeRepository;
    private final TipoJulgamentoRepository tipoJulgamentoRepository;
    private final StatusProcessoRepository statusProcessoRepository;
    private final OrgaoRepository orgaoRepository;
    private final UnidadeRepository unidadeRepository;
    private final DotacaoOrcamentariaRepository dotacaoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProcessoHistoricoRepository historicoRepository;

    @Transactional(readOnly = true)
    public Page<ProcessoCompraListDTO> findAll(String numeroProcesso, Integer exercicio,
                                                 Integer modalidadeId, Integer statusId,
                                                 Pageable pageable) {
        Specification<ProcessoCompra> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (numeroProcesso != null && !numeroProcesso.isBlank()) {
                predicates.add(cb.like(root.get("numeroProcesso"), "%" + numeroProcesso + "%"));
            }
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("ano"), exercicio));
            }
            if (modalidadeId != null) {
                predicates.add(cb.equal(root.get("modalidade").get("id"), modalidadeId));
            }
            if (statusId != null) {
                predicates.add(cb.equal(root.get("status").get("id"), statusId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ProcessoCompra> page = processoRepository.findAll(spec, pageable);
        if (page.isEmpty()) return page.map(p -> null);

        List<Long> ids = page.getContent().stream().map(ProcessoCompra::getId).toList();
        List<ProcessoCompra> processos = processoRepository.findByIdsWithAssociations(ids);
        Map<Long, ProcessoCompra> processoMap = processos.stream()
                .collect(Collectors.toMap(ProcessoCompra::getId, Function.identity()));

        return page.map(p -> toListDTO(processoMap.get(p.getId())));
    }

    @Transactional(readOnly = true)
    public ProcessoCompraResponseDTO findById(Long id) {
        ProcessoCompra processo = processoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("ME_PROCESSO_NOT_FOUND",
                        "Processo de compra não encontrado", HttpStatus.NOT_FOUND));

        List<ProcessoHistorico> historico = historicoRepository
                .findByProcessoIdOrderByDataRegistroDesc(id);

        ProcessoCompraResponseDTO response = toResponseDTO(processo);
        response.setHistorico(historico.stream()
                .map(h -> ProcessoHistoricoDTO.builder()
                        .id(h.getId())
                        .statusAnterior(h.getStatusAnterior() != null ? h.getStatusAnterior().getNome() : null)
                        .statusNovo(h.getStatusNovo().getNome())
                        .descricao(h.getDescricao())
                        .usuarioNome(h.getUsuarioNome())
                        .dataRegistro(h.getDataRegistro())
                        .build())
                .collect(Collectors.toList()));

        return response;
    }

    @Transactional
    public ProcessoCompraResponseDTO create(ProcessoCompraRequestDTO dto) {
        validateNumeroProcessoUnique(dto.getNumeroProcesso(), null);
        ProcessoCompra processo = new ProcessoCompra();
        updateEntity(processo, dto);

        StatusProcesso statusInicial = statusProcessoRepository.findByNome("Rascunho")
                .orElse(null);
        if (dto.getStatusId() != null) {
            statusInicial = statusProcessoRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new BusinessException("ME_STATUS_NOT_FOUND", "Status não encontrado"));
        }
        processo.setStatus(statusInicial);

        processo = processoRepository.save(processo);
        syncItens(processo, dto.getItens());
        processo = processoRepository.save(processo);

        // Registrar histórico
        registrarHistorico(processo, null, processo.getStatus(), "Processo criado");

        return findById(processo.getId());
    }

    @Transactional
    public ProcessoCompraResponseDTO update(Long id, ProcessoCompraRequestDTO dto) {
        ProcessoCompra processo = processoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("ME_PROCESSO_NOT_FOUND",
                        "Processo de compra não encontrado", HttpStatus.NOT_FOUND));

        validateNumeroProcessoUnique(dto.getNumeroProcesso(), id);
        StatusProcesso statusAnterior = processo.getStatus();
        updateEntity(processo, dto);

        if (dto.getStatusId() != null) {
            StatusProcesso novoStatus = statusProcessoRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new BusinessException("ME_STATUS_NOT_FOUND", "Status não encontrado"));
            processo.setStatus(novoStatus);

            if (statusAnterior == null || !statusAnterior.getId().equals(novoStatus.getId())) {
                registrarHistorico(processo, statusAnterior, novoStatus, "Status alterado");
            }
        }

        syncItens(processo, dto.getItens());
        processoRepository.save(processo);
        return findById(processo.getId());
    }

    @Transactional
    public void delete(Long id) {
        if (!processoRepository.existsById(id)) {
            throw new BusinessException("ME_PROCESSO_NOT_FOUND",
                    "Processo de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        processoRepository.deleteById(id);
    }

    // ───── Validations ─────

    private void validateNumeroProcessoUnique(String numeroProcesso, Long excludeId) {
        boolean exists = excludeId == null
                ? processoRepository.existsByNumeroProcesso(numeroProcesso)
                : processoRepository.existsByNumeroProcessoAndIdNot(numeroProcesso, excludeId);
        if (exists) {
            throw new BusinessException("ME_PROCESSO_NUMERO_DUPLICATE",
                    "Já existe um processo com este número");
        }
    }

    // ───── Itens sync ─────

    private void syncItens(ProcessoCompra processo, List<ProcessoItemDTO> dtos) {
        if (dtos == null) return;
        processo.getItens().clear();
        for (ProcessoItemDTO dto : dtos) {
            ProcessoItem item = ProcessoItem.builder()
                    .processo(processo)
                    .numeroItem(dto.getNumeroItem())
                    .descricao(dto.getDescricao())
                    .unidadeMedida(dto.getUnidadeMedida())
                    .quantidade(dto.getQuantidade())
                    .valorUnitarioEstimado(dto.getValorUnitarioEstimado())
                    .valorTotalEstimado(dto.getValorTotalEstimado())
                    .valorUnitarioHomologado(dto.getValorUnitarioHomologado())
                    .valorTotalHomologado(dto.getValorTotalHomologado())
                    .marca(dto.getMarca())
                    .situacao(dto.getSituacao() != null ? dto.getSituacao() : "PENDENTE")
                    .build();

            if (dto.getFornecedorVencedorId() != null) {
                Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorVencedorId())
                        .orElseThrow(() -> new BusinessException("ME_FORNECEDOR_NOT_FOUND",
                                "Fornecedor não encontrado"));
                item.setFornecedorVencedor(fornecedor);
            }

            processo.getItens().add(item);
        }
    }

    // ───── Histórico ─────

    private void registrarHistorico(ProcessoCompra processo, StatusProcesso anterior,
                                     StatusProcesso novo, String descricao) {
        ProcessoHistorico historico = ProcessoHistorico.builder()
                .processo(processo)
                .statusAnterior(anterior)
                .statusNovo(novo)
                .descricao(descricao)
                .build();
        historicoRepository.save(historico);
    }

    // ───── Mapping ─────

    private void updateEntity(ProcessoCompra p, ProcessoCompraRequestDTO dto) {
        p.setNumeroProcesso(dto.getNumeroProcesso());
        p.setAno(dto.getExercicio());

        p.setModalidade(modalidadeRepository.findById(dto.getModalidadeId())
                .orElseThrow(() -> new BusinessException("ME_MODALIDADE_NOT_FOUND", "Modalidade não encontrada")));
        p.setTipoJulgamento(tipoJulgamentoRepository.findById(dto.getTipoJulgamentoId())
                .orElseThrow(() -> new BusinessException("ME_TIPO_JULGAMENTO_NOT_FOUND",
                        "Tipo de julgamento não encontrado")));

        p.setObjeto(dto.getObjeto());
        p.setJustificativa(dto.getJustificativa());
        p.setValorEstimado(dto.getValorEstimado());
        p.setValorHomologado(dto.getValorHomologado());

        if (dto.getOrgaoId() != null) {
            p.setOrgao(orgaoRepository.findById(dto.getOrgaoId())
                    .orElseThrow(() -> new BusinessException("ME_ORGAO_NOT_FOUND", "Órgão não encontrado")));
        }
        if (dto.getUnidadeId() != null) {
            p.setUnidade(unidadeRepository.findById(dto.getUnidadeId())
                    .orElseThrow(() -> new BusinessException("ME_UNIDADE_NOT_FOUND", "Unidade não encontrada")));
        }
        if (dto.getDotacaoId() != null) {
            p.setDotacao(dotacaoRepository.findById(dto.getDotacaoId())
                    .orElseThrow(() -> new BusinessException("ME_DOTACAO_NOT_FOUND",
                            "Dotação orçamentária não encontrada")));
        }

        p.setDataAbertura(dto.getDataAbertura());
        p.setDataEncerramento(dto.getDataEncerramento());
        p.setDataHomologacao(dto.getDataHomologacao());
        p.setDataAdjudicacao(dto.getDataAdjudicacao());
        p.setNumeroEdital(dto.getNumeroEdital());
        p.setObservacoes(dto.getObservacoes());
    }

    private ProcessoCompraListDTO toListDTO(ProcessoCompra p) {
        return ProcessoCompraListDTO.builder()
                .id(p.getId())
                .numeroProcesso(p.getNumeroProcesso())
                .exercicio(p.getAno())
                .modalidade(p.getModalidade() != null ? p.getModalidade().getNome() : null)
                .status(p.getStatus() != null ? p.getStatus().getNome() : null)
                .statusCor(p.getStatus() != null ? p.getStatus().getCor() : null)
                .objeto(p.getObjeto() != null && p.getObjeto().length() > 100
                        ? p.getObjeto().substring(0, 100) + "..." : p.getObjeto())
                .valorEstimado(p.getValorEstimado())
                .valorHomologado(p.getValorHomologado())
                .dataAbertura(p.getDataAbertura())
                .orgaoNome(p.getOrgao() != null ? p.getOrgao().getNome() : null)
                .build();
    }

    private ProcessoCompraResponseDTO toResponseDTO(ProcessoCompra p) {
        return ProcessoCompraResponseDTO.builder()
                .id(p.getId())
                .numeroProcesso(p.getNumeroProcesso())
                .exercicio(p.getAno())
                .modalidade(p.getModalidade() != null ? ModalidadeLicitacaoDTO.builder()
                        .id(p.getModalidade().getId()).nome(p.getModalidade().getNome())
                        .sigla(null).build() : null)
                .tipoJulgamento(p.getTipoJulgamento() != null ? TipoJulgamentoDTO.builder()
                        .id(p.getTipoJulgamento().getId()).descricao(p.getTipoJulgamento().getDescricao())
                        .build() : null)
                .status(p.getStatus() != null ? StatusProcessoDTO.builder()
                        .id(p.getStatus().getId()).nome(p.getStatus().getNome())
                        .cor(p.getStatus().getCor()).build() : null)
                .objeto(p.getObjeto())
                .justificativa(p.getJustificativa())
                .valorEstimado(p.getValorEstimado())
                .valorHomologado(p.getValorHomologado())
                .orgao(p.getOrgao() != null ? OrgaoDTO.builder()
                        .id(p.getOrgao().getId()).codigo(p.getOrgao().getCodigo())
                        .nome(p.getOrgao().getNome()).build() : null)
                .unidade(p.getUnidade() != null ? UnidadeDTO.builder()
                        .id(p.getUnidade().getId()).codigo(p.getUnidade().getCodigo())
                        .nome(p.getUnidade().getNome()).build() : null)
                .dataAbertura(p.getDataAbertura())
                .dataEncerramento(p.getDataEncerramento())
                .dataHomologacao(p.getDataHomologacao())
                .dataAdjudicacao(p.getDataAdjudicacao())
                .numeroEdital(p.getNumeroEdital())
                .numeroContrato(null)
                .observacoes(p.getObservacoes())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .itens(p.getItens() != null ? p.getItens().stream()
                        .map(i -> ProcessoItemDTO.builder()
                                .id(i.getId())
                                .numeroItem(i.getNumeroItem())
                                .descricao(i.getDescricao())
                                .unidadeMedida(i.getUnidadeMedida())
                                .quantidade(i.getQuantidade())
                                .valorUnitarioEstimado(i.getValorUnitarioEstimado())
                                .valorTotalEstimado(i.getValorTotalEstimado())
                                .valorUnitarioHomologado(i.getValorUnitarioHomologado())
                                .valorTotalHomologado(i.getValorTotalHomologado())
                                .fornecedorVencedorId(i.getFornecedorVencedor() != null ?
                                        i.getFornecedorVencedor().getId() : null)
                                .fornecedorVencedorNome(i.getFornecedorVencedor() != null ?
                                        i.getFornecedorVencedor().getRazaoSocial() : null)
                                .marca(i.getMarca())
                                .modelo(null)
                                .situacao(i.getSituacao())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .participantes(p.getParticipantes() != null ? p.getParticipantes().stream()
                        .map(pp -> ProcessoParticipanteDTO.builder()
                                .id(pp.getId())
                                .fornecedorId(pp.getFornecedor() != null ? pp.getFornecedor().getId() : null)
                                .fornecedorRazaoSocial(pp.getFornecedor() != null ?
                                        pp.getFornecedor().getRazaoSocial() : null)
                                .fornecedorCnpjCpf(pp.getFornecedor() != null ?
                                        pp.getFornecedor().getCnpjCpf() : null)
                                .situacao(pp.getSituacao())
                                .motivoInabilitacao(pp.getMotivoInabilitacao())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .documentos(p.getDocumentos() != null ? p.getDocumentos().stream()
                        .map(d -> ProcessoDocumentoDTO.builder()
                                .id(d.getId())
                                .nome(d.getNome())
                                .tipo(d.getTipo())
                                .arquivoNome(d.getArquivoNome())
                                .arquivoPath(d.getArquivoPath())
                                .tamanhoBytes(d.getTamanhoBytes())
                                .observacao(d.getObservacao())
                                .createdAt(d.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
