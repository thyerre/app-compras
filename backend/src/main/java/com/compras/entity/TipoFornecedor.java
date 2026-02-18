package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_fornecedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoFornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String descricao;
}
