package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DependenteResponseDTO {

    private Long id;
    private Long servidorId;
    private String servidorNome;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String parentesco;
    private String sexo;
    private Boolean dependenteIr;
    private Boolean dependenteSalarioFamilia;
    private Boolean dependentePlanoSaude;
    private Boolean pensaoAlimenticia;
    private BigDecimal percentualPensao;
    private LocalDate dataInicioDependencia;
    private LocalDate dataFimDependencia;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
