package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertidaoFiscalDTO {

    private Long id;

    @NotBlank(message = "O nome da certidão é obrigatório")
    @Size(max = 200)
    private String nome;

    @Size(max = 100)
    private String numero;

    private LocalDate dataEmissao;
    private LocalDate dataValidade;

    private String arquivoNome;
    private String arquivoPath;

    private String observacoes;
}
