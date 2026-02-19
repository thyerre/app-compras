package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoContasRequestDTO {

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 30, message = "O código deve ter no máximo 30 caracteres")
    private String codigo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "A classe é obrigatória")
    private Short classe;

    @NotBlank(message = "A natureza do saldo é obrigatória")
    private String naturezaSaldo;

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    @NotNull(message = "O nível é obrigatório")
    private Short nivel;

    private Long parentId;
    private Boolean escrituravel;
    private Boolean ativo;
}
