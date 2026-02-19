package com.compras.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContribuinteResponseDTO {

    private Long id;
    private String tipoPessoa;
    private String cpfCnpj;
    private String nomeRazaoSocial;
    private String nomeFantasia;
    private String inscricaoMunicipal;
    private String email;
    private String telefone;
    private String celular;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private MunicipioDTO municipio;
    private EstadoDTO estado;
    private Boolean ativo;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
