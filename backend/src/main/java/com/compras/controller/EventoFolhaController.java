package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.EventoFolhaService;
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
@RequestMapping("/api/rh/eventos-folha")
@RequiredArgsConstructor
public class EventoFolhaController {

    private final EventoFolhaService eventoFolhaService;

    @GetMapping
    public ResponseEntity<Page<EventoFolhaListDTO>> findAll(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(eventoFolhaService.findAll(descricao, tipo, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoFolhaResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventoFolhaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EventoFolhaResponseDTO> create(@Valid @RequestBody EventoFolhaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoFolhaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoFolhaResponseDTO> update(
            @PathVariable Integer id, @Valid @RequestBody EventoFolhaRequestDTO dto) {
        return ResponseEntity.ok(eventoFolhaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        eventoFolhaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
