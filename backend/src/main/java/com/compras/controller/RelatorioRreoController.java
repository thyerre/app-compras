package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.RelatorioRreoService;
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
@RequestMapping("/api/contabilidade/rreo")
@RequiredArgsConstructor
public class RelatorioRreoController {

    private final RelatorioRreoService relatorioRreoService;

    @GetMapping
    public ResponseEntity<Page<RelatorioRreoListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) Integer bimestre,
            @PageableDefault(size = 20, sort = "exercicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(relatorioRreoService.findAll(exercicio, bimestre, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioRreoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioRreoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RelatorioRreoResponseDTO> create(@Valid @RequestBody RelatorioRreoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioRreoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RelatorioRreoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody RelatorioRreoRequestDTO dto) {
        return ResponseEntity.ok(relatorioRreoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        relatorioRreoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
