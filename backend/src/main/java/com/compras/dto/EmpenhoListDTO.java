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
public class EmpenhoListDTO {
    private Long id;
    private String numeroEmpenho;
    private Integer exercicio;
    private String numeroProcesso;
    private String fornecedorNome;
    private String fornecedorCnpjCpf;
    private BigDecimal valor;
    private BigDecimal valorLiquidado;
    private BigDecimal valorPago;
    private LocalDate dataEmpenho;
    private String tipoEmpenho;
    private String status;
}
