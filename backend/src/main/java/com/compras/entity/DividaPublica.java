package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "divida_publica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividaPublica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, length = 300)
    private String credor;

    @Column(name = "numero_contrato", length = 50)
    private String numeroContrato;

    @Column(name = "data_contratacao", nullable = false)
    private LocalDate dataContratacao;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "valor_original", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorOriginal;

    @Column(name = "saldo_devedor", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoDevedor;

    @Column(name = "taxa_juros", precision = 8, scale = 4)
    private BigDecimal taxaJuros;

    @Column(name = "indice_correcao", length = 50)
    private String indiceCorrecao;

    @Column(columnDefinition = "TEXT")
    private String finalidade;

    @Column(name = "lei_autorizativa", length = 100)
    private String leiAutorizativa;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "VIGENTE";

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
