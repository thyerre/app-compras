package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemonstracaoContabilListDTO {
    private Long id;
    private Integer exercicio;
    private String tipo;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
    private String status;
}
