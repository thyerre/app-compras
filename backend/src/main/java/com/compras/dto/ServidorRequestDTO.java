package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServidorRequestDTO {

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(max = 20)
    private String matricula;

    private Long usuarioId;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(max = 14)
    private String cpf;

    @Size(max = 20)
    private String rg;

    @Size(max = 20)
    private String rgOrgaoEmissor;

    @NotNull(message = "A data de nascimento é obrigatória")
    private String dataNascimento;

    @NotBlank(message = "O sexo é obrigatório")
    @Size(max = 1)
    private String sexo;

    @Size(max = 20)
    private String estadoCivil;

    @Size(max = 50)
    private String nacionalidade;

    @Size(max = 100)
    private String naturalidade;

    @Size(max = 15)
    private String pisPasep;

    @Size(max = 15)
    private String tituloEleitor;

    @Size(max = 5)
    private String zonaEleitoral;

    @Size(max = 5)
    private String secaoEleitoral;

    @Size(max = 15)
    private String ctpsNumero;

    @Size(max = 10)
    private String ctpsSerie;

    @Size(max = 15)
    private String cnhNumero;

    @Size(max = 5)
    private String cnhCategoria;

    @Size(max = 30)
    private String grauInstrucao;

    @Size(max = 255)
    private String email;

    @Size(max = 20)
    private String telefone;

    @Size(max = 20)
    private String celular;

    // Endereço
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

    // Dados funcionais
    @NotNull(message = "O cargo é obrigatório")
    private Integer cargoId;

    private Integer nivelSalarialId;

    @NotNull(message = "O tipo de vínculo é obrigatório")
    private Integer tipoVinculoId;

    private Integer orgaoId;
    private Integer unidadeId;

    @NotNull(message = "A data de admissão é obrigatória")
    private String dataAdmissao;

    private String dataPosse;
    private String dataExercicio;
    private String dataDemissao;

    @Size(max = 255)
    private String motivoDemissao;

    // Dados bancários
    @Size(max = 10)
    private String bancoCodigo;

    @Size(max = 100)
    private String bancoNome;

    @Size(max = 20)
    private String agencia;

    @Size(max = 30)
    private String conta;

    @Size(max = 20)
    private String tipoConta;

    // Previdência
    @Size(max = 10)
    private String regimePrevidencia;

    @Size(max = 20)
    private String numeroPrevidencia;

    // Status
    private String situacao;
    private Boolean ativo;
    private String observacoes;

    // Credenciais para criar/atualizar usuário vinculado
    private String senhaUsuario;
    private List<String> roleNames;
}
