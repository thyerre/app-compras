package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "acoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Acao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(name = "descricao", nullable = false, length = 255)
    private String nome;

    @Transient
    private String descricao;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String tipo = "ATIVIDADE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subfuncao_id")
    private Subfuncao subfuncao;

    @Column(length = 255)
    private String produto;

    @Column(name = "unidade_medida", length = 100)
    private String unidadeMedida;

    @Column(name = "meta_fisica", precision = 18, scale = 2)
    private BigDecimal metaFisica;

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
