package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consignados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servidor_id", nullable = false)
    private Servidor servidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_folha_id")
    private EventoFolha eventoFolha;

    @Column(nullable = false, length = 255)
    private String consignataria;

    @Column(length = 50)
    private String contrato;

    @Column(name = "parcela_atual", nullable = false)
    @Builder.Default
    private Integer parcelaAtual = 0;

    @Column(name = "parcela_total", nullable = false)
    private Integer parcelaTotal;

    @Column(name = "valor_parcela", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "valor_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ATIVO";

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
