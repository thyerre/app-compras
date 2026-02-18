package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "status_processo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusProcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descricao", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(length = 7)
    private String cor;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
