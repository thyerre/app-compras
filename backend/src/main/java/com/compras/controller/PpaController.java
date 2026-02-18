package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.PpaService;
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
@RequestMapping("/api/planejamento/ppas")
@RequiredArgsConstructor
public class PpaController {

    private final PpaService ppaService;

    @GetMapping
    public ResponseEntity<Page<PpaListDTO>> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer exercicio,
            @PageableDefault(size = 20, sort = "exercicioInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ppaService.findAll(status, exercicio, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PpaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ppaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PpaResponseDTO> create(@Valid @RequestBody PpaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ppaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PpaResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody PpaRequestDTO dto) {
        return ResponseEntity.ok(ppaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ppaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
