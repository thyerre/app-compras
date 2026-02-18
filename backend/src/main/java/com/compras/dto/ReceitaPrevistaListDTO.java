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
public class ReceitaPrevistaListDTO {
    private Long id;
    private Integer exercicio;
    private String codigoReceita;
    private String descricao;
    private String fonteRecursoDescricao;
    private BigDecimal valorPrevisto;
    private BigDecimal valorArrecadado;
}
