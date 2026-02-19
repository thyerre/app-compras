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
public class BemPatrimonialResponseDTO {
    private Long id;
    private String numeroPatrimonio;
    private String descricao;
    private String tipo;
    private String categoria;
    private String orgaoNome;
    private String unidadeNome;
    private LocalDate dataAquisicao;
    private BigDecimal valorAquisicao;
    private BigDecimal valorAtual;
    private Integer vidaUtilMeses;
    private BigDecimal valorResidual;
    private BigDecimal depreciacaoAcumulada;
    private String localizacao;
    private String responsavel;
    private String estadoConservacao;
    private String situacao;
    private String notaFiscal;
    private FornecedorListDTO fornecedor;
    private EmpenhoListDTO empenho;
    private PlanoContasListDTO planoConta;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
