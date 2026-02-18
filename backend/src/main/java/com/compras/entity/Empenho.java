package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "empenhos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empenho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_empenho", nullable = false, unique = true, length = 30)
    private String numeroEmpenho;

    @Column(nullable = false)
    private Integer exercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_id", nullable = false)
    private ProcessoCompra processo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dotacao_id", nullable = false)
    private DotacaoOrcamentaria dotacao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "valor_liquidado", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valorLiquidado = BigDecimal.ZERO;

    @Column(name = "valor_pago", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Column(name = "data_empenho", nullable = false)
    private LocalDate dataEmpenho;

    @Column(name = "data_liquidacao")
    private LocalDate dataLiquidacao;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "tipo", nullable = false, length = 30)
    @Builder.Default
    private String tipoEmpenho = "ORDINARIO";

    @Column(length = 20)
    @Builder.Default
    private String status = "ATIVO";

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
