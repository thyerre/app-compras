package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lancamentos_contabeis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoContabil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer exercicio;

    @Column(nullable = false, length = 30)
    private String numero;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String tipo = "NORMAL";

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String origem = "MANUAL";

    @Column(nullable = false, columnDefinition = "TEXT")
    private String historico;

    @Column(name = "valor_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "documento_ref", length = 100)
    private String documentoRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empenho_id")
    private Empenho empenho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidacao_id")
    private Liquidacao liquidacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "EFETIVADO";

    @OneToMany(mappedBy = "lancamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LancamentoItem> itens = new ArrayList<>();

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
