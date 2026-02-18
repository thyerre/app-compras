package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dotacoes_orcamentarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotacaoOrcamentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loa_id", nullable = false)
    private Loa loa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_id", nullable = false)
    private Orgao orgao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_id", nullable = false)
    private Funcao funcao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subfuncao_id", nullable = false)
    private Subfuncao subfuncao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acao_id", nullable = false)
    private Acao acao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "natureza_despesa_id", nullable = false)
    private NaturezaDespesa naturezaDespesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fonte_recurso_id", nullable = false)
    private FonteRecurso fonteRecurso;

    @Column(name = "valor_inicial", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorInicial = BigDecimal.ZERO;

    @Column(name = "valor_suplementado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorSuplementado = BigDecimal.ZERO;

    @Column(name = "valor_anulado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorAnulado = BigDecimal.ZERO;

    @Column(name = "valor_empenhado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorEmpenhado = BigDecimal.ZERO;

    @Column(name = "valor_liquidado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorLiquidado = BigDecimal.ZERO;

    @Column(name = "valor_pago", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String descricao;

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

    /** Saldo disponível para empenho */
    @Transient
    public BigDecimal getSaldoDisponivel() {
        return valorInicial
                .add(valorSuplementado)
                .subtract(valorAnulado)
                .subtract(valorEmpenhado);
    }
}
