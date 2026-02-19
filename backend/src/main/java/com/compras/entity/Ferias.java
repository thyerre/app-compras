package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ferias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ferias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servidor_id", nullable = false)
    private Servidor servidor;

    @Column(name = "periodo_aquisitivo_inicio", nullable = false)
    private LocalDate periodoAquisitivoInicio;

    @Column(name = "periodo_aquisitivo_fim", nullable = false)
    private LocalDate periodoAquisitivoFim;

    @Column(name = "periodo_gozo_inicio")
    private LocalDate periodoGozoInicio;

    @Column(name = "periodo_gozo_fim")
    private LocalDate periodoGozoFim;

    @Column(name = "dias_gozo")
    private Integer diasGozo;

    @Column(name = "dias_abono")
    @Builder.Default
    private Integer diasAbono = 0;

    @Builder.Default
    private Integer parcelas = 1;

    @Column(name = "valor_ferias", precision = 12, scale = 2)
    private BigDecimal valorFerias;

    @Column(name = "valor_abono", precision = 12, scale = 2)
    private BigDecimal valorAbono;

    @Column(name = "valor_13_ferias", precision = 12, scale = 2)
    private BigDecimal valor13Ferias;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDENTE";

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
