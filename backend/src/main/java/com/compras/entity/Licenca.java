package com.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "licencas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Licenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servidor_id", nullable = false)
    private Servidor servidor;

    @Column(name = "tipo_licenca", nullable = false, length = 50)
    private String tipoLicenca;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "quantidade_dias")
    private Integer quantidadeDias;

    @Column(nullable = false)
    @Builder.Default
    private Boolean remunerada = true;

    @Column(length = 10)
    private String cid;

    @Column(name = "medico_nome", length = 255)
    private String medicoNome;

    @Column(name = "medico_crm", length = 20)
    private String medicoCrm;

    @Column(name = "numero_documento", length = 50)
    private String numeroDocumento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

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
