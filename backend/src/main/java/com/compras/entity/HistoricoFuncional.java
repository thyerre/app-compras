package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_funcional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoFuncional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servidor_id", nullable = false)
    private Servidor servidor;

    @Column(name = "tipo_evento", nullable = false, length = 50)
    private String tipoEvento;

    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    @Column(name = "numero_ato", length = 50)
    private String numeroAto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_anterior_id")
    private Cargo cargoAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_novo_id")
    private Cargo cargoNovo;

    @Column(name = "salario_anterior", precision = 12, scale = 2)
    private BigDecimal salarioAnterior;

    @Column(name = "salario_novo", precision = 12, scale = 2)
    private BigDecimal salarioNovo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_anterior_id")
    private Orgao orgaoAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgao_novo_id")
    private Orgao orgaoNovo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_anterior_id")
    private Unidade unidadeAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_novo_id")
    private Unidade unidadeNovo;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
