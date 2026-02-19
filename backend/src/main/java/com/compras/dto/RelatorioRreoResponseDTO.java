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
public class RelatorioRreoResponseDTO {
    private Long id;
    private Integer exercicio;
    private Integer bimestre;
    private LocalDate periodoInicio;
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
