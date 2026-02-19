package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoContabilRequestDTO {

    @NotNull(message = "O exercício é obrigatório")
    private Integer exercicio;

    @NotBlank(message = "O número é obrigatório")
    private String numero;

    @NotNull(message = "A data do lançamento é obrigatória")
    private LocalDate dataLancamento;

    private String tipo;
    private String origem;

    @NotBlank(message = "O histórico é obrigatório")
    private String historico;

    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;

    private String documentoRef;
    private Long empenhoId;
    private Long liquidacaoId;
    private Long pagamentoId;
    private String status;

    @NotNull(message = "Os itens do lançamento são obrigatórios")
    private List<LancamentoItemDTO> itens;
}
