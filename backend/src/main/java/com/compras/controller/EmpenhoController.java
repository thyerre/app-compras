package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.EmpenhoService;
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
@RequestMapping("/api/compras/empenhos")
@RequiredArgsConstructor
public class EmpenhoController {

    private final EmpenhoService empenhoService;

    @GetMapping
    public ResponseEntity<Page<EmpenhoListDTO>> findAll(
            @RequestParam(required = false) String numeroEmpenho,
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) Long processoId,
            @RequestParam(required = false) Long fornecedorId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "dataEmpenho", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(empenhoService.findAll(
                numeroEmpenho, exercicio, processoId, fornecedorId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpenhoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(empenhoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmpenhoResponseDTO> create(@Valid @RequestBody EmpenhoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empenhoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpenhoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody EmpenhoRequestDTO dto) {
        return ResponseEntity.ok(empenhoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empenhoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
