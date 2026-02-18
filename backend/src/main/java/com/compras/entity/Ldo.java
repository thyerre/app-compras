package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ldos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ldo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ppa_id", nullable = false)
    private Ppa ppa;

    @Column(nullable = false, unique = true)
    private Integer exercicio;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "lei_numero", length = 50)
    private String leiNumero;

    @Column(name = "lei_data")
    private LocalDate leiData;

    @Column(name = "meta_fiscal_receita", precision = 18, scale = 2)
    private BigDecimal metaFiscalReceita;

    @Column(name = "meta_fiscal_despesa", precision = 18, scale = 2)
    private BigDecimal metaFiscalDespesa;

    @Column(name = "meta_resultado_primario", precision = 18, scale = 2)
    private BigDecimal metaResultadoPrimario;

    @Column(name = "meta_resultado_nominal", precision = 18, scale = 2)
    private BigDecimal metaResultadoNominal;

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

    @OneToMany(mappedBy = "ldo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LdoPrioridade> prioridades = new ArrayList<>();

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
