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
public class ReceitaOrcamentariaListDTO {
    private Long id;
    private Integer exercicio;
    private String codigoReceita;
    private String descricao;
    private String categoriaEconomica;
    private BigDecimal valorPrevistoInicial;
    private BigDecimal valorArrecadado;
    private BigDecimal valorRecolhido;
}
