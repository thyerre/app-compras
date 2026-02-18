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
public class ProcessoDocumentoDTO {
    private Long id;
    private String nome;
    private String tipo;
    private String arquivoNome;
    private String arquivoPath;
    private Long tamanhoBytes;
    private String observacao;
    private LocalDateTime createdAt;
}
