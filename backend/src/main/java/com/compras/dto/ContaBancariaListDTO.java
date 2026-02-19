package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaBancariaListDTO {
    private Long id;
    private String bancoCodigo;
    private String bancoNome;
    private String agencia;
    private String numeroConta;
    private String descricao;
    private String tipo;
    private BigDecimal saldoAtual;
    private Boolean ativo;
}
