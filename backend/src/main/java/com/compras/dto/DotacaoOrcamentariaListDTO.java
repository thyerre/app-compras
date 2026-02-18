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
public class DotacaoOrcamentariaListDTO {
    private Long id;
    private Integer exercicio;
    private String orgaoNome;
    private String unidadeNome;
    private String funcaoNome;
    private String programaNome;
    private String acaoNome;
    private String naturezaDespesaDescricao;
    private String fonteRecursoDescricao;
    private BigDecimal valorInicial;
    private BigDecimal saldoDisponivel;
}
