package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.DependenteService;
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
@RequestMapping("/api/rh/dependentes")
@RequiredArgsConstructor
public class DependenteController {

    private final DependenteService dependenteService;

    @GetMapping
    public ResponseEntity<Page<DependenteListDTO>> findAll(
            @RequestParam(required = false) Long servidorId,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String parentesco,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(dependenteService.findAll(servidorId, nome, parentesco, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DependenteResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dependenteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DependenteResponseDTO> create(@Valid @RequestBody DependenteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dependenteService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DependenteResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody DependenteRequestDTO dto) {
        return ResponseEntity.ok(dependenteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dependenteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
