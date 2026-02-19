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
public class CreditoAdicionalService {

    private final CreditoAdicionalRepository creditoAdicionalRepository;
    private final DotacaoOrcamentariaRepository dotacaoRepository;

    @Transactional(readOnly = true)
    public Page<CreditoAdicionalListDTO> findAll(Integer exercicio, String tipo,
                                                   Pageable pageable) {
        Page<CreditoAdicional> page = creditoAdicionalRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<CreditoAdicionalListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public CreditoAdicionalResponseDTO findById(Long id) {
        CreditoAdicional credito = creditoAdicionalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_CREDITO_NOT_FOUND",
                        "Crédito adicional não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(credito);
    }

    @Transactional
    public CreditoAdicionalResponseDTO create(CreditoAdicionalRequestDTO dto) {
        CreditoAdicional credito = new CreditoAdicional();
        updateEntity(credito, dto);
        return toResponseDTO(creditoAdicionalRepository.save(credito));
    }

    @Transactional
    public CreditoAdicionalResponseDTO update(Long id, CreditoAdicionalRequestDTO dto) {
        CreditoAdicional credito = creditoAdicionalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_CREDITO_NOT_FOUND",
                        "Crédito adicional não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(credito, dto);
        return toResponseDTO(creditoAdicionalRepository.save(credito));
    }

    @Transactional
    public void delete(Long id) {
        if (!creditoAdicionalRepository.existsById(id)) {
            throw new BusinessException("MC_CREDITO_NOT_FOUND",
                    "Crédito adicional não encontrado", HttpStatus.NOT_FOUND);
        }
        creditoAdicionalRepository.deleteById(id);
    }

    private void updateEntity(CreditoAdicional c, CreditoAdicionalRequestDTO dto) {
        c.setDotacao(dotacaoRepository.findById(dto.getDotacaoId())
                .orElseThrow(() -> new BusinessException("MC_CREDITO_DOTACAO_NOT_FOUND",
                        "Dotação orçamentária não encontrada")));
        c.setExercicio(dto.getExercicio());
        c.setTipo(dto.getTipo());
        c.setNumeroDecreto(dto.getNumeroDecreto());
        c.setDataDecreto(dto.getDataDecreto());
        c.setNumeroLei(dto.getNumeroLei());
        c.setDataLei(dto.getDataLei());
        c.setValor(dto.getValor());
        c.setFonteAnulacao(dto.getFonteAnulacao());
        c.setJustificativa(dto.getJustificativa());
        c.setStatus(dto.getStatus() != null ? dto.getStatus() : "VIGENTE");
    }

    private CreditoAdicionalListDTO toListDTO(CreditoAdicional c) {
        return CreditoAdicionalListDTO.builder()
                .id(c.getId())
                .exercicio(c.getExercicio())
                .tipo(c.getTipo())
                .numeroDecreto(c.getNumeroDecreto())
                .numeroLei(c.getNumeroLei())
                .valor(c.getValor())
                .status(c.getStatus())
                .build();
    }

    private CreditoAdicionalResponseDTO toResponseDTO(CreditoAdicional c) {
        return CreditoAdicionalResponseDTO.builder()
                .id(c.getId())
                .exercicio(c.getExercicio())
                .tipo(c.getTipo())
                .numeroDecreto(c.getNumeroDecreto())
                .dataDecreto(c.getDataDecreto())
                .numeroLei(c.getNumeroLei())
                .dataLei(c.getDataLei())
                .dotacao(c.getDotacao() != null ? DotacaoOrcamentariaListDTO.builder()
                        .id(c.getDotacao().getId())
                        .valorInicial(c.getDotacao().getValorInicial())
                        .saldoDisponivel(c.getDotacao().getSaldoDisponivel())
                        .build() : null)
                .valor(c.getValor())
                .fonteAnulacao(c.getFonteAnulacao())
                .justificativa(c.getJustificativa())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
