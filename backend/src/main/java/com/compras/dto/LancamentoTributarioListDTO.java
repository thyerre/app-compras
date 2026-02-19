package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoTributarioListDTO {

    private Long id;
    private String numeroLancamento;
    private Integer exercicio;
    private String contribuinteNome;
    private String contribuinteCpfCnpj;
    private String tributoCodigo;
    private String tributoDescricao;
    private String dataLancamento;
    private String dataVencimento;
    private BigDecimal valorTotal;
    private String status;
}
