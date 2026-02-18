package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoCompraListDTO {
    private Long id;
    private String numeroProcesso;
    private Integer exercicio;
    private String modalidade;
    private String status;
    private String statusCor;
    private String objeto;
    private BigDecimal valorEstimado;
    private BigDecimal valorHomologado;
    private LocalDate dataAbertura;
    private String orgaoNome;
}
