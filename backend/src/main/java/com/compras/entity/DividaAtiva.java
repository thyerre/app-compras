package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "divida_ativa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividaAtiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_inscricao", nullable = false, unique = true, length = 20)
    private String numeroInscricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_tributario_id", nullable = false)
    private LancamentoTributario lancamentoTributario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDate dataInscricao;

    @Column(name = "valor_original", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorOriginal;

    @Column(name = "valor_atualizado", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorAtualizado;

    @Column(name = "valor_juros", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorJuros = BigDecimal.ZERO;

    @Column(name = "valor_multa", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(name = "fundamentacao_legal", columnDefinition = "TEXT")
    private String fundamentacaoLegal;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "INSCRITA";

    @Column(name = "data_ajuizamento")
    private LocalDate dataAjuizamento;

    @Column(name = "numero_processo_judicial", length = 30)
    private String numeroProcessoJudicial;

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
