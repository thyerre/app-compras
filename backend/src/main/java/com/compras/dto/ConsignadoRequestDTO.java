package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsignadoRequestDTO {

    @NotNull(message = "O servidor é obrigatório")
    private Long servidorId;

    private Integer eventoFolhaId;

    @NotBlank(message = "A consignatária é obrigatória")
    @Size(max = 255)
    private String consignataria;

    @Size(max = 50)
    private String contrato;

    private Integer parcelaAtual;

    @NotNull(message = "O total de parcelas é obrigatório")
    private Integer parcelaTotal;

    @NotNull(message = "O valor da parcela é obrigatório")
    private BigDecimal valorParcela;

    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Size(max = 20)
    private String status;

    private String observacoes;
}
