package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "municipios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "codigo_ibge", length = 10)
    private String codigoIbge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;
}
