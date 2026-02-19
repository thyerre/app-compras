package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cargos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(length = 10)
    private String cbo;

    @Column(nullable = false)
    @Builder.Default
    private Integer vagas = 0;

    @Column(name = "vagas_ocupadas", nullable = false)
    @Builder.Default
    private Integer vagasOcupadas = 0;

    @Column(name = "escolaridade_minima", length = 30)
    private String escolaridadeMinima;

    @Column(name = "carga_horaria_semanal")
    private Integer cargaHorariaSemanal;

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
