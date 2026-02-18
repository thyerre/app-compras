package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "receitas_previstas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceitaPrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loa_id", nullable = false)
    private Loa loa;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "categoria_economica", nullable = false, length = 1)
    private String categoriaEconomica;

    @Column(length = 1)
    private String origem;

    @Column(length = 2)
    private String especie;

    @Column(length = 2)
    private String desdobramento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fonte_recurso_id")
    private FonteRecurso fonteRecurso;

    @Column(name = "valor_previsto", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorPrevisto = BigDecimal.ZERO;

    @Column(name = "valor_arrecadado", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorArrecadado = BigDecimal.ZERO;

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
