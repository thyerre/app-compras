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
public class RestosAPagarListDTO {
    private Long id;
    private Integer exercicioOrigem;
    private String numeroEmpenho;
    private String fornecedorNome;
    private String tipo;
    private BigDecimal valorInscrito;
    private BigDecimal valorPago;
    private LocalDate dataInscricao;
    private String status;
}
