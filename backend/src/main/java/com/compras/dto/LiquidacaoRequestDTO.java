package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class LiquidacaoRequestDTO {

    @NotNull(message = "O empenho é obrigatório")
    private Long empenhoId;

    @NotBlank(message = "O número da liquidação é obrigatório")
    @Size(max = 30)
    private String numeroLiquidacao;

    @NotNull(message = "A data da liquidação é obrigatória")
    private LocalDate dataLiquidacao;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;

    @Size(max = 50)
    private String documentoTipo;

    @Size(max = 50)
    private String documentoNumero;

    private LocalDate documentoData;
    private String descricao;
    private String status;
}
