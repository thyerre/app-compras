package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.DemonstracaoContabilService;
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
@RequestMapping("/api/contabilidade/demonstracoes")
@RequiredArgsConstructor
public class DemonstracaoContabilController {

    private final DemonstracaoContabilService demonstracaoContabilService;

    @GetMapping
    public ResponseEntity<Page<DemonstracaoContabilListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "exercicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(demonstracaoContabilService.findAll(exercicio, tipo, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemonstracaoContabilResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(demonstracaoContabilService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DemonstracaoContabilResponseDTO> create(
            @Valid @RequestBody DemonstracaoContabilRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(demonstracaoContabilService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemonstracaoContabilResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody DemonstracaoContabilRequestDTO dto) {
        return ResponseEntity.ok(demonstracaoContabilService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        demonstracaoContabilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
