package com.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PpaRequestDTO {

    @NotNull(message = "O exercício inicial é obrigatório")
    private Integer exercicioInicio;

    @NotNull(message = "O exercício final é obrigatório")
    private Integer exercicioFim;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 300, message = "A descrição deve ter no máximo 300 caracteres")
    private String descricao;

    private String status;
    private String observacoes;

    @Valid
    private List<PpaProgramaDTO> programas;
}
