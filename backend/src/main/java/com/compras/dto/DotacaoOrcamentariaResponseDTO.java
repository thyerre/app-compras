package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotacaoOrcamentariaResponseDTO {
    private Long id;
    private LoaListDTO loa;
    private OrgaoDTO orgao;
    private UnidadeDTO unidade;
    private FuncaoDTO funcao;
    private SubfuncaoDTO subfuncao;
    private ProgramaListDTO programa;
    private AcaoListDTO acao;
    private NaturezaDespesaDTO naturezaDespesa;
    private FonteRecursoDTO fonteRecurso;
    private BigDecimal valorInicial;
    private BigDecimal valorSuplementado;
    private BigDecimal valorAnulado;
    private BigDecimal valorEmpenhado;
    private BigDecimal saldoDisponivel;
    private String descricao;
    private LocalDateTime createdAt;
}
