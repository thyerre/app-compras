package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramaResponseDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private String objetivo;
    private String publicoAlvo;
    private Integer exercicioInicio;
    private Integer exercicioFim;
    private Boolean ativo;
    private LocalDateTime createdAt;
}
