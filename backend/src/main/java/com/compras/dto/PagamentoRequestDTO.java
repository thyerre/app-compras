package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoRequestDTO {

    @NotNull(message = "A liquidação é obrigatória")
    private Long liquidacaoId;

    @NotBlank(message = "O número do pagamento é obrigatório")
    @Size(max = 30)
    private String numeroPagamento;

    @NotNull(message = "A data do pagamento é obrigatória")
    private LocalDate dataPagamento;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;

    private Long contaBancariaId;
    private String formaPagamento;

    @Size(max = 50)
    private String documentoBancario;

    private String descricao;
    private String status;
}
