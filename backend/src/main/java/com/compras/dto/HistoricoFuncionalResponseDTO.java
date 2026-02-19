package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoFuncionalResponseDTO {

    private Long id;
    private Long servidorId;
    private String servidorNome;
    private String tipoEvento;
    private LocalDate dataEvento;
    private String numeroAto;
    private String descricao;
    private Integer cargoAnteriorId;
    private String cargoAnteriorDescricao;
    private Integer cargoNovoId;
    private String cargoNovoDescricao;
    private BigDecimal salarioAnterior;
    private BigDecimal salarioNovo;
    private Integer orgaoAnteriorId;
    private String orgaoAnteriorNome;
    private Integer orgaoNovoId;
    private String orgaoNovoNome;
    private Integer unidadeAnteriorId;
    private String unidadeAnteriorNome;
    private Integer unidadeNovoId;
    private String unidadeNovoNome;
    private String observacoes;
    private LocalDateTime createdAt;
}
