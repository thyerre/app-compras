package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "conciliacoes_bancarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConciliacaoBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id", nullable = false)
    private ContaBancaria contaBancaria;

    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia;

    @Column(name = "ano_referencia", nullable = false)
    private Integer anoReferencia;

    @Column(name = "saldo_extrato", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoExtrato;

    @Column(name = "saldo_contabil", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoContabil;

    @Column(precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal diferenca = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDENTE";

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(length = 200)
    private String responsavel;

    @Column(name = "data_conciliacao")
    private LocalDate dataConciliacao;

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
