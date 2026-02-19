package com.compras.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVinculoDTO {

    private Integer id;
    private String codigo;
    private String descricao;
    private String regime;
    private Boolean ativo;
}
