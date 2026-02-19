package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "atividades_economicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtividadeEconomica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @Column(nullable = false, length = 10)
    private String cnae;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(name = "tipo_atividade", nullable = false, length = 20)
    private String tipoAtividade;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_encerramento")
    private LocalDate dataEncerramento;

    @Column(name = "alvara_numero", length = 50)
    private String alvaraNumero;

    @Column(name = "alvara_validade")
    private LocalDate alvaraValidade;

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
