package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.NivelSalarialService;
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
@RequestMapping("/api/rh/niveis-salariais")
@RequiredArgsConstructor
public class NivelSalarialController {

    private final NivelSalarialService nivelSalarialService;

    @GetMapping
    public ResponseEntity<Page<NivelSalarialListDTO>> findAll(
            @RequestParam(required = false) Integer cargoId,
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nivel", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(nivelSalarialService.findAll(cargoId, nivel, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelSalarialResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(nivelSalarialService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NivelSalarialResponseDTO> create(@Valid @RequestBody NivelSalarialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nivelSalarialService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelSalarialResponseDTO> update(
            @PathVariable Integer id, @Valid @RequestBody NivelSalarialRequestDTO dto) {
        return ResponseEntity.ok(nivelSalarialService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        nivelSalarialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
