package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamento_itens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_id", nullable = false)
    private LancamentoContabil lancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_conta_id", nullable = false)
    private PlanoContas planoConta;

    @Column(nullable = false, length = 1)
    private String tipo;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "historico_item", columnDefinition = "TEXT")
    private String historicoItem;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
