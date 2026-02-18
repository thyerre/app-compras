package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "programas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(name = "descricao", nullable = false, length = 255)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String objetivo;

    @Column(name = "publico_alvo", columnDefinition = "TEXT")
    private String publicoAlvo;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String tipo = "TEMATICO";

    @Column(length = 255)
    private String indicador;

    @Column(name = "unidade_medida", length = 100)
    private String unidadeMedida;

    @Column(name = "meta_fisica", precision = 18, scale = 2)
    private BigDecimal metaFisica;

    @Column(name = "exercicio_inicio")
    private Integer exercicioInicio;

    @Column(name = "exercicio_fim")
    private Integer exercicioFim;

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
