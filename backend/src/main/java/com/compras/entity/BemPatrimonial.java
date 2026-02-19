package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bens_patrimoniais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BemPatrimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_patrimonio", nullable = false, unique = true, length = 30)
    private String numeroPatrimonio;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(length = 50)
    private String categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_id", nullable = false)
    private Orgao orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @Column(name = "data_aquisicao", nullable = false)
    private LocalDate dataAquisicao;

    @Column(name = "valor_aquisicao", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorAquisicao;

    @Column(name = "valor_atual", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorAtual;

    @Column(name = "vida_util_meses")
    private Integer vidaUtilMeses;

    @Column(name = "valor_residual", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorResidual = BigDecimal.ZERO;

    @Column(name = "depreciacao_acumulada", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal depreciacaoAcumulada = BigDecimal.ZERO;

    @Column(length = 500)
    private String localizacao;

    @Column(length = 200)
    private String responsavel;

    @Column(name = "estado_conservacao", length = 30)
    @Builder.Default
    private String estadoConservacao = "BOM";

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String situacao = "ATIVO";

    @Column(name = "nota_fiscal", length = 50)
    private String notaFiscal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empenho_id")
    private Empenho empenho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_conta_id")
    private PlanoContas planoConta;

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
