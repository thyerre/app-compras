package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.PagamentoService;
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
@RequestMapping("/api/contabilidade/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<Page<PagamentoListDTO>> findAll(
            @RequestParam(required = false) String numeroPagamento,
            @RequestParam(required = false) Long liquidacaoId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "dataPagamento", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(pagamentoService.findAll(numeroPagamento, liquidacaoId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> create(@Valid @RequestBody PagamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody PagamentoRequestDTO dto) {
        return ResponseEntity.ok(pagamentoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
