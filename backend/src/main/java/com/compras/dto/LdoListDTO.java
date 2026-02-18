package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LdoListDTO {
    private Long id;
    private Integer exercicio;
    private String descricao;
    private String status;
    private String ppaPeriodo;
}
