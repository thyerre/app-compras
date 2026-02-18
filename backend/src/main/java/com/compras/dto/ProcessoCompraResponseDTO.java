package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoCompraResponseDTO {
    private Long id;
    private String numeroProcesso;
    private Integer exercicio;
    private ModalidadeLicitacaoDTO modalidade;
    private TipoJulgamentoDTO tipoJulgamento;
    private StatusProcessoDTO status;
    private String objeto;
    private String justificativa;
    private BigDecimal valorEstimado;
    private BigDecimal valorHomologado;
    private OrgaoDTO orgao;
    private UnidadeDTO unidade;
    private DotacaoOrcamentariaListDTO dotacao;
    private LocalDate dataAbertura;
    private LocalDate dataEncerramento;
    private LocalDate dataHomologacao;
    private LocalDate dataAdjudicacao;
    private String numeroEdital;
    private String numeroContrato;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProcessoItemDTO> itens;
    private List<ProcessoParticipanteDTO> participantes;
    private List<ProcessoDocumentoDTO> documentos;
    private List<ProcessoHistoricoDTO> historico;
}
