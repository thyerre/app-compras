package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoContasResponseDTO {
    private Long id;
    private String codigo;
    private String descricao;
    private Short classe;
    private String naturezaSaldo;
    private String tipo;
    private Short nivel;
    private PlanoContasListDTO parent;
    private Boolean escrituravel;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
