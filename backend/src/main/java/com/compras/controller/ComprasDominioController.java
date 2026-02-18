package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ComprasDominioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras/dominios")
@RequiredArgsConstructor
public class ComprasDominioController {

    private final ComprasDominioService dominioService;

    @GetMapping("/modalidades")
    public ResponseEntity<List<ModalidadeLicitacaoDTO>> findAllModalidades() {
        return ResponseEntity.ok(dominioService.findAllModalidades());
    }

    @GetMapping("/tipos-julgamento")
    public ResponseEntity<List<TipoJulgamentoDTO>> findAllTiposJulgamento() {
        return ResponseEntity.ok(dominioService.findAllTiposJulgamento());
    }

    @GetMapping("/status-processo")
    public ResponseEntity<List<StatusProcessoDTO>> findAllStatusProcesso() {
        return ResponseEntity.ok(dominioService.findAllStatusProcesso());
    }
}
