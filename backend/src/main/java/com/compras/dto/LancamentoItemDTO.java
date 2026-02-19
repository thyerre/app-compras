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
public class LancamentoItemDTO {

    private Long id;

    @NotNull(message = "A conta do plano de contas é obrigatória")
    private Long planoContaId;

    private String planoContaCodigo;
    private String planoContaDescricao;

    @NotBlank(message = "O tipo (D/C) é obrigatório")
    private String tipo;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;

    private String historicoItem;
}
