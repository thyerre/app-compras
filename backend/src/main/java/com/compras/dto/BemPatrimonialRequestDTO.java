package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BemPatrimonialRequestDTO {

    @NotBlank(message = "O número de patrimônio é obrigatório")
    private String numeroPatrimonio;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    private String categoria;

    @NotNull(message = "O órgão é obrigatório")
    private Integer orgaoId;

    private Integer unidadeId;

    @NotNull(message = "A data de aquisição é obrigatória")
    private LocalDate dataAquisicao;

    @NotNull(message = "O valor de aquisição é obrigatório")
    private BigDecimal valorAquisicao;

    @NotNull(message = "O valor atual é obrigatório")
    private BigDecimal valorAtual;

    private Integer vidaUtilMeses;
    private BigDecimal valorResidual;
    private BigDecimal depreciacaoAcumulada;
    private String localizacao;
    private String responsavel;
    private String estadoConservacao;
    private String situacao;
    private String notaFiscal;
    private Long fornecedorId;
    private Long empenhoId;
    private Long planoContaId;
    private String observacoes;
}
