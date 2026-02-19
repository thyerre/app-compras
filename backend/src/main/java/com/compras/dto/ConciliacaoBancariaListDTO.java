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
public class ConciliacaoBancariaListDTO {
    private Long id;
    private String contaBancariaDescricao;
    private Integer mesReferencia;
    private Integer anoReferencia;
    private BigDecimal saldoExtrato;
    private BigDecimal saldoContabil;
    private BigDecimal diferenca;
    private String status;
}
