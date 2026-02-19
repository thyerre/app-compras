package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolhaPagamentoListDTO {

    private Long id;
    private String competencia;
    private String tipo;
    private BigDecimal totalProventos;
    private BigDecimal totalDescontos;
    private BigDecimal totalLiquido;
    private Integer quantidadeServidores;
    private String status;
}
