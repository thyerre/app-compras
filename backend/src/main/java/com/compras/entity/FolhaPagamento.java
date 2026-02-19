package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "folhas_pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolhaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 7)
    private String competencia;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(name = "data_calculo")
    private LocalDateTime dataCalculo;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprovado_por")
    private Usuario aprovadoPor;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "total_proventos", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalProventos = BigDecimal.ZERO;

    @Column(name = "total_descontos", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalDescontos = BigDecimal.ZERO;

    @Column(name = "total_liquido", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalLiquido = BigDecimal.ZERO;

    @Column(name = "quantidade_servidores", nullable = false)
    @Builder.Default
    private Integer quantidadeServidores = 0;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ABERTA";

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
