package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorios_rreo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioRreo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(nullable = false)
    private Integer bimestre;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDate periodoFim;

    @Column(name = "receita_prevista", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal receitaPrevista = BigDecimal.ZERO;

    @Column(name = "receita_realizada", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal receitaRealizada = BigDecimal.ZERO;

    @Column(name = "despesa_fixada", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaFixada = BigDecimal.ZERO;

    @Column(name = "despesa_empenhada", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaEmpenhada = BigDecimal.ZERO;

    @Column(name = "despesa_liquidada", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaLiquidada = BigDecimal.ZERO;

    @Column(name = "despesa_paga", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal despesaPaga = BigDecimal.ZERO;

    @Column(name = "resultado_primario", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal resultadoPrimario = BigDecimal.ZERO;

    @Column(name = "resultado_nominal", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal resultadoNominal = BigDecimal.ZERO;

    @Column(name = "receita_corrente_liquida", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal receitaCorrenteLiquida = BigDecimal.ZERO;

    @Column(name = "restos_pagar_processados", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal restosPagarProcessados = BigDecimal.ZERO;

    @Column(name = "restos_pagar_nao_proc", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal restosPagarNaoProc = BigDecimal.ZERO;

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
