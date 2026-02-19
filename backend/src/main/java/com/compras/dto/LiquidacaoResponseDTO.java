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
public class LiquidacaoResponseDTO {
    private Long id;
    private EmpenhoListDTO empenho;
    private String numeroLiquidacao;
    private LocalDate dataLiquidacao;
    private BigDecimal valor;
    private String documentoTipo;
    private String documentoNumero;
    private LocalDate documentoData;
    private String descricao;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
