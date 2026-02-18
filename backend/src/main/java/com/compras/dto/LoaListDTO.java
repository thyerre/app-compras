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
public class LoaListDTO {
    private Long id;
    private Integer exercicio;
    private String descricao;
    private BigDecimal valorTotalReceita;
    private BigDecimal valorTotalDespesa;
    private String status;
}
