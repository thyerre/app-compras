package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaixaArrecadacaoListDTO {

    private Long id;
    private String guiaNumero;
    private String contribuinteNome;
    private String dataPagamento;
    private BigDecimal valorPago;
    private String tipoBaixa;
    private String agenteDescricao;
}
