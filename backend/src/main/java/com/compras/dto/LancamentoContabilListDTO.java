package com.compras.dto;

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
public class LancamentoContabilListDTO {
    private Long id;
    private String numero;
    private LocalDate dataLancamento;
    private String tipo;
    private String origem;
    private String historico;
    private BigDecimal valorTotal;
    private String status;
}
