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
public class LoaRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "A LDO é obrigatória")
    private Long ldoId;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 300, message = "A descrição deve ter no máximo 300 caracteres")
    private String descricao;

    private BigDecimal valorTotalReceita;
    private BigDecimal valorTotalDespesa;

    private String status;
    private String observacoes;
}
