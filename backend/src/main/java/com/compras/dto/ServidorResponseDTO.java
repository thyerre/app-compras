package com.compras.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServidorResponseDTO {

    private Long id;
    private String matricula;
    private Long usuarioId;
    private String usuarioEmail;
    private String nome;
    private String cpf;
    private String rg;
    private String rgOrgaoEmissor;
    private String dataNascimento;
    private String sexo;
    private String estadoCivil;
    private String nacionalidade;
    private String naturalidade;
    private String pisPasep;
    private String tituloEleitor;
    private String zonaEleitoral;
    private String secaoEleitoral;
    private String ctpsNumero;
    private String ctpsSerie;
    private String cnhNumero;
    private String cnhCategoria;
    private String grauInstrucao;
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

    // Dados funcionais
    private CargoListDTO cargo;
    private Integer nivelSalarialId;
    private String nivelSalarialDescricao;
    private TipoVinculoDTO tipoVinculo;
    private OrgaoDTO orgao;
    private UnidadeDTO unidade;
    private String dataAdmissao;
    private String dataPosse;
    private String dataExercicio;
    private String dataDemissao;
    private String motivoDemissao;

    // Dados bancários
    private String bancoCodigo;
    private String bancoNome;
    private String agencia;
    private String conta;
    private String tipoConta;

    // Previdência
    private String regimePrevidencia;
    private String numeroPrevidencia;

    // Status
    private String situacao;
    private Boolean ativo;
    private String observacoes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
