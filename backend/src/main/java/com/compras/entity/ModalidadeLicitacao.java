package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modalidades_licitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModalidadeLicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descricao", nullable = false, unique = true, length = 100)
    private String nome;

    @Transient
    private String sigla;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
