package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MunicipioDTO {
    private Integer id;
    private String nome;
    private String codigoIbge;
    private Integer estadoId;
    private String estadoSigla;
}
