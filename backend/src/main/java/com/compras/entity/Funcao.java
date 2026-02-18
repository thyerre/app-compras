package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funcoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 5)
    private String codigo;

    @Column(name = "descricao", nullable = false, length = 255)
    private String nome;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
