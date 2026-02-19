package com.compras.controller;

import com.compras.dto.ContribuinteListDTO;
import com.compras.dto.ContribuinteRequestDTO;
import com.compras.dto.ContribuinteResponseDTO;
import com.compras.service.ContribuinteService;
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
@RequestMapping("/api/arrecadacao/contribuintes")
@RequiredArgsConstructor
public class ContribuinteController {

    private final ContribuinteService contribuinteService;

    @GetMapping
    public ResponseEntity<Page<ContribuinteListDTO>> findAll(
            @RequestParam(required = false) String nomeRazaoSocial,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) String tipoPessoa,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nomeRazaoSocial", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(contribuinteService.findAll(nomeRazaoSocial, cpfCnpj, tipoPessoa, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContribuinteResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contribuinteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ContribuinteResponseDTO> create(@Valid @RequestBody ContribuinteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contribuinteService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContribuinteResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ContribuinteRequestDTO dto) {
        return ResponseEntity.ok(contribuinteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contribuinteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
