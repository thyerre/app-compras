package com.compras.controller;

import com.compras.dto.BaixaArrecadacaoListDTO;
import com.compras.dto.BaixaArrecadacaoRequestDTO;
import com.compras.dto.BaixaArrecadacaoResponseDTO;
import com.compras.service.BaixaArrecadacaoService;
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
@RequestMapping("/api/arrecadacao/baixas")
@RequiredArgsConstructor
public class BaixaArrecadacaoController {

    private final BaixaArrecadacaoService baixaArrecadacaoService;

    @GetMapping
    public ResponseEntity<Page<BaixaArrecadacaoListDTO>> findAll(
            @RequestParam(required = false) String contribuinteNome,
            @RequestParam(required = false) String tipoBaixa,
            @RequestParam(required = false) String dataBaixaDe,
            @RequestParam(required = false) String dataBaixaAte,
            @PageableDefault(size = 20, sort = "dataBaixa", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(baixaArrecadacaoService.findAll(contribuinteNome, tipoBaixa, dataBaixaDe, dataBaixaAte, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaixaArrecadacaoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(baixaArrecadacaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BaixaArrecadacaoResponseDTO> create(@Valid @RequestBody BaixaArrecadacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(baixaArrecadacaoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaixaArrecadacaoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody BaixaArrecadacaoRequestDTO dto) {
        return ResponseEntity.ok(baixaArrecadacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        baixaArrecadacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
