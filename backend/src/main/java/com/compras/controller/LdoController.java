package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.LdoService;
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
@RequestMapping("/api/planejamento/ldos")
@RequiredArgsConstructor
public class LdoController {

    private final LdoService ldoService;

    @GetMapping
    public ResponseEntity<Page<LdoListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "exercicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ldoService.findAll(exercicio, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LdoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ldoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LdoResponseDTO> create(@Valid @RequestBody LdoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ldoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LdoResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody LdoRequestDTO dto) {
        return ResponseEntity.ok(ldoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ldoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
