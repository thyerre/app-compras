package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamentos_tributarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoTributario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_lancamento", nullable = false, unique = true, length = 20)
    private String numeroLancamento;

    @Column(nullable = false)
    private Integer exercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tributo_id", nullable = false)
    private Tributo tributo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id")
    private Imovel imovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_economica_id")
    private AtividadeEconomica atividadeEconomica;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "base_calculo", precision = 18, scale = 2)
    private BigDecimal baseCalculo;

    @Column(precision = 8, scale = 4)
    private BigDecimal aliquota;

    @Column(name = "valor_principal", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorPrincipal;

    @Column(name = "valor_desconto", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_juros", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorJuros = BigDecimal.ZERO;

    @Column(name = "valor_multa", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(name = "valor_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ABERTO";

    @Column(columnDefinition = "TEXT")
    private String historico;

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
