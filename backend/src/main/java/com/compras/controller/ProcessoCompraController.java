package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ProcessoCompraService;
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
@RequestMapping("/api/compras/processos")
@RequiredArgsConstructor
public class ProcessoCompraController {

    private final ProcessoCompraService processoService;

    @GetMapping
    public ResponseEntity<Page<ProcessoCompraListDTO>> findAll(
            @RequestParam(required = false) String numeroProcesso,
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) Integer modalidadeId,
            @RequestParam(required = false) Integer statusId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(processoService.findAll(
                numeroProcesso, exercicio, modalidadeId, statusId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoCompraResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(processoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProcessoCompraResponseDTO> create(
            @Valid @RequestBody ProcessoCompraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(processoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessoCompraResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ProcessoCompraRequestDTO dto) {
        return ResponseEntity.ok(processoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        processoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
