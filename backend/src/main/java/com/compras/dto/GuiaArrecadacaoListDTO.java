package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuiaArrecadacaoListDTO {

    private Long id;
    private String numeroGuia;
    private String contribuinteNome;
    private String contribuinteCpfCnpj;
    private String dataEmissao;
    private String dataVencimento;
    private BigDecimal valorTotal;
    private String status;
}
