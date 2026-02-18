package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ldo_id", nullable = false)
    private Ldo ldo;

    @Column(nullable = false, unique = true)
    private Integer exercicio;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "lei_numero", length = 50)
    private String leiNumero;

    @Column(name = "lei_data")
    private LocalDate leiData;

    @Column(name = "valor_total_receita", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorTotalReceita = BigDecimal.ZERO;

    @Column(name = "valor_total_despesa", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorTotalDespesa = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ELABORACAO";

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

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

    /** Alias para leiData usado nos DTOs */
    @Transient
    public LocalDate getDataAprovacao() {
        return this.leiData;
    }
}
