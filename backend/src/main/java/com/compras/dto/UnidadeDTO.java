package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadeDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private Integer orgaoId;
    private String orgaoNome;
    private Boolean ativo;
}
