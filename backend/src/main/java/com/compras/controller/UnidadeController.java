package com.compras.controller;

import com.compras.dto.UnidadeDTO;
import com.compras.dto.UnidadeRequestDTO;
import com.compras.service.UnidadeService;
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
@RequestMapping("/api/planejamento/unidades")
@RequiredArgsConstructor
public class UnidadeController {

    private final UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<Page<UnidadeDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer orgaoId,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(unidadeService.findAll(nome, orgaoId, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(unidadeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UnidadeDTO> create(@Valid @RequestBody UnidadeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody UnidadeRequestDTO dto) {
        return ResponseEntity.ok(unidadeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        unidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
