package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RestosAPagarRequestDTO {

    @NotNull(message = "O exercício de origem é obrigatório")
    private Integer exercicioOrigem;

    @NotNull(message = "O empenho é obrigatório")
    private Long empenhoId;

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    @NotNull(message = "O valor inscrito é obrigatório")
    private BigDecimal valorInscrito;

    private BigDecimal valorCancelado;
    private BigDecimal valorLiquidado;
    private BigDecimal valorPago;

    @NotNull(message = "A data de inscrição é obrigatória")
    private LocalDate dataInscricao;

    private LocalDate dataCancelamento;
    private LocalDate dataPagamento;
    private String status;
    private String observacoes;
}
