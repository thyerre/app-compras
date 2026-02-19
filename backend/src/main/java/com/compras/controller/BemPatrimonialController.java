package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.BemPatrimonialService;
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
@RequestMapping("/api/contabilidade/bens-patrimoniais")
@RequiredArgsConstructor
public class BemPatrimonialController {

    private final BemPatrimonialService bemPatrimonialService;

    @GetMapping
    public ResponseEntity<Page<BemPatrimonialListDTO>> findAll(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String situacao,
            @PageableDefault(size = 20, sort = "dataAquisicao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(bemPatrimonialService.findAll(descricao, tipo, situacao, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BemPatrimonialResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bemPatrimonialService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BemPatrimonialResponseDTO> create(@Valid @RequestBody BemPatrimonialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bemPatrimonialService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BemPatrimonialResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody BemPatrimonialRequestDTO dto) {
        return ResponseEntity.ok(bemPatrimonialService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bemPatrimonialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
