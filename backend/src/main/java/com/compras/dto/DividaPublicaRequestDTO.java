package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
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
public class DividaPublicaRequestDTO {

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    @NotBlank(message = "O credor é obrigatório")
    private String credor;

    private String numeroContrato;

    @NotNull(message = "A data de contratação é obrigatória")
    private LocalDate dataContratacao;

    private LocalDate dataVencimento;

    @NotNull(message = "O valor original é obrigatório")
    private BigDecimal valorOriginal;

    @NotNull(message = "O saldo devedor é obrigatório")
    private BigDecimal saldoDevedor;

    private BigDecimal taxaJuros;
    private String indiceCorrecao;
    private String finalidade;
    private String leiAutorizativa;
    private String status;
    private String observacoes;
}
