package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.BalanceteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contabilidade/balancetes")
@RequiredArgsConstructor
public class BalanceteController {

    private final BalanceteService balanceteService;

    @GetMapping
    public ResponseEntity<Page<BalanceteListDTO>> findAll(
            @RequestParam(required = false) Integer exercicio,
            @RequestParam(required = false) Integer mes,
            @PageableDefault(size = 20, sort = "exercicio", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(balanceteService.findAll(exercicio, mes, pageable));
    }
}
