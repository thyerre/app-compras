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
public class ReceitaPrevistaResponseDTO {
    private Long id;
    private LoaListDTO loa;
    private String codigoReceita;
    private String descricao;
    private FonteRecursoDTO fonteRecurso;
    private BigDecimal valorPrevisto;
    private BigDecimal valorArrecadado;
}
