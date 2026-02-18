package com.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PpaProgramaDTO {

    private Long id;

    @NotNull(message = "O programa é obrigatório")
    private Integer programaId;

    private BigDecimal valorPrevisto;
    private String indicador;
    private String indiceRecente;
    private String indiceDesejado;

    @Valid
    private List<PpaProgramaMetaDTO> metas;
}
