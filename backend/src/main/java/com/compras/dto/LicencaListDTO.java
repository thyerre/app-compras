package com.compras.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicencaListDTO {

    private Long id;
    private String servidorNome;
    private String servidorMatricula;
    private String tipoLicenca;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer quantidadeDias;
    private Boolean remunerada;
}
