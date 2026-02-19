package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "servidores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Column(name = "rg_orgao_emissor", length = 20)
    private String rgOrgaoEmissor;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 1)
    private String sexo;

    @Column(name = "estado_civil", length = 20)
    private String estadoCivil;

    @Column(length = 50)
    @Builder.Default
    private String nacionalidade = "Brasileira";

    @Column(length = 100)
    private String naturalidade;

    @Column(name = "pis_pasep", length = 15)
    private String pisPasep;

    @Column(name = "titulo_eleitor", length = 15)
    private String tituloEleitor;

    @Column(name = "zona_eleitoral", length = 5)
    private String zonaEleitoral;

    @Column(name = "secao_eleitoral", length = 5)
    private String secaoEleitoral;

    @Column(name = "ctps_numero", length = 15)
    private String ctpsNumero;

    @Column(name = "ctps_serie", length = 10)
    private String ctpsSerie;

    @Column(name = "cnh_numero", length = 15)
    private String cnhNumero;

    @Column(name = "cnh_categoria", length = 5)
    private String cnhCategoria;

    @Column(name = "grau_instrucao", length = 30)
    private String grauInstrucao;

    @Column(length = 255)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String celular;

    // Endereço
    @Column(length = 10)
    private String cep;

    @Column(length = 255)
    private String logradouro;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;

    // Dados funcionais
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_salarial_id")
    private NivelSalarial nivelSalarial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_vinculo_id", nullable = false)
    private TipoVinculo tipoVinculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_id")
    private Orgao orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_posse")
    private LocalDate dataPosse;

    @Column(name = "data_exercicio")
    private LocalDate dataExercicio;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Column(name = "motivo_demissao", length = 255)
    private String motivoDemissao;

    // Dados bancários
    @Column(name = "banco_codigo", length = 10)
    private String bancoCodigo;

    @Column(name = "banco_nome", length = 100)
    private String bancoNome;

    @Column(length = 20)
    private String agencia;

    @Column(length = 30)
    private String conta;

    @Column(name = "tipo_conta", length = 20)
    private String tipoConta;

    // Dados previdenciários
    @Column(name = "regime_previdencia", nullable = false, length = 10)
    @Builder.Default
    private String regimePrevidencia = "RGPS";

    @Column(name = "numero_previdencia", length = 20)
    private String numeroPrevidencia;

    // Status
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String situacao = "ATIVO";

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

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
