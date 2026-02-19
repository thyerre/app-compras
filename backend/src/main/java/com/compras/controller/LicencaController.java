package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.LicencaService;
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
@RequestMapping("/api/rh/licencas")
@RequiredArgsConstructor
public class LicencaController {

    private final LicencaService licencaService;

    @GetMapping
    public ResponseEntity<Page<LicencaListDTO>> findAll(
            @RequestParam(required = false) Long servidorId,
            @RequestParam(required = false) String tipoLicenca,
            @PageableDefault(size = 20, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(licencaService.findAll(servidorId, tipoLicenca, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicencaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(licencaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LicencaResponseDTO> create(@Valid @RequestBody LicencaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(licencaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LicencaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody LicencaRequestDTO dto) {
        return ResponseEntity.ok(licencaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        licencaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
