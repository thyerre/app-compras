package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceteListDTO {
    private Long id;
    private Integer exercicio;
    private Integer mes;
    private String planoContaCodigo;
    private String planoContaDescricao;
    private BigDecimal saldoAnterior;
    private BigDecimal totalDebitos;
    private BigDecimal totalCreditos;
    private BigDecimal saldoAtual;
}
