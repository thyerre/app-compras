package com.compras.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DependenteListDTO {

    private Long id;
    private String servidorNome;
    private String servidorMatricula;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String parentesco;
    private Boolean ativo;
}
