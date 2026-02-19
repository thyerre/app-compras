package com.compras.controller;

import com.compras.dto.ServidorListDTO;
import com.compras.dto.ServidorRequestDTO;
import com.compras.dto.ServidorResponseDTO;
import com.compras.service.ServidorService;
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
@RequestMapping("/api/rh/servidores")
@RequiredArgsConstructor
public class ServidorController {

    private final ServidorService servidorService;

    @GetMapping
    public ResponseEntity<Page<ServidorListDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String situacao,
            @RequestParam(required = false) Long cargoId,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(servidorService.findAll(nome, matricula, cpf, situacao, cargoId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servidorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ServidorResponseDTO> create(@Valid @RequestBody ServidorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servidorService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ServidorRequestDTO dto) {
        return ResponseEntity.ok(servidorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servidorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
