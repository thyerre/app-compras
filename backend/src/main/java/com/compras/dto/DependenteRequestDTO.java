package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DependenteRequestDTO {

    @NotNull(message = "O servidor é obrigatório")
    private Long servidorId;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255)
    private String nome;

    @Size(max = 14)
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate dataNascimento;

    @NotBlank(message = "O parentesco é obrigatório")
    @Size(max = 30)
    private String parentesco;

    @Size(max = 1)
    private String sexo;

    private Boolean dependenteIr;
    private Boolean dependenteSalarioFamilia;
    private Boolean dependentePlanoSaude;
    private Boolean pensaoAlimenticia;
    private BigDecimal percentualPensao;
    private LocalDate dataInicioDependencia;
    private LocalDate dataFimDependencia;
    private Boolean ativo;
}
