package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoContasListDTO {
    private Long id;
    private String codigo;
    private String descricao;
    private Short classe;
    private String naturezaSaldo;
    private String tipo;
    private Short nivel;
    private Boolean escrituravel;
    private Boolean ativo;
}
