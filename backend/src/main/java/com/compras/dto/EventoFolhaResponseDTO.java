package com.compras.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoFolhaResponseDTO {

    private Integer id;
    private String codigo;
    private String descricao;
    private String tipo;
    private Boolean incidenciaInss;
    private Boolean incidenciaIrrf;
    private Boolean incidenciaFgts;
    private Boolean automatico;
    private String formula;
    private BigDecimal percentual;
    private BigDecimal valorFixo;
    private String tipoCalculo;
    private Boolean aplicaMensal;
    private Boolean aplicaFerias;
    private Boolean aplica13;
    private Boolean aplicaRescisao;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
