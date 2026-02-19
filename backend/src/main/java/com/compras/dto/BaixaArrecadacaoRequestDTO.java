package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaixaArrecadacaoRequestDTO {

    @NotNull(message = "A guia de arrecadação é obrigatória")
    private Long guiaArrecadacaoId;

    private Integer agenteArrecadadorId;

    @NotNull(message = "A data de pagamento é obrigatória")
    private String dataPagamento;

    private String dataCredito;

    @NotNull(message = "O valor pago é obrigatório")
    private BigDecimal valorPago;

    private BigDecimal valorJuros;
    private BigDecimal valorMulta;
    private BigDecimal valorDesconto;

    @NotBlank(message = "O tipo de baixa é obrigatório")
    private String tipoBaixa;

    @Size(max = 100)
    private String autenticacao;

    private Long receitaOrcamentariaId;
    private String observacoes;
}
