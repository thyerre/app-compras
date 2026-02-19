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
public class RelatorioRgfResponseDTO {
    private Long id;
    private Integer exercicio;
    private Integer quadrimestre;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
    private BigDecimal receitaCorrenteLiquida;
    private BigDecimal despesaPessoalExecutivo;
    private BigDecimal despesaPessoalLegislativo;
    private BigDecimal despesaPessoalTotal;
    private BigDecimal percentualPessoalExecutivo;
    private BigDecimal percentualPessoalLegislativo;
    private BigDecimal percentualPessoalTotal;
    private BigDecimal limiteMaximo;
    private BigDecimal limitePrudencial;
    private BigDecimal limiteAlerta;
    private BigDecimal dividaConsolidada;
    private BigDecimal limiteDivida;
    private BigDecimal percentualDivida;
    private BigDecimal disponibilidadeCaixa;
    private BigDecimal obrigacoesFinanceiras;
    private String status;
    private LocalDateTime dataGeracao;
    private LocalDate dataPublicacao;
    private String responsavelNome;
    private String responsavelCargo;
    private String contadorNome;
    private String contadorCrc;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
