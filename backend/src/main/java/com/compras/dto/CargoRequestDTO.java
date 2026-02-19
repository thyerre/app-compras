package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoRequestDTO {

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 10)
    private String codigo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255)
    private String descricao;

    @NotBlank(message = "O tipo é obrigatório")
    @Size(max = 30)
    private String tipo;

    @Size(max = 10)
    private String cbo;

    private Integer vagas;
    private Integer vagasOcupadas;

    @Size(max = 30)
    private String escolaridadeMinima;

    private Integer cargaHorariaSemanal;
    private Boolean ativo;
}
