package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.LoaService;
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
@RequestMapping("/api/planejamento/loas")
@RequiredArgsConstructor
public class LoaController {

    private final LoaService loaService;

    @GetMapping
    public ResponseEntity<Page<LoaListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "exercicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(loaService.findAll(exercicio, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(loaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LoaResponseDTO> create(@Valid @RequestBody LoaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody LoaRequestDTO dto) {
        return ResponseEntity.ok(loaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
