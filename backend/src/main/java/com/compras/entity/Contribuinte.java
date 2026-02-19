package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contribuintes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contribuinte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_pessoa", nullable = false, length = 2)
    private String tipoPessoa;

    @Column(name = "cpf_cnpj", nullable = false, unique = true, length = 18)
    private String cpfCnpj;

    @Column(name = "nome_razao_social", nullable = false, length = 255)
    private String nomeRazaoSocial;

    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;

    @Column(name = "inscricao_municipal", length = 20)
    private String inscricaoMunicipal;

    @Column(length = 255)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String celular;

    // Endereço
    @Column(length = 10)
    private String cep;

    @Column(length = 255)
    private String logradouro;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

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
