package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ReceitaOrcamentariaService;
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
@RequestMapping("/api/contabilidade/receita-orcamentaria")
@RequiredArgsConstructor
public class ReceitaOrcamentariaController {

    private final ReceitaOrcamentariaService receitaOrcamentariaService;

    @GetMapping
    public ResponseEntity<Page<ReceitaOrcamentariaListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) String codigoReceita,
            @PageableDefault(size = 20, sort = "codigoReceita", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(receitaOrcamentariaService.findAll(exercicio, codigoReceita, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaOrcamentariaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(receitaOrcamentariaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReceitaOrcamentariaResponseDTO> create(
            @Valid @RequestBody ReceitaOrcamentariaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaOrcamentariaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaOrcamentariaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ReceitaOrcamentariaRequestDTO dto) {
        return ResponseEntity.ok(receitaOrcamentariaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        receitaOrcamentariaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
