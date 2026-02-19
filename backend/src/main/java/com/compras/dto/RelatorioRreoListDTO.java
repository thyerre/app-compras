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
public class RelatorioRreoListDTO {
    private Long id;
    private Integer exercicio;
    private Integer bimestre;
    private BigDecimal receitaRealizada;
    private BigDecimal despesaEmpenhada;
    private BigDecimal receitaCorrenteLiquida;
    private String status;
}
