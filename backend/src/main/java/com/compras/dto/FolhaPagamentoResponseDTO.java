package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolhaPagamentoResponseDTO {

    private Long id;
    private String competencia;
    private String tipo;
    private LocalDateTime dataCalculo;
    private LocalDateTime dataAprovacao;
    private String aprovadoPorNome;
    private String dataPagamento;
    private BigDecimal totalProventos;
    private BigDecimal totalDescontos;
    private BigDecimal totalLiquido;
    private Integer quantidadeServidores;
    private String status;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
