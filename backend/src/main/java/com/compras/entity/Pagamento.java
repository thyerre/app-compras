package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidacao_id", nullable = false)
    private Liquidacao liquidacao;

    @Column(name = "numero_pagamento", nullable = false, length = 30)
    private String numeroPagamento;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id")
    private ContaBancaria contaBancaria;

    @Column(name = "forma_pagamento", nullable = false, length = 30)
    @Builder.Default
    private String formaPagamento = "TRANSFERENCIA";

    @Column(name = "documento_bancario", length = 50)
    private String documentoBancario;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "EFETIVADO";

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
