package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ConciliacaoBancariaService;
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
@RequestMapping("/api/contabilidade/conciliacao-bancaria")
@RequiredArgsConstructor
public class ConciliacaoBancariaController {

    private final ConciliacaoBancariaService conciliacaoBancariaService;

    @GetMapping
    public ResponseEntity<Page<ConciliacaoBancariaListDTO>> findAll(
            @RequestParam(required = false) Integer anoReferencia,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "anoReferencia", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(conciliacaoBancariaService.findAll(anoReferencia, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConciliacaoBancariaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(conciliacaoBancariaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ConciliacaoBancariaResponseDTO> create(
            @Valid @RequestBody ConciliacaoBancariaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conciliacaoBancariaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConciliacaoBancariaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ConciliacaoBancariaRequestDTO dto) {
        return ResponseEntity.ok(conciliacaoBancariaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        conciliacaoBancariaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
