package com.compras.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TributoDTO {

    private Integer id;
    private String codigo;
    private String descricao;
    private String tipo;
    private Boolean ativo;
}
