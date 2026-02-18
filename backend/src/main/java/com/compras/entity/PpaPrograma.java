package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ppa_programas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ppa_id", "programa_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PpaPrograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ppa_id", nullable = false)
    private Ppa ppa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_id")
    private Orgao orgao;

    @Column(name = "valor_global", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal valorGlobal = BigDecimal.ZERO;

    @Column(name = "meta_fisica_global", precision = 18, scale = 2)
    private BigDecimal metaFisicaGlobal;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @OneToMany(mappedBy = "ppaPrograma", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PpaProgramaMeta> metas = new ArrayList<>();
}
