package com.compras.controller;

import com.compras.dto.AcaoListDTO;
import com.compras.dto.AcaoRequestDTO;
import com.compras.dto.AcaoResponseDTO;
import com.compras.service.AcaoService;
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
@RequestMapping("/api/planejamento/acoes")
@RequiredArgsConstructor
public class AcaoController {

    private final AcaoService acaoService;

    @GetMapping
    public ResponseEntity<Page<AcaoListDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer funcaoId,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(acaoService.findAll(nome, tipo, funcaoId, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcaoResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(acaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AcaoResponseDTO> create(@Valid @RequestBody AcaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(acaoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcaoResponseDTO> update(@PathVariable Integer id,
                                                    @Valid @RequestBody AcaoRequestDTO dto) {
        return ResponseEntity.ok(acaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        acaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
