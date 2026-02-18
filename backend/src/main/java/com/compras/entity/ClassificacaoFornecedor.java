package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classificacoes_fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassificacaoFornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String descricao;
}
