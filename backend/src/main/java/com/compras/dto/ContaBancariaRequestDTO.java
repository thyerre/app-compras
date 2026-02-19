package com.compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaBancariaRequestDTO {

    @NotBlank(message = "O código do banco é obrigatório")
    @Size(max = 10)
    private String bancoCodigo;

    @NotBlank(message = "O nome do banco é obrigatório")
    @Size(max = 100)
    private String bancoNome;

    @NotBlank(message = "A agência é obrigatória")
    @Size(max = 20)
    private String agencia;

    @NotBlank(message = "O número da conta é obrigatório")
    @Size(max = 30)
    private String numeroConta;

    @Size(max = 5)
    private String digito;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255)
    private String descricao;

    private String tipo;
    private Integer fonteRecursoId;
    private BigDecimal saldoAtual;
    private Boolean ativo;
}
