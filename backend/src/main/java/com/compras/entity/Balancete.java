package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balancetes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balancete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(nullable = false)
    private Integer mes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_conta_id", nullable = false)
    private PlanoContas planoConta;

    @Column(name = "saldo_anterior", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal saldoAnterior = BigDecimal.ZERO;

    @Column(name = "total_debitos", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalDebitos = BigDecimal.ZERO;

    @Column(name = "total_creditos", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalCreditos = BigDecimal.ZERO;

    @Column(name = "saldo_atual", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal saldoAtual = BigDecimal.ZERO;

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
