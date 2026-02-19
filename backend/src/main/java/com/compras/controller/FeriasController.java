package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.FeriasService;
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
@RequestMapping("/api/rh/ferias")
@RequiredArgsConstructor
public class FeriasController {

    private final FeriasService feriasService;

    @GetMapping
    public ResponseEntity<Page<FeriasListDTO>> findAll(
            @RequestParam(required = false) Long servidorId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "periodoAquisitivoInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(feriasService.findAll(servidorId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeriasResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(feriasService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FeriasResponseDTO> create(@Valid @RequestBody FeriasRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feriasService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeriasResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody FeriasRequestDTO dto) {
        return ResponseEntity.ok(feriasService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feriasService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
