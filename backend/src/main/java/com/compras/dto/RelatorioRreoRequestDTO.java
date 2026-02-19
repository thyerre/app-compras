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
public class RelatorioRreoRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "O bimestre é obrigatório")
    private Integer bimestre;

    @NotNull(message = "O período início é obrigatório")
    private LocalDate periodoInicio;

    @NotNull(message = "O período fim é obrigatório")
    private LocalDate periodoFim;

    private BigDecimal receitaPrevista;
    private BigDecimal receitaRealizada;
    private BigDecimal despesaFixada;
    private BigDecimal despesaEmpenhada;
    private BigDecimal despesaLiquidada;
    private BigDecimal despesaPaga;
    private BigDecimal resultadoPrimario;
    private BigDecimal resultadoNominal;
    private BigDecimal receitaCorrenteLiquida;
    private BigDecimal restosPagarProcessados;
    private BigDecimal restosPagarNaoProc;
    private String status;
    private String responsavelNome;
    private String responsavelCargo;
    private String contadorNome;
    private String contadorCrc;
    private String observacoes;
}
