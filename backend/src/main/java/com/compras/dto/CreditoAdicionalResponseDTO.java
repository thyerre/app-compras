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
public class CreditoAdicionalResponseDTO {
    private Long id;
    private Integer exercicio;
    private String tipo;
    private String numeroDecreto;
    private LocalDate dataDecreto;
    private String numeroLei;
    private LocalDate dataLei;
    private DotacaoOrcamentariaListDTO dotacao;
    private BigDecimal valor;
    private String fonteAnulacao;
    private String justificativa;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
