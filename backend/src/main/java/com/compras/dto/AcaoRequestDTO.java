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
public class AcaoRequestDTO {

    @NotBlank(message = "O código da ação é obrigatório")
    @Size(max = 10, message = "O código deve ter no máximo 10 caracteres")
    private String codigo;

    @NotBlank(message = "O nome da ação é obrigatório")
    @Size(max = 300, message = "O nome deve ter no máximo 300 caracteres")
    private String nome;

    @NotBlank(message = "O tipo é obrigatório")
    @Size(max = 20, message = "O tipo deve ter no máximo 20 caracteres")
    private String tipo;

    private String descricao;

    @NotNull(message = "A função é obrigatória")
    private Integer funcaoId;

    @NotNull(message = "A subfunção é obrigatória")
    private Integer subfuncaoId;

    private Boolean ativo;
}
