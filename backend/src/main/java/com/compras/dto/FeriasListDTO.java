package com.compras.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeriasListDTO {

    private Long id;
    private String servidorNome;
    private String servidorMatricula;
    private LocalDate periodoAquisitivoInicio;
    private LocalDate periodoAquisitivoFim;
    private LocalDate periodoGozoInicio;
    private LocalDate periodoGozoFim;
    private Integer diasGozo;
    private String status;
}
