package com.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorRequestDTO {

    @NotBlank(message = "A razão social é obrigatória")
    @Size(max = 255, message = "A razão social deve ter no máximo 255 caracteres")
    private String razaoSocial;

    @Size(max = 255, message = "O nome fantasia deve ter no máximo 255 caracteres")
    private String nomeFantasia;

    @NotBlank(message = "O CNPJ/CPF é obrigatório")
    @Size(max = 18, message = "O CNPJ/CPF deve ter no máximo 18 caracteres")
    private String cnpjCpf;

    @Size(max = 20)
    private String inscricaoEstadual;

    @Size(max = 20)
    private String inscricaoMunicipal;

    @NotNull(message = "O tipo de fornecedor é obrigatório")
    private Integer tipoFornecedorId;

    private Integer classificacaoId;

    // Endereço
    @NotBlank(message = "O CEP é obrigatório")
    @Size(max = 10)
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório")
    @Size(max = 255)
    private String logradouro;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 20)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 100)
    private String bairro;

    @NotNull(message = "O município é obrigatório")
    private Integer municipioId;

    @NotNull(message = "O estado é obrigatório")
    private Integer estadoId;

    // Contato
    @Size(max = 20)
    private String telefone;

    @Size(max = 20)
    private String celular;

    @Size(max = 255)
    private String email;

    // Responsável legal
    @Size(max = 200)
    private String responsavelNome;

    @Size(max = 14)
    private String responsavelCpf;

    @Size(max = 20)
    private String responsavelRg;

    @Size(max = 100)
    private String responsavelCargo;

    // Dados bancários
    @Size(max = 100)
    private String bancoNome;

    @Size(max = 20)
    private String bancoAgencia;

    @Size(max = 30)
    private String bancoConta;

    // Status
    private Boolean ativo;

    private String observacoes;

    // Certidões (nested)
    @Valid
    private List<CertidaoFiscalDTO> certidoes;
}
