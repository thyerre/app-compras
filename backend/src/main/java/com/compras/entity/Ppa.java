package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ppas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ano_inicio", "ano_fim"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ppa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @Column(name = "ano_fim", nullable = false)
    private Integer anoFim;

    @Column(name = "lei_numero", length = 50)
    private String leiNumero;

    @Column(name = "lei_data")
    private LocalDate leiData;

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

    @OneToMany(mappedBy = "ppa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PpaPrograma> programas = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
