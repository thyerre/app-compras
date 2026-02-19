package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaBancariaResponseDTO {
    private Long id;
    private String bancoCodigo;
    private String bancoNome;
    private String agencia;
    private String numeroConta;
    private String digito;
    private String descricao;
    private String tipo;
    private FonteRecursoDTO fonteRecurso;
    private BigDecimal saldoAtual;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
