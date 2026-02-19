package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_vinculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, length = 30)
    private String regime;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
