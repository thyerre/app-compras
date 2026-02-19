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
public class BemPatrimonialListDTO {
    private Long id;
    private String numeroPatrimonio;
    private String descricao;
    private String tipo;
    private String orgaoNome;
    private LocalDate dataAquisicao;
    private BigDecimal valorAtual;
    private String estadoConservacao;
    private String situacao;
}
