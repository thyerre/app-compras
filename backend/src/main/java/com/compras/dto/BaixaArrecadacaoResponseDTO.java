package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaixaArrecadacaoResponseDTO {

    private Long id;
    private Long guiaArrecadacaoId;
    private String guiaNumero;
    private Integer agenteArrecadadorId;
    private String agenteDescricao;
    private String dataPagamento;
    private String dataCredito;
    private BigDecimal valorPago;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;
    private BigDecimal valorDesconto;
    private String tipoBaixa;
    private String autenticacao;
    private Long receitaOrcamentariaId;
    private String observacoes;
    private LocalDateTime createdAt;
}
