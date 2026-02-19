package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelSalarialResponseDTO {

    private Integer id;
    private Integer cargoId;
    private String cargoDescricao;
    private String nivel;
    private String classe;
    private String referencia;
    private BigDecimal valorBase;
    private LocalDate vigenciaInicio;
    private LocalDate vigenciaFim;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
