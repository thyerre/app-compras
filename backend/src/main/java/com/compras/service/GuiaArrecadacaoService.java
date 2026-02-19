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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuiaArrecadacaoService {

    private final GuiaArrecadacaoRepository guiaArrecadacaoRepository;
    private final LancamentoTributarioRepository lancamentoTributarioRepository;
    private final ContribuinteRepository contribuinteRepository;

    @Transactional(readOnly = true)
    public Page<GuiaArrecadacaoListDTO> findAll(String contribuinteNome, String status,
                                                  String dataVencimentoDe, String dataVencimentoAte,
                                                  Pageable pageable) {
        Specification<GuiaArrecadacao> spec = buildFilterSpec(contribuinteNome, status, dataVencimentoDe, dataVencimentoAte);
        Page<GuiaArrecadacao> page = guiaArrecadacaoRepository.findAll(spec, pageable);

        if (page.isEmpty()) return page.map(g -> null);

        List<Long> ids = page.getContent().stream().map(GuiaArrecadacao::getId).toList();
        List<GuiaArrecadacao> items = guiaArrecadacaoRepository.findByIdsWithAssociations(ids);
        Map<Long, GuiaArrecadacao> map = items.stream().collect(Collectors.toMap(GuiaArrecadacao::getId, Function.identity()));

        return page.map(g -> toListDTO(map.get(g.getId())));
    }

    private Specification<GuiaArrecadacao> buildFilterSpec(String contribuinteNome, String status,
                                                            String dataVencimentoDe, String dataVencimentoAte) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (contribuinteNome != null && !contribuinteNome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("contribuinte").get("nomeRazaoSocial")),
                        "%" + contribuinteNome.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (dataVencimentoDe != null && !dataVencimentoDe.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataVencimento"), LocalDate.parse(dataVencimentoDe)));
            }
            if (dataVencimentoAte != null && !dataVencimentoAte.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataVencimento"), LocalDate.parse(dataVencimentoAte)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public GuiaArrecadacaoResponseDTO findById(Long id) {
        GuiaArrecadacao g = guiaArrecadacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_GUIA_NOT_FOUND", "Guia de arrecadação não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(g);
    }

    @Transactional
    public GuiaArrecadacaoResponseDTO create(GuiaArrecadacaoRequestDTO dto) {
        GuiaArrecadacao g = toEntity(dto);
        g = guiaArrecadacaoRepository.save(g);
        return toResponseDTO(g);
    }

    @Transactional
    public GuiaArrecadacaoResponseDTO update(Long id, GuiaArrecadacaoRequestDTO dto) {
        GuiaArrecadacao g = guiaArrecadacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_GUIA_NOT_FOUND", "Guia de arrecadação não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(g, dto);
        g = guiaArrecadacaoRepository.save(g);
        return toResponseDTO(g);
    }

    @Transactional
    public void delete(Long id) {
        if (!guiaArrecadacaoRepository.existsById(id)) {
            throw new BusinessException("ME_GUIA_NOT_FOUND", "Guia de arrecadação não encontrada", HttpStatus.NOT_FOUND);
        }
        guiaArrecadacaoRepository.deleteById(id);
    }

    private GuiaArrecadacaoListDTO toListDTO(GuiaArrecadacao g) {
        return GuiaArrecadacaoListDTO.builder()
                .id(g.getId())
                .numeroGuia(g.getNumeroGuia())
                .contribuinteNome(g.getContribuinte() != null ? g.getContribuinte().getNomeRazaoSocial() : null)
                .contribuinteCpfCnpj(g.getContribuinte() != null ? g.getContribuinte().getCpfCnpj() : null)
                .dataEmissao(g.getDataEmissao() != null ? g.getDataEmissao().toString() : null)
                .dataVencimento(g.getDataVencimento() != null ? g.getDataVencimento().toString() : null)
                .valorTotal(g.getValorTotal())
                .status(g.getStatus())
                .build();
    }

    private GuiaArrecadacaoResponseDTO toResponseDTO(GuiaArrecadacao g) {
        return GuiaArrecadacaoResponseDTO.builder()
                .id(g.getId())
                .numeroGuia(g.getNumeroGuia())
                .lancamentoTributarioId(g.getLancamentoTributario() != null ? g.getLancamentoTributario().getId() : null)
                .lancamentoNumero(g.getLancamentoTributario() != null ? g.getLancamentoTributario().getNumeroLancamento() : null)
                .contribuinteId(g.getContribuinte() != null ? g.getContribuinte().getId() : null)
                .contribuinteNome(g.getContribuinte() != null ? g.getContribuinte().getNomeRazaoSocial() : null)
                .dataEmissao(g.getDataEmissao() != null ? g.getDataEmissao().toString() : null)
                .dataVencimento(g.getDataVencimento() != null ? g.getDataVencimento().toString() : null)
                .valorPrincipal(g.getValorPrincipal())
                .valorDesconto(g.getValorDesconto())
                .valorJuros(g.getValorJuros())
                .valorMulta(g.getValorMulta())
                .valorTotal(g.getValorTotal())
                .codigoBarras(g.getCodigoBarras())
                .linhaDigitavel(g.getLinhaDigitavel())
                .status(g.getStatus())
                .createdAt(g.getCreatedAt())
                .updatedAt(g.getUpdatedAt())
                .build();
    }

    private GuiaArrecadacao toEntity(GuiaArrecadacaoRequestDTO dto) {
        GuiaArrecadacao g = new GuiaArrecadacao();
        updateEntity(g, dto);
        return g;
    }

    private void updateEntity(GuiaArrecadacao g, GuiaArrecadacaoRequestDTO dto) {
        g.setNumeroGuia(dto.getNumeroGuia());
        if (dto.getLancamentoTributarioId() != null) {
            g.setLancamentoTributario(lancamentoTributarioRepository.findById(dto.getLancamentoTributarioId())
                    .orElseThrow(() -> new BusinessException("ME_LANCAMENTO_TRIB_NOT_FOUND", "Lançamento tributário não encontrado")));
        }
        g.setContribuinte(contribuinteRepository.findById(dto.getContribuinteId())
                .orElseThrow(() -> new BusinessException("ME_CONTRIBUINTE_NOT_FOUND", "Contribuinte não encontrado")));
        g.setDataEmissao(LocalDate.parse(dto.getDataEmissao()));
        g.setDataVencimento(LocalDate.parse(dto.getDataVencimento()));
        g.setValorPrincipal(dto.getValorPrincipal());
        g.setValorDesconto(dto.getValorDesconto() != null ? dto.getValorDesconto() : java.math.BigDecimal.ZERO);
        g.setValorJuros(dto.getValorJuros() != null ? dto.getValorJuros() : java.math.BigDecimal.ZERO);
        g.setValorMulta(dto.getValorMulta() != null ? dto.getValorMulta() : java.math.BigDecimal.ZERO);
        g.setValorTotal(dto.getValorTotal());
        g.setCodigoBarras(dto.getCodigoBarras());
        g.setLinhaDigitavel(dto.getLinhaDigitavel());
        g.setStatus(dto.getStatus() != null ? dto.getStatus() : "EMITIDA");
    }
}
