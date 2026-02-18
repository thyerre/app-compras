package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_julgamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoJulgamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String descricao;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
