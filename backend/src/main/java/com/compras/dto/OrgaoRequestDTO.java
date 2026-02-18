package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgaoRequestDTO {

    @NotBlank(message = "O código do órgão é obrigatório")
    @Size(max = 10, message = "O código deve ter no máximo 10 caracteres")
    private String codigo;

    @NotBlank(message = "O nome do órgão é obrigatório")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    private String nome;

    private Boolean ativo;
}
