package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "receita_orcamentaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceitaOrcamentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loa_id", nullable = false)
    private Loa loa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_prevista_id")
    private ReceitaPrevista receitaPrevista;

    @Column(name = "codigo_receita", nullable = false, length = 20)
    private String codigoReceita;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(name = "categoria_economica", nullable = false, length = 1)
    private String categoriaEconomica;

    @Column(length = 1)
    private String origem;

    @Column(length = 2)
    private String especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fonte_recurso_id")
    private FonteRecurso fonteRecurso;

    @Column(name = "valor_previsto_inicial", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorPrevistoInicial = BigDecimal.ZERO;

    @Column(name = "valor_previsto_atualizado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorPrevistoAtualizado = BigDecimal.ZERO;

    @Column(name = "valor_lancado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorLancado = BigDecimal.ZERO;

    @Column(name = "valor_arrecadado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorArrecadado = BigDecimal.ZERO;

    @Column(name = "valor_recolhido", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorRecolhido = BigDecimal.ZERO;

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
