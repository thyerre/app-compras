package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcaoListDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private String tipo;
    private String funcaoNome;
    private String subfuncaoNome;
    private Boolean ativo;
}
