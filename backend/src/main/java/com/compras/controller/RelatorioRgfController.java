package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.RelatorioRgfService;
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
@RequestMapping("/api/contabilidade/rgf")
@RequiredArgsConstructor
public class RelatorioRgfController {

    private final RelatorioRgfService relatorioRgfService;

    @GetMapping
    public ResponseEntity<Page<RelatorioRgfListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) Integer quadrimestre,
            @PageableDefault(size = 20, sort = "exercicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(relatorioRgfService.findAll(exercicio, quadrimestre, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioRgfResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioRgfService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RelatorioRgfResponseDTO> create(@Valid @RequestBody RelatorioRgfRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioRgfService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RelatorioRgfResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody RelatorioRgfRequestDTO dto) {
        return ResponseEntity.ok(relatorioRgfService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        relatorioRgfService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
