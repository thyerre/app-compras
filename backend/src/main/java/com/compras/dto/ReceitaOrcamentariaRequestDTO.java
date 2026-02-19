package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceitaOrcamentariaRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "A LOA é obrigatória")
    private Long loaId;

    private Long receitaPrevistaId;

    @NotBlank(message = "O código da receita é obrigatório")
    private String codigoReceita;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "A categoria econômica é obrigatória")
    private String categoriaEconomica;

    private String origem;
    private String especie;
    private Integer fonteRecursoId;
    private BigDecimal valorPrevistoInicial;
    private BigDecimal valorPrevistoAtualizado;
    private BigDecimal valorLancado;
    private BigDecimal valorArrecadado;
    private BigDecimal valorRecolhido;
}
