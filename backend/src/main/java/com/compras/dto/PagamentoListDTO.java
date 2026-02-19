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
public class PagamentoListDTO {
    private Long id;
    private String numeroPagamento;
    private String numeroLiquidacao;
    private String numeroEmpenho;
    private String fornecedorNome;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String formaPagamento;
    private String contaBancariaDescricao;
    private String status;
}
