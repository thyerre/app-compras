package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 2)
    private String sigla;

    @Column(nullable = false, length = 100)
    private String nome;
}
