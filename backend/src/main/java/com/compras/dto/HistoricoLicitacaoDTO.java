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
public class HistoricoLicitacaoDTO {

    private Long id;
    private String numeroProcesso;
    private String descricao;
    private LocalDate dataParticipacao;
    private String resultado;
    private BigDecimal valor;
    private String observacoes;
}
