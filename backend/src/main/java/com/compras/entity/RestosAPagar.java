package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "restos_a_pagar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestosAPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercicio_origem", nullable = false)
    private Integer exercicioOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empenho_id", nullable = false)
    private Empenho empenho;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(name = "valor_inscrito", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorInscrito;

    @Column(name = "valor_cancelado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorCancelado = BigDecimal.ZERO;

    @Column(name = "valor_liquidado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorLiquidado = BigDecimal.ZERO;

    @Column(name = "valor_pago", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDate dataInscricao;

    @Column(name = "data_cancelamento")
    private LocalDate dataCancelamento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "INSCRITO";

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
