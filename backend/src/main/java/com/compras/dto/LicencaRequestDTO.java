package com.compras.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicencaRequestDTO {

    @NotNull(message = "O servidor é obrigatório")
    private Long servidorId;

    @NotBlank(message = "O tipo de licença é obrigatório")
    @Size(max = 50)
    private String tipoLicenca;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;
    private Integer quantidadeDias;
    private Boolean remunerada;

    @Size(max = 10)
    private String cid;

    @Size(max = 255)
    private String medicoNome;

    @Size(max = 20)
    private String medicoCrm;

    @Size(max = 50)
    private String numeroDocumento;

    private String observacoes;
}
