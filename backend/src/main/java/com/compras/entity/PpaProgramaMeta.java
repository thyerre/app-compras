package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ppa_programa_metas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ppa_programa_id", "exercicio"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PpaProgramaMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ppa_programa_id", nullable = false)
    private PpaPrograma ppaPrograma;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(name = "valor_previsto", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorPrevisto = BigDecimal.ZERO;

    @Column(name = "meta_fisica", precision = 18, scale = 2)
    private BigDecimal metaFisica;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
