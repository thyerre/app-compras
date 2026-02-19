package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuiaArrecadacaoResponseDTO {

    private Long id;
    private String numeroGuia;
    private Long lancamentoTributarioId;
    private String lancamentoNumero;
    private Long contribuinteId;
    private String contribuinteNome;
    private String dataEmissao;
    private String dataVencimento;
    private BigDecimal valorPrincipal;
    private BigDecimal valorDesconto;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;
    private BigDecimal valorTotal;
    private String codigoBarras;
    private String linhaDigitavel;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
