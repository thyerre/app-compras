package com.compras.controller;

import com.compras.dto.ProgramaListDTO;
import com.compras.dto.ProgramaRequestDTO;
import com.compras.dto.ProgramaResponseDTO;
import com.compras.service.ProgramaService;
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
@RequestMapping("/api/planejamento/programas")
@RequiredArgsConstructor
public class ProgramaController {

    private final ProgramaService programaService;

    @GetMapping
    public ResponseEntity<Page<ProgramaListDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(programaService.findAll(nome, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramaResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(programaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProgramaResponseDTO> create(@Valid @RequestBody ProgramaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramaResponseDTO> update(@PathVariable Integer id,
                                                        @Valid @RequestBody ProgramaRequestDTO dto) {
        return ResponseEntity.ok(programaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        programaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
