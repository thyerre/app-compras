package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsignadoListDTO {

    private Long id;
    private String servidorNome;
    private String servidorMatricula;
    private String consignataria;
    private String contrato;
    private Integer parcelaAtual;
    private Integer parcelaTotal;
    private BigDecimal valorParcela;
    private String status;
}
