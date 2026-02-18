package com.compras.controller;

import com.compras.dto.*;
import com.compras.service.PlanejamentoDominioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planejamento/dominios")
@RequiredArgsConstructor
public class PlanejamentoDominioController {

    private final PlanejamentoDominioService dominioService;

    @GetMapping("/funcoes")
    public ResponseEntity<List<FuncaoDTO>> findAllFuncoes() {
        return ResponseEntity.ok(dominioService.findAllFuncoes());
    }

    @GetMapping("/subfuncoes")
    public ResponseEntity<List<SubfuncaoDTO>> findSubfuncoes(
            @RequestParam(required = false) Integer funcaoId) {
        if (funcaoId != null) {
            return ResponseEntity.ok(dominioService.findSubfuncoesByFuncao(funcaoId));
        }
        return ResponseEntity.ok(dominioService.findAllSubfuncoes());
    }

    @GetMapping("/naturezas-despesa")
    public ResponseEntity<List<NaturezaDespesaDTO>> findAllNaturezasDespesa() {
        return ResponseEntity.ok(dominioService.findAllNaturezasDespesa());
    }

    @GetMapping("/fontes-recurso")
    public ResponseEntity<List<FonteRecursoDTO>> findAllFontesRecurso() {
        return ResponseEntity.ok(dominioService.findAllFontesRecurso());
    }

    @GetMapping("/orgaos")
    public ResponseEntity<List<OrgaoDTO>> findAllOrgaosAtivos() {
        return ResponseEntity.ok(dominioService.findAllOrgaosAtivos());
    }

    @GetMapping("/unidades")
    public ResponseEntity<List<UnidadeDTO>> findUnidadesByOrgao(
            @RequestParam Integer orgaoId) {
        return ResponseEntity.ok(dominioService.findUnidadesByOrgao(orgaoId));
    }

    @GetMapping("/programas")
    public ResponseEntity<List<ProgramaListDTO>> findAllProgramasAtivos() {
        return ResponseEntity.ok(dominioService.findAllProgramasAtivos());
    }

    @GetMapping("/loas")
    public ResponseEntity<List<LoaListDTO>> findAllLoasSimples() {
        return ResponseEntity.ok(dominioService.findAllLoasSimples());
    }
}
