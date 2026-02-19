package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelSalarialRequestDTO {

    @NotNull(message = "O cargo é obrigatório")
    private Integer cargoId;

    @NotBlank(message = "O nível é obrigatório")
    @Size(max = 10)
    private String nivel;

    @Size(max = 10)
    private String classe;

    @Size(max = 10)
    private String referencia;

    @NotNull(message = "O valor base é obrigatório")
    private BigDecimal valorBase;

    @NotNull(message = "A vigência inicial é obrigatória")
    private LocalDate vigenciaInicio;

    private LocalDate vigenciaFim;

    private Boolean ativo;
}
