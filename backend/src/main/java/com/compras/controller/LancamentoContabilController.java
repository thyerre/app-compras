package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.LancamentoContabilService;
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
@RequestMapping("/api/contabilidade/lancamentos")
@RequiredArgsConstructor
public class LancamentoContabilController {

    private final LancamentoContabilService lancamentoContabilService;

    @GetMapping
    public ResponseEntity<Page<LancamentoContabilListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String origem,
            @PageableDefault(size = 20, sort = "dataLancamento", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(lancamentoContabilService.findAll(exercicio, tipo, origem, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoContabilResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(lancamentoContabilService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LancamentoContabilResponseDTO> create(
            @Valid @RequestBody LancamentoContabilRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoContabilService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoContabilResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody LancamentoContabilRequestDTO dto) {
        return ResponseEntity.ok(lancamentoContabilService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lancamentoContabilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
