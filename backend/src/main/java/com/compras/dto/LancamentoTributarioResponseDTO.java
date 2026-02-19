package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoTributarioResponseDTO {

    private Long id;
    private String numeroLancamento;
    private Integer exercicio;
    private Long contribuinteId;
    private String contribuinteNome;
    private String contribuinteCpfCnpj;
    private Integer tributoId;
    private String tributoCodigo;
    private String tributoDescricao;
    private Long imovelId;
    private String imovelInscricao;
    private Long atividadeEconomicaId;
    private String dataLancamento;
    private String dataVencimento;
    private BigDecimal baseCalculo;
    private BigDecimal aliquota;
    private BigDecimal valorPrincipal;
    private BigDecimal valorDesconto;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;
    private BigDecimal valorTotal;
    private String status;
    private String historico;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
