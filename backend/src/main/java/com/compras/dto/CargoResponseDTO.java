package com.compras.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoResponseDTO {

    private Integer id;
    private String codigo;
    private String descricao;
    private String tipo;
    private String cbo;
    private Integer vagas;
    private Integer vagasOcupadas;
    private String escolaridadeMinima;
    private Integer cargaHorariaSemanal;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
