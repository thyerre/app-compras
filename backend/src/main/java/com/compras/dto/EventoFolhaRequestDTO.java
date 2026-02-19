package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoFolhaRequestDTO {

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 10)
    private String codigo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255)
    private String descricao;

    @NotBlank(message = "O tipo é obrigatório")
    @Size(max = 10)
    private String tipo;

    private Boolean incidenciaInss;
    private Boolean incidenciaIrrf;
    private Boolean incidenciaFgts;
    private Boolean automatico;
    private String formula;
    private BigDecimal percentual;
    private BigDecimal valorFixo;

    @Size(max = 20)
    private String tipoCalculo;

    private Boolean aplicaMensal;
    private Boolean aplicaFerias;
    private Boolean aplica13;
    private Boolean aplicaRescisao;
    private Boolean ativo;
}
