package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contas_bancarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banco_codigo", nullable = false, length = 10)
    private String bancoCodigo;

    @Column(name = "banco_nome", nullable = false, length = 100)
    private String bancoNome;

    @Column(nullable = false, length = 20)
    private String agencia;

    @Column(name = "numero_conta", nullable = false, length = 30)
    private String numeroConta;

    @Column(length = 5)
    private String digito;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String tipo = "MOVIMENTO";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fonte_recurso_id")
    private FonteRecurso fonteRecurso;

    @Column(name = "saldo_atual", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

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
