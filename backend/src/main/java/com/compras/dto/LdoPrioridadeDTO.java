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
public class LdoPrioridadeDTO {

    private Long id;

    @NotNull(message = "O programa é obrigatório")
    private Integer programaId;

    private String programaNome;

    @NotNull(message = "A ação é obrigatória")
    private Integer acaoId;

    private String acaoNome;

    @NotBlank(message = "A meta é obrigatória")
    @Size(max = 500, message = "A meta deve ter no máximo 500 caracteres")
    private String meta;

    private BigDecimal valorEstimado;
    private Integer prioridade;
}
