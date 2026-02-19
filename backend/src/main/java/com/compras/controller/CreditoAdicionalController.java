package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.CreditoAdicionalService;
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
@RequestMapping("/api/contabilidade/creditos-adicionais")
@RequiredArgsConstructor
public class CreditoAdicionalController {

    private final CreditoAdicionalService creditoAdicionalService;

    @GetMapping
    public ResponseEntity<Page<CreditoAdicionalListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) String tipo,
            @PageableDefault(size = 20, sort = "dataDecreto", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(creditoAdicionalService.findAll(exercicio, tipo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditoAdicionalResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(creditoAdicionalService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CreditoAdicionalResponseDTO> create(
            @Valid @RequestBody CreditoAdicionalRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creditoAdicionalService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditoAdicionalResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody CreditoAdicionalRequestDTO dto) {
        return ResponseEntity.ok(creditoAdicionalService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        creditoAdicionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
