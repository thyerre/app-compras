package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorListDTO {

    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpjCpf;
    private String tipoFornecedor;
    private String classificacao;
    private String municipio;
    private String estadoSigla;
    private String telefone;
    private String email;
    private Boolean ativo;
}
