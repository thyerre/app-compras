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
public class LiquidacaoListDTO {
    private Long id;
    private String numeroLiquidacao;
    private String numeroEmpenho;
    private String fornecedorNome;
    private LocalDate dataLiquidacao;
    private BigDecimal valor;
    private String documentoTipo;
    private String documentoNumero;
    private String status;
}
