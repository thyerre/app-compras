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
public class DividaPublicaListDTO {
    private Long id;
    private String tipo;
    private String credor;
    private String numeroContrato;
    private LocalDate dataContratacao;
    private BigDecimal valorOriginal;
    private BigDecimal saldoDevedor;
    private String status;
}
