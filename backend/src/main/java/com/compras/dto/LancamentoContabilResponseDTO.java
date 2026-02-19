package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoContabilResponseDTO {
    private Long id;
    private Integer exercicio;
    private String numero;
    private LocalDate dataLancamento;
    private String tipo;
    private String origem;
    private String historico;
    private BigDecimal valorTotal;
    private String documentoRef;
    private String status;
    private List<LancamentoItemDTO> itens;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
