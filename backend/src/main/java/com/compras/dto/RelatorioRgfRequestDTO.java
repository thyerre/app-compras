package com.compras.dto;

import jakarta.validation.constraints.NotNull;
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
public class RelatorioRgfRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "O quadrimestre é obrigatório")
    private Integer quadrimestre;

    @NotNull(message = "O período início é obrigatório")
    private LocalDate periodoInicio;

    @NotNull(message = "O período fim é obrigatório")
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
    private String responsavelNome;
    private String responsavelCargo;
    private String contadorNome;
    private String contadorCrc;
    private String observacoes;
}
