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
public class ConciliacaoBancariaRequestDTO {

    @NotNull(message = "A conta bancária é obrigatória")
    private Long contaBancariaId;

    @NotNull(message = "O mês de referência é obrigatório")
    private Integer mesReferencia;

    @NotNull(message = "O ano de referência é obrigatório")
    private Integer anoReferencia;

    @NotNull(message = "O saldo do extrato é obrigatório")
    private BigDecimal saldoExtrato;

    @NotNull(message = "O saldo contábil é obrigatório")
    private BigDecimal saldoContabil;

    private BigDecimal diferenca;
    private String status;
    private String observacoes;
    private String responsavel;
    private LocalDate dataConciliacao;
}
