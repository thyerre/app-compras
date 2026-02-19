package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsignadoResponseDTO {

    private Long id;
    private Long servidorId;
    private String servidorNome;
    private Integer eventoFolhaId;
    private String eventoFolhaDescricao;
    private String consignataria;
    private String contrato;
    private Integer parcelaAtual;
    private Integer parcelaTotal;
    private BigDecimal valorParcela;
    private BigDecimal valorTotal;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
