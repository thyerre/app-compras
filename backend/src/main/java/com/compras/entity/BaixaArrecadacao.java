package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "baixas_arrecadacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaixaArrecadacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guia_arrecadacao_id", nullable = false)
    private GuiaArrecadacao guiaArrecadacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_arrecadador_id")
    private AgenteArrecadador agenteArrecadador;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "data_credito")
    private LocalDate dataCredito;

    @Column(name = "valor_pago", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "valor_juros", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorJuros = BigDecimal.ZERO;

    @Column(name = "valor_multa", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorMulta = BigDecimal.ZERO;

    @Column(name = "valor_desconto", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "tipo_baixa", nullable = false, length = 20)
    private String tipoBaixa;

    @Column(length = 100)
    private String autenticacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_orcamentaria_id")
    private ReceitaOrcamentaria receitaOrcamentaria;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
