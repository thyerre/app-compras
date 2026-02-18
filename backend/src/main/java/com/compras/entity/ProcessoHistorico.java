package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "processo_historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_id", nullable = false)
    private ProcessoCompra processo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_anterior_id")
    private StatusProcesso statusAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_novo_id", nullable = false)
    private StatusProcesso statusNovo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "usuario_nome", length = 200)
    private String usuarioNome;

    @Column(name = "data_registro", nullable = false)
    @Builder.Default
    private LocalDateTime dataRegistro = LocalDateTime.now();
}
