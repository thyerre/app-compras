package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "processo_participantes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"processo_id", "fornecedor_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_id", nullable = false)
    private ProcessoCompra processo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String situacao = "HABILITADO";

    @Column(name = "data_habilitacao")
    private LocalDate dataHabilitacao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "motivo_inabilitacao", columnDefinition = "TEXT")
    private String motivoInabilitacao;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
