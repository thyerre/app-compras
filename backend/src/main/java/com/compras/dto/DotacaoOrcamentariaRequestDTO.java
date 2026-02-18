package com.compras.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotacaoOrcamentariaRequestDTO {

    @NotNull(message = "A LOA é obrigatória")
    private Long loaId;

    @NotNull(message = "O órgão é obrigatório")
    private Integer orgaoId;

    @NotNull(message = "A unidade é obrigatória")
    private Integer unidadeId;

    @NotNull(message = "A função é obrigatória")
    private Integer funcaoId;

    @NotNull(message = "A subfunção é obrigatória")
    private Integer subfuncaoId;

    @NotNull(message = "O programa é obrigatório")
    private Integer programaId;

    @NotNull(message = "A ação é obrigatória")
    private Integer acaoId;

    @NotNull(message = "A natureza de despesa é obrigatória")
    private Integer naturezaDespesaId;

    @NotNull(message = "A fonte de recurso é obrigatória")
    private Integer fonteRecursoId;

    @NotNull(message = "O valor inicial é obrigatório")
    private BigDecimal valorInicial;

    private BigDecimal valorSuplementado;
    private BigDecimal valorAnulado;
    private BigDecimal valorEmpenhado;

    @Size(max = 500)
    private String descricao;
}
