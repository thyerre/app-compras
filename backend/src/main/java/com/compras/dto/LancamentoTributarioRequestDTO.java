package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoTributarioRequestDTO {

    @NotBlank(message = "O número do lançamento é obrigatório")
    @Size(max = 20)
    private String numeroLancamento;

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotNull(message = "O contribuinte é obrigatório")
    private Long contribuinteId;

    @NotNull(message = "O tributo é obrigatório")
    private Integer tributoId;

    private Long imovelId;
    private Long atividadeEconomicaId;

    @NotNull(message = "A data de lançamento é obrigatória")
    private String dataLancamento;

    @NotNull(message = "A data de vencimento é obrigatória")
    private String dataVencimento;

    private BigDecimal baseCalculo;
    private BigDecimal aliquota;

    @NotNull(message = "O valor principal é obrigatório")
    private BigDecimal valorPrincipal;

    private BigDecimal valorDesconto;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;

    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;

    private String status;
    private String historico;
}
