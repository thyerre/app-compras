package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpenhoRequestDTO {

    @NotBlank(message = "O número do empenho é obrigatório")
    @Size(max = 30, message = "O número deve ter no máximo 30 caracteres")
    private String numeroEmpenho;

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "O processo é obrigatório")
    private Long processoId;

    @NotNull(message = "O fornecedor é obrigatório")
    private Long fornecedorId;

    @NotNull(message = "A dotação é obrigatória")
    private Long dotacaoId;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;

    private BigDecimal valorLiquidado;
    private BigDecimal valorPago;

    @NotNull(message = "A data do empenho é obrigatória")
    private LocalDate dataEmpenho;

    private LocalDate dataLiquidacao;
    private LocalDate dataPagamento;

    @Size(max = 20)
    private String tipoEmpenho;

    private String status;
    private String descricao;
    private String observacao;
}
