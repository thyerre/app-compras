package com.compras.dto;

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
public class PpaProgramaResponseDTO {
    private Long id;
    private ProgramaListDTO programa;
    private BigDecimal valorPrevisto;
    private String indicador;
    private String indiceRecente;
    private String indiceDesejado;
    private List<PpaProgramaMetaDTO> metas;
}
