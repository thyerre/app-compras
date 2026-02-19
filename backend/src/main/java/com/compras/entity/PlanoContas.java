package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "plano_contas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoContas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private Short classe;

    @Column(name = "natureza_saldo", nullable = false, length = 1)
    private String naturezaSaldo;

    @Column(nullable = false, length = 1)
    private String tipo;

    @Column(nullable = false)
    private Short nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PlanoContas parent;

    @Column(nullable = false)
    @Builder.Default
    private Boolean escrituravel = false;

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
