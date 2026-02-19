package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestosAPagarResponseDTO {
    private Long id;
    private Integer exercicioOrigem;
    private EmpenhoListDTO empenho;
    private String tipo;
    private BigDecimal valorInscrito;
    private BigDecimal valorCancelado;
    private BigDecimal valorLiquidado;
    private BigDecimal valorPago;
    private LocalDate dataInscricao;
    private LocalDate dataCancelamento;
    private LocalDate dataPagamento;
    private String status;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
