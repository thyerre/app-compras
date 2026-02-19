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
public class ReceitaOrcamentariaResponseDTO {
    private Long id;
    private Integer exercicio;
    private LoaListDTO loa;
    private String codigoReceita;
    private String descricao;
    private String categoriaEconomica;
    private String origem;
    private String especie;
    private FonteRecursoDTO fonteRecurso;
    private BigDecimal valorPrevistoInicial;
    private BigDecimal valorPrevistoAtualizado;
    private BigDecimal valorLancado;
    private BigDecimal valorArrecadado;
    private BigDecimal valorRecolhido;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
