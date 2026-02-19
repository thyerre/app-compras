package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelSalarialListDTO {

    private Integer id;
    private String cargoDescricao;
    private String nivel;
    private String classe;
    private String referencia;
    private BigDecimal valorBase;
    private LocalDate vigenciaInicio;
    private LocalDate vigenciaFim;
    private Boolean ativo;
}
