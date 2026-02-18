package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgaoDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private Boolean ativo;
}
