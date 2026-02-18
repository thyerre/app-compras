package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
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
public class PpaProgramaMetaDTO {

    private Long id;

    @NotBlank(message = "A descrição da meta é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private Integer exercicio;
    private BigDecimal valorPrevisto;
    private String unidadeMedida;
    private BigDecimal quantidadePrevista;
}
