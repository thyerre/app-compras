package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.DividaPublicaService;
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
@RequestMapping("/api/contabilidade/divida-publica")
@RequiredArgsConstructor
public class DividaPublicaController {

    private final DividaPublicaService dividaPublicaService;

    @GetMapping
    public ResponseEntity<Page<DividaPublicaListDTO>> findAll(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "dataContratacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(dividaPublicaService.findAll(tipo, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DividaPublicaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dividaPublicaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DividaPublicaResponseDTO> create(@Valid @RequestBody DividaPublicaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dividaPublicaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DividaPublicaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody DividaPublicaRequestDTO dto) {
        return ResponseEntity.ok(dividaPublicaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dividaPublicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
