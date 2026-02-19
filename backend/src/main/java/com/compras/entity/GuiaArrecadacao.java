package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "guias_arrecadacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuiaArrecadacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_guia", nullable = false, unique = true, length = 30)
    private String numeroGuia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_tributario_id", nullable = false)
    private LancamentoTributario lancamentoTributario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

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

    @Column(name = "codigo_barras", length = 60)
    private String codigoBarras;

    @Column(name = "linha_digitavel", length = 60)
    private String linhaDigitavel;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "EMITIDA";

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
