package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "liquidacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liquidacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empenho_id", nullable = false)
    private Empenho empenho;

    @Column(name = "numero_liquidacao", nullable = false, length = 30)
    private String numeroLiquidacao;

    @Column(name = "data_liquidacao", nullable = false)
    private LocalDate dataLiquidacao;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "documento_tipo", length = 50)
    private String documentoTipo;

    @Column(name = "documento_numero", length = 50)
    private String documentoNumero;

    @Column(name = "documento_data")
    private LocalDate documentoData;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ATIVA";

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
