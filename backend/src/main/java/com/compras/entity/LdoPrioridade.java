package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ldo_prioridades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ldo_id", "acao_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LdoPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ldo_id", nullable = false)
    private Ldo ldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acao_id", nullable = false)
    private Acao acao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id")
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @Column(name = "valor_estimado", precision = 18, scale = 2)
    private BigDecimal valorEstimado;

    @Column(name = "meta_fisica", precision = 18, scale = 2)
    private BigDecimal metaFisica;

    @Column(length = 500)
    private String meta;

    private Integer prioridade;

    @Column(columnDefinition = "TEXT")
    private String justificativa;
}
