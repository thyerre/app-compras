package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fontes_recurso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FonteRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(length = 100)
    private String grupo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
