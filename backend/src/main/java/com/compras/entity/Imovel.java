package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "imoveis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inscricao_imobiliaria", nullable = false, unique = true, length = 30)
    private String inscricaoImobiliaria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuinte_id", nullable = false)
    private Contribuinte contribuinte;

    @Column(name = "tipo_imovel", nullable = false, length = 20)
    private String tipoImovel;

    @Column(nullable = false, length = 255)
    private String logradouro;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(nullable = false, length = 100)
    private String bairro;

    @Column(length = 10)
    private String cep;

    @Column(name = "area_terreno", precision = 12, scale = 2)
    private BigDecimal areaTerreno;

    @Column(name = "area_construida", precision = 12, scale = 2)
    private BigDecimal areaConstruida;

    @Column(name = "valor_venal", precision = 18, scale = 2)
    private BigDecimal valorVenal;

    @Column(length = 50)
    private String uso;

    @Column(length = 10)
    private String setor;

    @Column(length = 10)
    private String quadra;

    @Column(length = 10)
    private String lote;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
