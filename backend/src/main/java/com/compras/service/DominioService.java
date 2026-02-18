package com.compras.service;

import com.compras.dto.EstadoDTO;
import com.compras.dto.MunicipioDTO;
import com.compras.dto.TipoFornecedorDTO;
import com.compras.dto.ClassificacaoFornecedorDTO;
import com.compras.repository.EstadoRepository;
import com.compras.repository.MunicipioRepository;
import com.compras.repository.TipoFornecedorRepository;
import com.compras.repository.ClassificacaoFornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DominioService {

    private final EstadoRepository estadoRepository;
    private final MunicipioRepository municipioRepository;
    private final TipoFornecedorRepository tipoFornecedorRepository;
    private final ClassificacaoFornecedorRepository classificacaoRepository;

    @Transactional(readOnly = true)
    public List<EstadoDTO> findAllEstados() {
        return estadoRepository.findAll().stream()
                .map(e -> EstadoDTO.builder()
                        .id(e.getId())
                        .sigla(e.getSigla())
                        .nome(e.getNome())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MunicipioDTO> findMunicipiosByEstado(Integer estadoId) {
        return municipioRepository.findByEstadoIdOrderByNome(estadoId).stream()
                .map(m -> MunicipioDTO.builder()
                        .id(m.getId())
                        .nome(m.getNome())
                        .codigoIbge(m.getCodigoIbge())
                        .estadoId(m.getEstado().getId())
                        .estadoSigla(m.getEstado().getSigla())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MunicipioDTO> searchMunicipios(String nome) {
        return municipioRepository.findByNomeContainingIgnoreCaseOrderByNome(nome).stream()
                .map(m -> MunicipioDTO.builder()
                        .id(m.getId())
                        .nome(m.getNome())
                        .codigoIbge(m.getCodigoIbge())
                        .estadoId(m.getEstado().getId())
                        .estadoSigla(m.getEstado().getSigla())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoFornecedorDTO> findAllTiposFornecedor() {
        return tipoFornecedorRepository.findAll().stream()
                .map(t -> TipoFornecedorDTO.builder()
                        .id(t.getId())
                        .descricao(t.getDescricao())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassificacaoFornecedorDTO> findAllClassificacoes() {
        return classificacaoRepository.findAll().stream()
                .map(c -> ClassificacaoFornecedorDTO.builder()
                        .id(c.getId())
                        .descricao(c.getDescricao())
                        .build())
                .collect(Collectors.toList());
    }
}
