package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.DotacaoOrcamentariaService;
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
@RequestMapping("/api/planejamento/dotacoes")
@RequiredArgsConstructor
public class DotacaoOrcamentariaController {

    private final DotacaoOrcamentariaService dotacaoService;

    @GetMapping
    public ResponseEntity<Page<DotacaoOrcamentariaListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) Integer orgaoId,
            @RequestParam(required = false) Integer programaId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(dotacaoService.findAll(exercicio, orgaoId, programaId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DotacaoOrcamentariaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dotacaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DotacaoOrcamentariaResponseDTO> create(
            @Valid @RequestBody DotacaoOrcamentariaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dotacaoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DotacaoOrcamentariaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody DotacaoOrcamentariaRequestDTO dto) {
        return ResponseEntity.ok(dotacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dotacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
