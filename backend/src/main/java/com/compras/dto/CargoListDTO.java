package com.compras.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoListDTO {

    private Integer id;
    private String codigo;
    private String descricao;
    private String tipo;
    private Integer vagas;
    private Integer vagasOcupadas;
    private Integer cargaHorariaSemanal;
    private Boolean ativo;
}
