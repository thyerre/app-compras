package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContribuinteRequestDTO {

    @NotBlank(message = "O tipo de pessoa é obrigatório")
    @Size(max = 2)
    private String tipoPessoa;

    @NotBlank(message = "O CPF/CNPJ é obrigatório")
    @Size(max = 18)
    private String cpfCnpj;

    @NotBlank(message = "O nome/razão social é obrigatório")
    @Size(max = 255)
    private String nomeRazaoSocial;

    @Size(max = 255)
    private String nomeFantasia;

    @Size(max = 20)
    private String inscricaoMunicipal;

    @Size(max = 255)
    private String email;

    @Size(max = 20)
    private String telefone;

    @Size(max = 20)
    private String celular;

    @Size(max = 10)
    private String cep;

    @Size(max = 255)
    private String logradouro;

    @Size(max = 20)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @Size(max = 100)
    private String bairro;

    private Integer municipioId;
    private Integer estadoId;

    private Boolean ativo;
    private String observacoes;
}
