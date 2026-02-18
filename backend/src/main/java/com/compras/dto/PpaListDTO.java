package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PpaListDTO {
    private Long id;
    private Integer exercicioInicio;
    private Integer exercicioFim;
    private String descricao;
    private String status;
}
