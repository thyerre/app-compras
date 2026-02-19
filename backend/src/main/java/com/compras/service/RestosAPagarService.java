package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.*;
import com.compras.exception.BusinessException;
import com.compras.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestosAPagarService {

    private final RestosAPagarRepository restosAPagarRepository;
    private final EmpenhoRepository empenhoRepository;

    @Transactional(readOnly = true)
    public Page<RestosAPagarListDTO> findAll(Integer exercicioOrigem, String tipo,
                                              String status, Pageable pageable) {
        Specification<RestosAPagar> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicioOrigem != null) {
                predicates.add(cb.equal(root.get("exercicioOrigem"), exercicioOrigem));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<RestosAPagar> page = restosAPagarRepository.findAll(spec, pageable);
        if (page.isEmpty()) return page.map(r -> null);

        List<Long> ids = page.getContent().stream().map(RestosAPagar::getId).toList();
        List<RestosAPagar> restos = restosAPagarRepository.findByIdsWithAssociations(ids);
        Map<Long, RestosAPagar> map = restos.stream()
                .collect(Collectors.toMap(RestosAPagar::getId, Function.identity()));

        return page.map(r -> toListDTO(map.get(r.getId())));
    }

    @Transactional(readOnly = true)
    public RestosAPagarResponseDTO findById(Long id) {
        RestosAPagar rp = restosAPagarRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("MC_RP_NOT_FOUND",
                        "Restos a pagar não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(rp);
    }

    @Transactional
    public RestosAPagarResponseDTO create(RestosAPagarRequestDTO dto) {
        RestosAPagar rp = new RestosAPagar();
        updateEntity(rp, dto);
        rp = restosAPagarRepository.save(rp);
        return toResponseDTO(restosAPagarRepository.findByIdWithAssociations(rp.getId()).orElse(rp));
    }

    @Transactional
    public RestosAPagarResponseDTO update(Long id, RestosAPagarRequestDTO dto) {
        RestosAPagar rp = restosAPagarRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RP_NOT_FOUND",
                        "Restos a pagar não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(rp, dto);
        rp = restosAPagarRepository.save(rp);
        return toResponseDTO(restosAPagarRepository.findByIdWithAssociations(rp.getId()).orElse(rp));
    }

    @Transactional
    public void delete(Long id) {
        if (!restosAPagarRepository.existsById(id)) {
            throw new BusinessException("MC_RP_NOT_FOUND",
                    "Restos a pagar não encontrado", HttpStatus.NOT_FOUND);
        }
        restosAPagarRepository.deleteById(id);
    }

    private void updateEntity(RestosAPagar r, RestosAPagarRequestDTO dto) {
        r.setEmpenho(empenhoRepository.findById(dto.getEmpenhoId())
                .orElseThrow(() -> new BusinessException("MC_RP_EMPENHO_NOT_FOUND", "Empenho não encontrado")));
        r.setExercicioOrigem(dto.getExercicioOrigem());
        r.setTipo(dto.getTipo());
        r.setValorInscrito(dto.getValorInscrito());
        r.setValorCancelado(dto.getValorCancelado() != null ? dto.getValorCancelado() : BigDecimal.ZERO);
        r.setValorLiquidado(dto.getValorLiquidado() != null ? dto.getValorLiquidado() : BigDecimal.ZERO);
        r.setValorPago(dto.getValorPago() != null ? dto.getValorPago() : BigDecimal.ZERO);
        r.setDataInscricao(dto.getDataInscricao());
        r.setDataCancelamento(dto.getDataCancelamento());
        r.setDataPagamento(dto.getDataPagamento());
        r.setStatus(dto.getStatus() != null ? dto.getStatus() : "INSCRITO");
        r.setObservacoes(dto.getObservacoes());
    }

    private RestosAPagarListDTO toListDTO(RestosAPagar r) {
        return RestosAPagarListDTO.builder()
                .id(r.getId())
                .exercicioOrigem(r.getExercicioOrigem())
                .numeroEmpenho(r.getEmpenho() != null ? r.getEmpenho().getNumeroEmpenho() : null)
                .fornecedorNome(r.getEmpenho() != null && r.getEmpenho().getFornecedor() != null
                        ? r.getEmpenho().getFornecedor().getRazaoSocial() : null)
                .tipo(r.getTipo())
                .valorInscrito(r.getValorInscrito())
                .valorPago(r.getValorPago())
                .dataInscricao(r.getDataInscricao())
                .status(r.getStatus())
                .build();
    }

    private RestosAPagarResponseDTO toResponseDTO(RestosAPagar r) {
        return RestosAPagarResponseDTO.builder()
                .id(r.getId())
                .exercicioOrigem(r.getExercicioOrigem())
                .empenho(r.getEmpenho() != null ? EmpenhoListDTO.builder()
                        .id(r.getEmpenho().getId())
                        .numeroEmpenho(r.getEmpenho().getNumeroEmpenho())
                        .exercicio(r.getEmpenho().getExercicio())
                        .valor(r.getEmpenho().getValor())
                        .status(r.getEmpenho().getStatus())
                        .build() : null)
                .tipo(r.getTipo())
                .valorInscrito(r.getValorInscrito())
                .valorCancelado(r.getValorCancelado())
                .valorLiquidado(r.getValorLiquidado())
                .valorPago(r.getValorPago())
                .dataInscricao(r.getDataInscricao())
                .dataCancelamento(r.getDataCancelamento())
                .dataPagamento(r.getDataPagamento())
                .status(r.getStatus())
                .observacoes(r.getObservacoes())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
