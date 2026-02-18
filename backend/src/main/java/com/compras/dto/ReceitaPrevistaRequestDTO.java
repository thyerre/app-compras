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
public class ReceitaPrevistaRequestDTO {

    @NotNull(message = "A LOA é obrigatória")
    private Long loaId;

    @NotBlank(message = "O código da receita é obrigatório")
    @Size(max = 20, message = "O código deve ter no máximo 20 caracteres")
    private String codigoReceita;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 300, message = "A descrição deve ter no máximo 300 caracteres")
    private String descricao;

    @NotNull(message = "A fonte de recurso é obrigatória")
    private Integer fonteRecursoId;

    @NotNull(message = "O valor previsto é obrigatório")
    private BigDecimal valorPrevisto;

    private BigDecimal valorArrecadado;
}
