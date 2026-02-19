package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dependentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dependente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servidor_id", nullable = false)
    private Servidor servidor;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 14)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 30)
    private String parentesco;

    @Column(length = 1)
    private String sexo;

    @Column(name = "dependente_ir", nullable = false)
    @Builder.Default
    private Boolean dependenteIr = false;

    @Column(name = "dependente_salario_familia", nullable = false)
    @Builder.Default
    private Boolean dependenteSalarioFamilia = false;

    @Column(name = "dependente_plano_saude", nullable = false)
    @Builder.Default
    private Boolean dependentePlanoSaude = false;

    @Column(name = "pensao_alimenticia", nullable = false)
    @Builder.Default
    private Boolean pensaoAlimenticia = false;

    @Column(name = "percentual_pensao", precision = 5, scale = 2)
    private BigDecimal percentualPensao;

    @Column(name = "data_inicio_dependencia")
    private LocalDate dataInicioDependencia;

    @Column(name = "data_fim_dependencia")
    private LocalDate dataFimDependencia;

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
