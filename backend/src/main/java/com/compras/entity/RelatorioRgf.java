package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorios_rgf")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioRgf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(nullable = false)
    private Integer quadrimestre;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDate periodoFim;

    @Column(name = "receita_corrente_liquida", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal receitaCorrenteLiquida = BigDecimal.ZERO;

    @Column(name = "despesa_pessoal_executivo", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaPessoalExecutivo = BigDecimal.ZERO;

    @Column(name = "despesa_pessoal_legislativo", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaPessoalLegislativo = BigDecimal.ZERO;

    @Column(name = "despesa_pessoal_total", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaPessoalTotal = BigDecimal.ZERO;

    @Column(name = "percentual_pessoal_executivo", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal percentualPessoalExecutivo = BigDecimal.ZERO;

    @Column(name = "percentual_pessoal_legislativo", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal percentualPessoalLegislativo = BigDecimal.ZERO;

    @Column(name = "percentual_pessoal_total", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal percentualPessoalTotal = BigDecimal.ZERO;

    @Column(name = "limite_maximo", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal limiteMaximo = new BigDecimal("60.00");

    @Column(name = "limite_prudencial", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal limitePrudencial = new BigDecimal("57.00");

    @Column(name = "limite_alerta", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal limiteAlerta = new BigDecimal("54.00");

    @Column(name = "divida_consolidada", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal dividaConsolidada = BigDecimal.ZERO;

    @Column(name = "limite_divida", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal limiteDivida = BigDecimal.ZERO;

    @Column(name = "percentual_divida", precision = 8, scale = 4, nullable = false)
    @Builder.Default
    private BigDecimal percentualDivida = BigDecimal.ZERO;

    @Column(name = "disponibilidade_caixa", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal disponibilidadeCaixa = BigDecimal.ZERO;

    @Column(name = "obrigacoes_financeiras", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal obrigacoesFinanceiras = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "RASCUNHO";

    @Column(name = "data_geracao")
    private LocalDateTime dataGeracao;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column(name = "responsavel_nome", length = 200)
    private String responsavelNome;

    @Column(name = "responsavel_cargo", length = 100)
    private String responsavelCargo;

    @Column(name = "contador_nome", length = 200)
    private String contadorNome;

    @Column(name = "contador_crc", length = 30)
    private String contadorCrc;

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
