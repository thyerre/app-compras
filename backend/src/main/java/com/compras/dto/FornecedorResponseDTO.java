package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorResponseDTO {

    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpjCpf;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;

    private TipoFornecedorDTO tipoFornecedor;
    private ClassificacaoFornecedorDTO classificacao;

    // Endereço
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private MunicipioDTO municipio;
    private EstadoDTO estado;

    // Contato
    private String telefone;
    private String celular;
    private String email;

    // Responsável legal
    private String responsavelNome;
    private String responsavelCpf;
    private String responsavelRg;
    private String responsavelCargo;

    // Dados bancários
    private String bancoNome;
    private String bancoAgencia;
    private String bancoConta;

    // Status
    private Boolean ativo;
    private String observacoes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    private List<CertidaoFiscalDTO> certidoes;
    private List<HistoricoLicitacaoDTO> historicoLicitacoes;
}
