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
public class ProgramaRequestDTO {

    @NotBlank(message = "O código do programa é obrigatório")
    @Size(max = 10, message = "O código deve ter no máximo 10 caracteres")
    private String codigo;

    @NotBlank(message = "O nome do programa é obrigatório")
    @Size(max = 300, message = "O nome deve ter no máximo 300 caracteres")
    private String nome;

    private String objetivo;
    private String publicoAlvo;

    @NotNull(message = "O exercício inicial é obrigatório")
    private Integer exercicioInicio;

    @NotNull(message = "O exercício final é obrigatório")
    private Integer exercicioFim;

    private Boolean ativo;
}
