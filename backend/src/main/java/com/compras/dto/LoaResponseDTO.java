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
public class LoaResponseDTO {
    private Long id;
    private Integer exercicio;
    private LdoListDTO ldo;
    private String descricao;
    private BigDecimal valorTotalReceita;
    private BigDecimal valorTotalDespesa;
    private String status;
    private LocalDate dataAprovacao;
    private String observacoes;
    private LocalDateTime createdAt;
}
