package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolhaPagamentoRequestDTO {

    @NotBlank(message = "A competência é obrigatória")
    @Size(max = 7)
    private String competencia;

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    private String dataPagamento;
    private String status;
    private String observacoes;
}
