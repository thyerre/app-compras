package com.compras.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemonstracaoContabilRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "O tipo é obrigatório")
    private String tipo;

    @NotNull(message = "O período início é obrigatório")
    private LocalDate periodoInicio;

    @NotNull(message = "O período fim é obrigatório")
    private LocalDate periodoFim;

    private String status;
    private String responsavel;
    private String observacoes;
}
