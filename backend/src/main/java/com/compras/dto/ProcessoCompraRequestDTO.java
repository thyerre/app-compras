package com.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoCompraRequestDTO {

    @NotBlank(message = "O número do processo é obrigatório")
    @Size(max = 30, message = "O número do processo deve ter no máximo 30 caracteres")
    private String numeroProcesso;

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "A modalidade é obrigatória")
    private Integer modalidadeId;

    @NotNull(message = "O tipo de julgamento é obrigatório")
    private Integer tipoJulgamentoId;

    @NotBlank(message = "O objeto é obrigatório")
    private String objeto;

    private String justificativa;

    private BigDecimal valorEstimado;
    private BigDecimal valorHomologado;

    private Integer orgaoId;
    private Integer unidadeId;
    private Long dotacaoId;

    private LocalDate dataAbertura;
    private LocalDate dataEncerramento;
    private LocalDate dataHomologacao;
    private LocalDate dataAdjudicacao;

    @Size(max = 200, message = "O número do edital deve ter no máximo 200 caracteres")
    private String numeroEdital;

    @Size(max = 200, message = "O número do contrato deve ter no máximo 200 caracteres")
    private String numeroContrato;

    private Integer statusId;
    private String observacoes;

    @Valid
    private List<ProcessoItemDTO> itens;
}
