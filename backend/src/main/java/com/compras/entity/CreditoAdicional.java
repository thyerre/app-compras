package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "creditos_adicionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditoAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(name = "numero_decreto", length = 50)
    private String numeroDecreto;

    @Column(name = "data_decreto")
    private LocalDate dataDecreto;

    @Column(name = "numero_lei", length = 50)
    private String numeroLei;

    @Column(name = "data_lei")
    private LocalDate dataLei;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dotacao_id", nullable = false)
    private DotacaoOrcamentaria dotacao;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "fonte_anulacao", columnDefinition = "TEXT")
    private String fonteAnulacao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "VIGENTE";

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
