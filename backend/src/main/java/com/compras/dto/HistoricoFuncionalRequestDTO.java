package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoFuncionalRequestDTO {

    @NotNull(message = "O servidor é obrigatório")
    private Long servidorId;

    @NotBlank(message = "O tipo de evento é obrigatório")
    @Size(max = 50)
    private String tipoEvento;

    @NotNull(message = "A data do evento é obrigatória")
    private LocalDate dataEvento;

    @Size(max = 50)
    private String numeroAto;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    private Integer cargoAnteriorId;
    private Integer cargoNovoId;
    private BigDecimal salarioAnterior;
    private BigDecimal salarioNovo;
    private Integer orgaoAnteriorId;
    private Integer orgaoNovoId;
    private Integer unidadeAnteriorId;
    private Integer unidadeNovoId;
    private String observacoes;
}
