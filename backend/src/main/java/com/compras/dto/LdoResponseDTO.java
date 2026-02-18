package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LdoResponseDTO {
    private Long id;
    private Integer exercicio;
    private PpaListDTO ppa;
    private String descricao;
    private String status;
    private LocalDate dataAprovacao;
    private String observacoes;
    private LocalDateTime createdAt;
    private List<LdoPrioridadeDTO> prioridades;
}
