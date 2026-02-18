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
public class EmpenhoResponseDTO {
    private Long id;
    private String numeroEmpenho;
    private Integer exercicio;
    private ProcessoCompraListDTO processo;
    private FornecedorListDTO fornecedor;
    private DotacaoOrcamentariaListDTO dotacao;
    private BigDecimal valor;
    private BigDecimal valorLiquidado;
    private BigDecimal valorPago;
    private LocalDate dataEmpenho;
    private LocalDate dataLiquidacao;
    private LocalDate dataPagamento;
    private String tipoEmpenho;
    private String status;
    private String descricao;
    private String observacao;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
