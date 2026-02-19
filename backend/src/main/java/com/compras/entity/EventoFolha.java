package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_folha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoFolha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(name = "incidencia_inss", nullable = false)
    @Builder.Default
    private Boolean incidenciaInss = false;

    @Column(name = "incidencia_irrf", nullable = false)
    @Builder.Default
    private Boolean incidenciaIrrf = false;

    @Column(name = "incidencia_fgts", nullable = false)
    @Builder.Default
    private Boolean incidenciaFgts = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean automatico = false;

    @Column(columnDefinition = "TEXT")
    private String formula;

    @Column(precision = 8, scale = 4)
    private BigDecimal percentual;

    @Column(name = "valor_fixo", precision = 12, scale = 2)
    private BigDecimal valorFixo;

    @Column(name = "tipo_calculo", length = 20)
    private String tipoCalculo;

    @Column(name = "aplica_mensal", nullable = false)
    @Builder.Default
    private Boolean aplicaMensal = true;

    @Column(name = "aplica_ferias", nullable = false)
    @Builder.Default
    private Boolean aplicaFerias = false;

    @Column(name = "aplica_13", nullable = false)
    @Builder.Default
    private Boolean aplica13 = false;

    @Column(name = "aplica_rescisao", nullable = false)
    @Builder.Default
    private Boolean aplicaRescisao = false;

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
