package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.*;
import com.compras.exception.BusinessException;
import com.compras.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DividaPublicaService {

    private final DividaPublicaRepository dividaPublicaRepository;

    @Transactional(readOnly = true)
    public Page<DividaPublicaListDTO> findAll(String tipo, String status, Pageable pageable) {
        Page<DividaPublica> page = dividaPublicaRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<DividaPublicaListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public DividaPublicaResponseDTO findById(Long id) {
        DividaPublica divida = dividaPublicaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_DIVIDA_NOT_FOUND",
                        "Dívida pública não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(divida);
    }

    @Transactional
    public DividaPublicaResponseDTO create(DividaPublicaRequestDTO dto) {
        DividaPublica divida = new DividaPublica();
        updateEntity(divida, dto);
        return toResponseDTO(dividaPublicaRepository.save(divida));
    }

    @Transactional
    public DividaPublicaResponseDTO update(Long id, DividaPublicaRequestDTO dto) {
        DividaPublica divida = dividaPublicaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_DIVIDA_NOT_FOUND",
                        "Dívida pública não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(divida, dto);
        return toResponseDTO(dividaPublicaRepository.save(divida));
    }

    @Transactional
    public void delete(Long id) {
        if (!dividaPublicaRepository.existsById(id)) {
            throw new BusinessException("MC_DIVIDA_NOT_FOUND",
                    "Dívida pública não encontrada", HttpStatus.NOT_FOUND);
        }
        dividaPublicaRepository.deleteById(id);
    }

    private void updateEntity(DividaPublica d, DividaPublicaRequestDTO dto) {
        d.setTipo(dto.getTipo());
        d.setCredor(dto.getCredor());
        d.setNumeroContrato(dto.getNumeroContrato());
        d.setDataContratacao(dto.getDataContratacao());
        d.setDataVencimento(dto.getDataVencimento());
        d.setValorOriginal(dto.getValorOriginal());
        d.setSaldoDevedor(dto.getSaldoDevedor());
        d.setTaxaJuros(dto.getTaxaJuros());
        d.setIndiceCorrecao(dto.getIndiceCorrecao());
        d.setFinalidade(dto.getFinalidade());
        d.setLeiAutorizativa(dto.getLeiAutorizativa());
        d.setStatus(dto.getStatus() != null ? dto.getStatus() : "VIGENTE");
        d.setObservacoes(dto.getObservacoes());
    }

    private DividaPublicaListDTO toListDTO(DividaPublica d) {
        return DividaPublicaListDTO.builder()
                .id(d.getId())
                .tipo(d.getTipo())
                .credor(d.getCredor())
                .numeroContrato(d.getNumeroContrato())
                .dataContratacao(d.getDataContratacao())
                .valorOriginal(d.getValorOriginal())
                .saldoDevedor(d.getSaldoDevedor())
                .status(d.getStatus())
                .build();
    }

    private DividaPublicaResponseDTO toResponseDTO(DividaPublica d) {
        return DividaPublicaResponseDTO.builder()
                .id(d.getId())
                .tipo(d.getTipo())
                .credor(d.getCredor())
                .numeroContrato(d.getNumeroContrato())
                .dataContratacao(d.getDataContratacao())
                .dataVencimento(d.getDataVencimento())
                .valorOriginal(d.getValorOriginal())
                .saldoDevedor(d.getSaldoDevedor())
                .taxaJuros(d.getTaxaJuros())
                .indiceCorrecao(d.getIndiceCorrecao())
                .finalidade(d.getFinalidade())
                .leiAutorizativa(d.getLeiAutorizativa())
                .status(d.getStatus())
                .observacoes(d.getObservacoes())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}
