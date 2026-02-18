package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processos_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_processo", nullable = false, unique = true, length = 30)
    private String numeroProcesso;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String objeto;

    @Column(columnDefinition = "TEXT")
    private String justificativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modalidade_id", nullable = false)
    private ModalidadeLicitacao modalidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_julgamento_id")
    private TipoJulgamento tipoJulgamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusProcesso status;

    // Integração com orçamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dotacao_id")
    private DotacaoOrcamentaria dotacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_id")
    private Orgao orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    // Valores
    @Column(name = "valor_estimado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorEstimado = BigDecimal.ZERO;

    @Column(name = "valor_homologado", precision = 18, scale = 2)
    private BigDecimal valorHomologado;

    @Column(name = "valor_economizado", precision = 18, scale = 2)
    private BigDecimal valorEconomizado;

    // Datas
    @Column(name = "data_abertura")
    private LocalDate dataAbertura;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column(name = "data_encerramento")
    private LocalDate dataEncerramento;

    @Column(name = "data_homologacao")
    private LocalDate dataHomologacao;

    @Column(name = "data_adjudicacao")
    private LocalDate dataAdjudicacao;

    // Publicação
    @Column(name = "numero_edital", length = 50)
    private String numeroEdital;

    @Column(name = "diario_oficial", length = 255)
    private String diarioOficial;

    // Responsáveis
    @Column(name = "pregoeiro_nome", length = 200)
    private String pregoeiroNome;

    @Column(name = "pregoeiro_matricula", length = 50)
    private String pregoeiroMatricula;

    // Controle
    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProcessoItem> itens = new ArrayList<>();

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProcessoParticipante> participantes = new ArrayList<>();

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProcessoDocumento> documentos = new ArrayList<>();

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProcessoHistorico> historico = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
