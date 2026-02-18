package com.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {

    private Long id;
    private String label;
    private String icone;
    private String rota;
    private Integer ordem;
    private List<MenuDTO> children;
}
