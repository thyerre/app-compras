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
public class ComprasDominioService {

    private final ModalidadeLicitacaoRepository modalidadeRepository;
    private final TipoJulgamentoRepository tipoJulgamentoRepository;
    private final StatusProcessoRepository statusProcessoRepository;

    @Transactional(readOnly = true)
    public List<ModalidadeLicitacaoDTO> findAllModalidades() {
        return modalidadeRepository.findAll().stream()
                .map(m -> ModalidadeLicitacaoDTO.builder()
                        .id(m.getId()).nome(m.getNome()).sigla(m.getSigla())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoJulgamentoDTO> findAllTiposJulgamento() {
        return tipoJulgamentoRepository.findAll().stream()
                .map(t -> TipoJulgamentoDTO.builder()
                        .id(t.getId()).descricao(t.getDescricao())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StatusProcessoDTO> findAllStatusProcesso() {
        return statusProcessoRepository.findAll().stream()
                .map(s -> StatusProcessoDTO.builder()
                        .id(s.getId()).nome(s.getNome()).cor(s.getCor())
                        .build())
                .collect(Collectors.toList());
    }
}
