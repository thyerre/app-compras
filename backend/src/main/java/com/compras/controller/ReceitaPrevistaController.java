package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ReceitaPrevistaService;
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
@RequestMapping("/api/planejamento/receitas")
@RequiredArgsConstructor
public class ReceitaPrevistaController {

    private final ReceitaPrevistaService receitaService;

    @GetMapping
    public ResponseEntity<Page<ReceitaPrevistaListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @PageableDefault(size = 20, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(receitaService.findAll(exercicio, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaPrevistaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(receitaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReceitaPrevistaResponseDTO> create(
            @Valid @RequestBody ReceitaPrevistaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaPrevistaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ReceitaPrevistaRequestDTO dto) {
        return ResponseEntity.ok(receitaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        receitaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
