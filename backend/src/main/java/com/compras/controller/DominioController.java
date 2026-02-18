package com.compras.controller;

import com.compras.dto.ClassificacaoFornecedorDTO;
import com.compras.dto.EstadoDTO;
import com.compras.dto.MunicipioDTO;
import com.compras.dto.TipoFornecedorDTO;
import com.compras.service.DominioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dominios")
@RequiredArgsConstructor
public class DominioController {

    private final DominioService dominioService;

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoDTO>> findAllEstados() {
        return ResponseEntity.ok(dominioService.findAllEstados());
    }

    @GetMapping("/municipios")
    public ResponseEntity<List<MunicipioDTO>> findMunicipios(
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) String nome
    ) {
        if (estadoId != null) {
            return ResponseEntity.ok(dominioService.findMunicipiosByEstado(estadoId));
        }
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(dominioService.searchMunicipios(nome));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/tipos-fornecedor")
    public ResponseEntity<List<TipoFornecedorDTO>> findAllTiposFornecedor() {
        return ResponseEntity.ok(dominioService.findAllTiposFornecedor());
    }

    @GetMapping("/classificacoes-fornecedor")
    public ResponseEntity<List<ClassificacaoFornecedorDTO>> findAllClassificacoes() {
        return ResponseEntity.ok(dominioService.findAllClassificacoes());
    }
}
