package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certidoes_tributarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertidaoTributaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_certidao", nullable = false, unique = true, length = 30)
    private String numeroCertidao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(length = 255)
    private String finalidade;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(name = "codigo_verificacao", length = 50)
    private String codigoVerificacao;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
