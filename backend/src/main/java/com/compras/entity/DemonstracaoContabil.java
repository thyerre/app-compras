package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demonstracoes_contabeis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemonstracaoContabil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(nullable = false, length = 5)
    private String tipo;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDate periodoFim;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "RASCUNHO";

    @Column(name = "data_geracao")
    private LocalDateTime dataGeracao;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column(length = 200)
    private String responsavel;

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
