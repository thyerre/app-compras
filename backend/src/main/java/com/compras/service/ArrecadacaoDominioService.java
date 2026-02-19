package com.compras.service;

import com.compras.dto.*;
import com.compras.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArrecadacaoDominioService {

    private final TributoRepository tributoRepository;
    private final AgenteArrecadadorRepository agenteArrecadadorRepository;
    private final TipoVinculoRepository tipoVinculoRepository;
    private final CargoRepository cargoRepository;

    @Transactional(readOnly = true)
    public List<TributoDTO> getTributos() {
        return tributoRepository.findByAtivoTrueOrderByCodigoAsc().stream()
                .map(t -> TributoDTO.builder()
                        .id(t.getId()).codigo(t.getCodigo()).descricao(t.getDescricao())
                        .tipo(t.getTipo()).ativo(t.getAtivo()).build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoVinculoDTO> getTiposVinculo() {
        return tipoVinculoRepository.findByAtivoTrueOrderByDescricaoAsc().stream()
                .map(tv -> TipoVinculoDTO.builder()
                        .id(tv.getId()).codigo(tv.getCodigo()).descricao(tv.getDescricao())
                        .regime(tv.getRegime()).ativo(tv.getAtivo()).build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CargoListDTO> getCargos() {
        return cargoRepository.findByAtivoTrueOrderByDescricaoAsc().stream()
                .map(c -> CargoListDTO.builder()
                        .id(c.getId()).codigo(c.getCodigo()).descricao(c.getDescricao())
                        .tipo(c.getTipo()).vagas(c.getVagas()).vagasOcupadas(c.getVagasOcupadas())
                        .cargaHorariaSemanal(c.getCargaHorariaSemanal()).ativo(c.getAtivo()).build())
                .collect(Collectors.toList());
    }
}
