package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "processo_itens", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"processo_id", "numero_item"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_id", nullable = false)
    private ProcessoCompra processo;

    @Column(name = "numero_item", nullable = false)
    private Integer numeroItem;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "unidade_medida", nullable = false, length = 50)
    private String unidadeMedida;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario_estimado", precision = 18, scale = 4)
    private BigDecimal valorUnitarioEstimado;

    @Column(name = "valor_total_estimado", precision = 18, scale = 2)
    private BigDecimal valorTotalEstimado;

    @Column(name = "valor_unitario_homologado", precision = 18, scale = 4)
    private BigDecimal valorUnitarioHomologado;

    @Column(name = "valor_total_homologado", precision = 18, scale = 2)
    private BigDecimal valorTotalHomologado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_vencedor_id")
    private Fornecedor fornecedorVencedor;

    @Column(length = 255)
    private String marca;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String situacao = "PENDENTE";

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
