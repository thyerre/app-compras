package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tributos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "fato_gerador", columnDefinition = "TEXT")
    private String fatoGerador;

    @Column(name = "aliquota_padrao", precision = 8, scale = 4)
    private BigDecimal aliquotaPadrao;

    @Column(name = "valor_fixo", precision = 18, scale = 2)
    private BigDecimal valorFixo;

    @Column(name = "receita_classificacao", length = 20)
    private String receitaClassificacao;

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
