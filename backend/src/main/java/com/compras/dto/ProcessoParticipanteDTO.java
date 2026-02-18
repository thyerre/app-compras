package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoParticipanteDTO {
    private Long id;
    private Long fornecedorId;
    private String fornecedorRazaoSocial;
    private String fornecedorCnpjCpf;
    private String situacao;
    private String motivoInabilitacao;
}
