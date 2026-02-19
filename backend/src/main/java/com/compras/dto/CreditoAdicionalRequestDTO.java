package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreditoAdicionalRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    private String numeroDecreto;
    private LocalDate dataDecreto;
    private String numeroLei;
    private LocalDate dataLei;

    @NotNull(message = "A dotação é obrigatória")
    private Long dotacaoId;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;

    private String fonteAnulacao;

    @NotBlank(message = "A justificativa é obrigatória")
    private String justificativa;

    private String status;
}
