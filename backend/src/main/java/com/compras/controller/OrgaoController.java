package com.compras.controller;

import com.compras.dto.OrgaoDTO;
import com.compras.dto.OrgaoRequestDTO;
import com.compras.service.OrgaoService;
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
@RequestMapping("/api/planejamento/orgaos")
@RequiredArgsConstructor
public class OrgaoController {

    private final OrgaoService orgaoService;

    @GetMapping
    public ResponseEntity<Page<OrgaoDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(orgaoService.findAll(nome, ativo, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgaoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(orgaoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrgaoDTO> create(@Valid @RequestBody OrgaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orgaoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrgaoDTO> update(@PathVariable Integer id,
                                            @Valid @RequestBody OrgaoRequestDTO dto) {
        return ResponseEntity.ok(orgaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orgaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
