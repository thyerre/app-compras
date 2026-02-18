package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ProcessoItemDTO {

    private Long id;

    @NotNull(message = "O número do item é obrigatório")
    private Integer numeroItem;

    @NotBlank(message = "A descrição do item é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "A unidade de medida é obrigatória")
    @Size(max = 30, message = "A unidade deve ter no máximo 30 caracteres")
    private String unidadeMedida;

    @NotNull(message = "A quantidade é obrigatória")
    private BigDecimal quantidade;

    @NotNull(message = "O valor unitário estimado é obrigatório")
    private BigDecimal valorUnitarioEstimado;

    private BigDecimal valorTotalEstimado;
    private BigDecimal valorUnitarioHomologado;
    private BigDecimal valorTotalHomologado;

    private Long fornecedorVencedorId;
    private String fornecedorVencedorNome;

    @Size(max = 50)
    private String marca;

    @Size(max = 200)
    private String modelo;

    private String situacao;
}
