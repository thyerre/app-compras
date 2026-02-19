package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.HistoricoFuncionalService;
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
@RequestMapping("/api/rh/historico-funcional")
@RequiredArgsConstructor
public class HistoricoFuncionalController {

    private final HistoricoFuncionalService historicoFuncionalService;

    @GetMapping
    public ResponseEntity<Page<HistoricoFuncionalListDTO>> findAll(
            @RequestParam(required = false) Long servidorId,
            @RequestParam(required = false) String tipoEvento,
            @PageableDefault(size = 20, sort = "dataEvento", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(historicoFuncionalService.findAll(servidorId, tipoEvento, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoFuncionalResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historicoFuncionalService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HistoricoFuncionalResponseDTO> create(@Valid @RequestBody HistoricoFuncionalRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoFuncionalService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoFuncionalResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody HistoricoFuncionalRequestDTO dto) {
        return ResponseEntity.ok(historicoFuncionalService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historicoFuncionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
