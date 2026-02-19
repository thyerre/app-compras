package com.compras.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServidorListDTO {

    private Long id;
    private String matricula;
    private String nome;
    private String cpf;
    private String cargoDescricao;
    private String tipoVinculoDescricao;
    private String orgaoNome;
    private String unidadeNome;
    private String dataAdmissao;
    private String situacao;
    private Boolean ativo;
}
