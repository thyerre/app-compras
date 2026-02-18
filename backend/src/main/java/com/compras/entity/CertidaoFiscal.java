package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certidoes_fiscais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertidaoFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(length = 100)
    private String numero;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "arquivo_nome", length = 255)
    private String arquivoNome;

    @Column(name = "arquivo_path", length = 500)
    private String arquivoPath;

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
