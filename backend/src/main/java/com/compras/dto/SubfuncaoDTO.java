package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubfuncaoDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private Integer funcaoId;
    private String funcaoNome;
}
