package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoResponseDTO {
    private Long id;
    private LiquidacaoListDTO liquidacao;
    private String numeroPagamento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private ContaBancariaListDTO contaBancaria;
    private String formaPagamento;
    private String documentoBancario;
    private String descricao;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
