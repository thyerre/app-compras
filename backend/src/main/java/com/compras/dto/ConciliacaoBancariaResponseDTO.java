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
public class ConciliacaoBancariaResponseDTO {
    private Long id;
    private ContaBancariaListDTO contaBancaria;
    private Integer mesReferencia;
    private Integer anoReferencia;
    private BigDecimal saldoExtrato;
    private BigDecimal saldoContabil;
    private BigDecimal diferenca;
    private String status;
    private String observacoes;
    private String responsavel;
    private LocalDate dataConciliacao;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
