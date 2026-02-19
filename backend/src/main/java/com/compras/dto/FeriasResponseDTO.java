package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeriasResponseDTO {

    private Long id;
    private Long servidorId;
    private String servidorNome;
    private LocalDate periodoAquisitivoInicio;
    private LocalDate periodoAquisitivoFim;
    private LocalDate periodoGozoInicio;
    private LocalDate periodoGozoFim;
    private Integer diasGozo;
    private Integer diasAbono;
    private Integer parcelas;
    private BigDecimal valorFerias;
    private BigDecimal valorAbono;
    private BigDecimal valor13Ferias;
    private String status;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
