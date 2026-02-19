package com.compras.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicencaResponseDTO {

    private Long id;
    private Long servidorId;
    private String servidorNome;
    private String tipoLicenca;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer quantidadeDias;
    private Boolean remunerada;
    private String cid;
    private String medicoNome;
    private String medicoCrm;
    private String numeroDocumento;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
