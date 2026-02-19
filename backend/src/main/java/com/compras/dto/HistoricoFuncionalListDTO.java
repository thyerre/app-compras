package com.compras.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoFuncionalListDTO {

    private Long id;
    private String servidorNome;
    private String servidorMatricula;
    private String tipoEvento;
    private LocalDate dataEvento;
    private String descricao;
}
