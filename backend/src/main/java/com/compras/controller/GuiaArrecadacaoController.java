package com.compras.controller;

import com.compras.dto.GuiaArrecadacaoListDTO;
import com.compras.dto.GuiaArrecadacaoRequestDTO;
import com.compras.dto.GuiaArrecadacaoResponseDTO;
import com.compras.service.GuiaArrecadacaoService;
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
@RequestMapping("/api/arrecadacao/guias")
@RequiredArgsConstructor
public class GuiaArrecadacaoController {

    private final GuiaArrecadacaoService guiaArrecadacaoService;

    @GetMapping
    public ResponseEntity<Page<GuiaArrecadacaoListDTO>> findAll(
            @RequestParam(required = false) String contribuinteNome,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dataVencimentoDe,
            @RequestParam(required = false) String dataVencimentoAte,
            @PageableDefault(size = 20, sort = "dataEmissao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(guiaArrecadacaoService.findAll(contribuinteNome, status, dataVencimentoDe, dataVencimentoAte, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuiaArrecadacaoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(guiaArrecadacaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GuiaArrecadacaoResponseDTO> create(@Valid @RequestBody GuiaArrecadacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guiaArrecadacaoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuiaArrecadacaoResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody GuiaArrecadacaoRequestDTO dto) {
        return ResponseEntity.ok(guiaArrecadacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guiaArrecadacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
