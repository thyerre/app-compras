package com.compras.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContribuinteListDTO {

    private Long id;
    private String tipoPessoa;
    private String cpfCnpj;
    private String nomeRazaoSocial;
    private String nomeFantasia;
    private String inscricaoMunicipal;
    private String telefone;
    private String email;
    private String municipioNome;
    private String estadoSigla;
    private Boolean ativo;
}
