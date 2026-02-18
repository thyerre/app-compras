package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;

    @Column(name = "cnpj_cpf", nullable = false, unique = true, length = 18)
    private String cnpjCpf;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal", length = 20)
    private String inscricaoMunicipal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_fornecedor_id", nullable = false)
    private TipoFornecedor tipoFornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classificacao_id")
    private ClassificacaoFornecedor classificacao;

    // Endereço
    @Column(nullable = false, length = 10)
    private String cep;

    @Column(nullable = false, length = 255)
    private String logradouro;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(nullable = false, length = 100)
    private String bairro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    // Contato
    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String celular;

    @Column(length = 255)
    private String email;

    // Responsável legal
    @Column(name = "responsavel_nome", length = 200)
    private String responsavelNome;

    @Column(name = "responsavel_cpf", length = 14)
    private String responsavelCpf;

    @Column(name = "responsavel_rg", length = 20)
    private String responsavelRg;

    @Column(name = "responsavel_cargo", length = 100)
    private String responsavelCargo;

    // Dados bancários
    @Column(name = "banco_nome", length = 100)
    private String bancoNome;

    @Column(name = "banco_agencia", length = 20)
    private String bancoAgencia;

    @Column(name = "banco_conta", length = 30)
    private String bancoConta;

    // Status
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CertidaoFiscal> certidoes = new ArrayList<>();

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HistoricoLicitacao> historicoLicitacoes = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
