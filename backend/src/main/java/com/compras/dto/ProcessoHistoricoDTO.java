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
public class ProcessoHistoricoDTO {
    private Long id;
    private String statusAnterior;
    private String statusNovo;
    private String descricao;
    private String usuarioNome;
    private LocalDateTime dataRegistro;
}
