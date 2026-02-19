package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.RestosAPagarService;
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
@RequestMapping("/api/contabilidade/restos-a-pagar")
@RequiredArgsConstructor
public class RestosAPagarController {

    private final RestosAPagarService restosAPagarService;

    @GetMapping
    public ResponseEntity<Page<RestosAPagarListDTO>> findAll(
            @RequestParam(required = false) Integer exercicioOrigem,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "dataInscricao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(restosAPagarService.findAll(exercicioOrigem, tipo, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestosAPagarResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(restosAPagarService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RestosAPagarResponseDTO> create(@Valid @RequestBody RestosAPagarRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restosAPagarService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestosAPagarResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody RestosAPagarRequestDTO dto) {
        return ResponseEntity.ok(restosAPagarService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restosAPagarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
