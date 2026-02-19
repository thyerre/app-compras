package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcelamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcelamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_parcelamento", nullable = false, unique = true, length = 20)
    private String numeroParcelamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "divida_ativa_id")
    private DividaAtiva dividaAtiva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_tributario_id")
    private LancamentoTributario lancamentoTributario;

    @Column(name = "data_parcelamento", nullable = false)
    private LocalDate dataParcelamento;

    @Column(name = "valor_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "quantidade_parcelas", nullable = false)
    private Integer quantidadeParcelas;

    @Column(name = "valor_entrada", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorEntrada = BigDecimal.ZERO;

    @Column(name = "taxa_juros", nullable = false, precision = 8, scale = 4)
    @Builder.Default
    private BigDecimal taxaJuros = BigDecimal.ZERO;

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
