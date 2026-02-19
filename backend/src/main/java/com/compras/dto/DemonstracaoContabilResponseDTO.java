package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemonstracaoContabilResponseDTO {
    private Long id;
    private Integer exercicio;
    private String tipo;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
    private String status;
    private LocalDateTime dataGeracao;
    private LocalDate dataPublicacao;
    private String responsavel;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
