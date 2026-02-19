package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.ContaBancariaService;
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
@RequestMapping("/api/contabilidade/contas-bancarias")
@RequiredArgsConstructor
public class ContaBancariaController {

    private final ContaBancariaService contaBancariaService;

    @GetMapping
    public ResponseEntity<Page<ContaBancariaListDTO>> findAll(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "descricao", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(contaBancariaService.findAll(descricao, tipo, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contaBancariaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ContaBancariaResponseDTO> create(@Valid @RequestBody ContaBancariaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaBancariaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaBancariaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ContaBancariaRequestDTO dto) {
        return ResponseEntity.ok(contaBancariaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contaBancariaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
