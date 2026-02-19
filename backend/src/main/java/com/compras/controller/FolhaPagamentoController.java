package com.compras.controller;

import com.compras.dto.FolhaPagamentoListDTO;
import com.compras.dto.FolhaPagamentoRequestDTO;
import com.compras.dto.FolhaPagamentoResponseDTO;
import com.compras.service.FolhaPagamentoService;
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
@RequestMapping("/api/rh/folhas-pagamento")
@RequiredArgsConstructor
public class FolhaPagamentoController {

    private final FolhaPagamentoService folhaPagamentoService;

    @GetMapping
    public ResponseEntity<Page<FolhaPagamentoListDTO>> findAll(
            @RequestParam(required = false) String competencia,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "competencia", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(folhaPagamentoService.findAll(competencia, tipo, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaPagamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(folhaPagamentoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FolhaPagamentoResponseDTO> create(@Valid @RequestBody FolhaPagamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folhaPagamentoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FolhaPagamentoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody FolhaPagamentoRequestDTO dto) {
        return ResponseEntity.ok(folhaPagamentoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaPagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
