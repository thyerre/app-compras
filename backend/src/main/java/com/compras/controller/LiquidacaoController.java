package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.LiquidacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contabilidade/liquidacoes")
@RequiredArgsConstructor
public class LiquidacaoController {

    private final LiquidacaoService liquidacaoService;

    @GetMapping
    public ResponseEntity<Page<LiquidacaoListDTO>> findAll(
            @RequestParam(required = false) String numeroLiquidacao,
            @RequestParam(required = false) Long empenhoId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "dataLiquidacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(liquidacaoService.findAll(numeroLiquidacao, empenhoId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiquidacaoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(liquidacaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LiquidacaoResponseDTO> create(@Valid @RequestBody LiquidacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(liquidacaoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LiquidacaoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody LiquidacaoRequestDTO dto) {
        return ResponseEntity.ok(liquidacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        liquidacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
