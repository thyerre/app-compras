package com.compras.controller;

import com.compras.dto.FornecedorListDTO;
import com.compras.dto.FornecedorRequestDTO;
import com.compras.dto.FornecedorResponseDTO;
import com.compras.service.FornecedorService;
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
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<Page<FornecedorListDTO>> findAll(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String cnpjCpf,
            @RequestParam(required = false) Integer tipoFornecedorId,
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "razaoSocial", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<FornecedorListDTO> result = fornecedorService.findAll(
                razaoSocial, cnpjCpf, tipoFornecedorId, estadoId, ativo, pageable
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> create(@Valid @RequestBody FornecedorRequestDTO dto) {
        FornecedorResponseDTO response = fornecedorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorRequestDTO dto
    ) {
        return ResponseEntity.ok(fornecedorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fornecedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
