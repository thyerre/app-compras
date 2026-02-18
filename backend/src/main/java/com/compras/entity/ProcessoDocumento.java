package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "processo_documentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processo_id", nullable = false)
    private ProcessoCompra processo;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(name = "descricao", nullable = false, length = 255)
    private String nome;

    @Column(name = "arquivo_nome", length = 255)
    private String arquivoNome;

    @Column(name = "arquivo_path", length = 500)
    private String arquivoPath;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "data_documento")
    private LocalDate dataDocumento;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
