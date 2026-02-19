package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditoAdicionalListDTO {
    private Long id;
    private Integer exercicio;
    private String tipo;
    private String numeroDecreto;
    private String numeroLei;
    private BigDecimal valor;
    private String status;
}
