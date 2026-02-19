package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "niveis_salariais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelSalarial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @Column(nullable = false, length = 10)
    private String nivel;

    @Column(length = 10)
    private String classe;

    @Column(length = 10)
    private String referencia;

    @Column(name = "valor_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorBase;

    @Column(name = "vigencia_inicio", nullable = false)
    private LocalDate vigenciaInicio;

    @Column(name = "vigencia_fim")
    private LocalDate vigenciaFim;

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
}
