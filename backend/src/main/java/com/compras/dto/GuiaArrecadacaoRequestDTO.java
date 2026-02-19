package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuiaArrecadacaoRequestDTO {

    @NotBlank(message = "O número da guia é obrigatório")
    @Size(max = 30)
    private String numeroGuia;

    @NotNull(message = "O lançamento tributário é obrigatório")
    private Long lancamentoTributarioId;

    @NotNull(message = "O contribuinte é obrigatório")
    private Long contribuinteId;

    @NotNull(message = "A data de emissão é obrigatória")
    private String dataEmissao;

    @NotNull(message = "A data de vencimento é obrigatória")
    private String dataVencimento;

    @NotNull(message = "O valor principal é obrigatório")
    private BigDecimal valorPrincipal;

    private BigDecimal valorDesconto;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;

    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;

    @Size(max = 60)
    private String codigoBarras;

    @Size(max = 60)
    private String linhaDigitavel;

    private String status;
}
