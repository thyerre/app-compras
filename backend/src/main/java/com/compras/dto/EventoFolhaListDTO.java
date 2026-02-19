package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoFolhaListDTO {

    private Integer id;
    private String codigo;
    private String descricao;
    private String tipo;
    private BigDecimal percentual;
    private BigDecimal valorFixo;
    private Boolean ativo;
}
