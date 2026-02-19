package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ConsignadoService;
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
@RequestMapping("/api/rh/consignados")
@RequiredArgsConstructor
public class ConsignadoController {

    private final ConsignadoService consignadoService;

    @GetMapping
    public ResponseEntity<Page<ConsignadoListDTO>> findAll(
            @RequestParam(required = false) Long servidorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String consignataria,
            @PageableDefault(size = 20, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(consignadoService.findAll(servidorId, status, consignataria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsignadoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(consignadoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ConsignadoResponseDTO> create(@Valid @RequestBody ConsignadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consignadoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsignadoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ConsignadoRequestDTO dto) {
        return ResponseEntity.ok(consignadoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consignadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
