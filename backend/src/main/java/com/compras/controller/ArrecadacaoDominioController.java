package com.compras.controller;

import com.compras.dto.CargoListDTO;
import com.compras.dto.TributoDTO;
import com.compras.dto.TipoVinculoDTO;
import com.compras.service.ArrecadacaoDominioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/arrecadacao/dominios")
@RequiredArgsConstructor
public class ArrecadacaoDominioController {

    private final ArrecadacaoDominioService dominioService;

    @GetMapping("/tributos")
    public ResponseEntity<List<TributoDTO>> getTributos() {
        return ResponseEntity.ok(dominioService.getTributos());
    }

    @GetMapping("/tipos-vinculo")
    public ResponseEntity<List<TipoVinculoDTO>> getTiposVinculo() {
        return ResponseEntity.ok(dominioService.getTiposVinculo());
    }

    @GetMapping("/cargos")
    public ResponseEntity<List<CargoListDTO>> getCargos() {
        return ResponseEntity.ok(dominioService.getCargos());
    }
}
