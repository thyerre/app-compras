package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividaPublicaResponseDTO {
    private Long id;
    private String tipo;
    private String credor;
    private String numeroContrato;
    private LocalDate dataContratacao;
    private LocalDate dataVencimento;
    private BigDecimal valorOriginal;
    private BigDecimal saldoDevedor;
    private BigDecimal taxaJuros;
    private String indiceCorrecao;
    private String finalidade;
    private String leiAutorizativa;
    private String status;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
