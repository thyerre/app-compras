package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "processo_propostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private ProcessoParticipante participante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ProcessoItem item;

    @Column(name = "valor_unitario", nullable = false, precision = 15, scale = 4)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(length = 255)
    private String marca;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacao;

    @Builder.Default
    @Column(name = "vencedor", nullable = false)
    private Boolean vencedor = false;

    private Integer classificacao;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
