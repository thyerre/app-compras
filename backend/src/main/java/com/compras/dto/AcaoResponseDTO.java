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
public class AcaoResponseDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private String tipo;
    private String descricao;
    private FuncaoDTO funcao;
    private SubfuncaoDTO subfuncao;
    private Boolean ativo;
    private LocalDateTime createdAt;
}
