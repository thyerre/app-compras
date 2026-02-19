package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.PlanoContasService;
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
@RequestMapping("/api/contabilidade/plano-contas")
@RequiredArgsConstructor
public class PlanoContasController {

    private final PlanoContasService planoContasService;

    @GetMapping
    public ResponseEntity<Page<PlanoContasListDTO>> findAll(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Short classe,
            @RequestParam(required = false) String tipo,
            @PageableDefault(size = 20, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(planoContasService.findAll(codigo, descricao, classe, tipo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoContasResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(planoContasService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlanoContasResponseDTO> create(@Valid @RequestBody PlanoContasRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planoContasService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoContasResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody PlanoContasRequestDTO dto) {
        return ResponseEntity.ok(planoContasService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planoContasService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
