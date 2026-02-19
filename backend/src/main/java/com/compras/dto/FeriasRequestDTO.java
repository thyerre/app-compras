package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeriasRequestDTO {

    @NotNull(message = "O servidor é obrigatório")
    private Long servidorId;

    @NotNull(message = "O início do período aquisitivo é obrigatório")
    private LocalDate periodoAquisitivoInicio;

    @NotNull(message = "O fim do período aquisitivo é obrigatório")
    private LocalDate periodoAquisitivoFim;

    private LocalDate periodoGozoInicio;
    private LocalDate periodoGozoFim;
    private Integer diasGozo;
    private Integer diasAbono;
    private Integer parcelas;
    private BigDecimal valorFerias;
    private BigDecimal valorAbono;
    private BigDecimal valor13Ferias;

    @Size(max = 20)
    private String status;

    private String observacoes;
}
