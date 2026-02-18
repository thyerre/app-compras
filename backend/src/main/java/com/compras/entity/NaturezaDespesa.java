package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "naturezas_despesa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaturezaDespesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 15)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "categoria_economica", nullable = false, length = 1)
    private String categoriaEconomica;

    @Column(name = "grupo_despesa", nullable = false, length = 1)
    private String grupoDespesa;

    @Column(nullable = false, length = 2)
    private String modalidade;

    @Column(length = 2)
    private String elemento;

    @Column(length = 2)
    private String subelemento;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
