package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "subfuncoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subfuncao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 5)
    private String codigo;

    @Column(name = "descricao", nullable = false, length = 255)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
