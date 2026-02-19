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
public class BaixaArrecadacaoService {

    private final BaixaArrecadacaoRepository baixaArrecadacaoRepository;
    private final GuiaArrecadacaoRepository guiaArrecadacaoRepository;
    private final AgenteArrecadadorRepository agenteArrecadadorRepository;

    @Transactional(readOnly = true)
    public Page<BaixaArrecadacaoListDTO> findAll(String contribuinteNome, String tipoBaixa,
                                                   String dataBaixaDe, String dataBaixaAte,
                                                   Pageable pageable) {
        Specification<BaixaArrecadacao> spec = buildFilterSpec(contribuinteNome, tipoBaixa, dataBaixaDe, dataBaixaAte);
        Page<BaixaArrecadacao> page = baixaArrecadacaoRepository.findAll(spec, pageable);

        if (page.isEmpty()) return page.map(b -> null);

        List<Long> ids = page.getContent().stream().map(BaixaArrecadacao::getId).toList();
        List<BaixaArrecadacao> items = baixaArrecadacaoRepository.findByIdsWithAssociations(ids);
        Map<Long, BaixaArrecadacao> map = items.stream().collect(Collectors.toMap(BaixaArrecadacao::getId, Function.identity()));

        return page.map(b -> toListDTO(map.get(b.getId())));
    }

    private Specification<BaixaArrecadacao> buildFilterSpec(String contribuinteNome, String tipoBaixa,
                                                              String dataBaixaDe, String dataBaixaAte) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (contribuinteNome != null && !contribuinteNome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("guiaArrecadacao").get("contribuinte").get("nomeRazaoSocial")),
                        "%" + contribuinteNome.toLowerCase() + "%"));
            }
            if (tipoBaixa != null && !tipoBaixa.isBlank()) {
                predicates.add(cb.equal(root.get("tipoBaixa"), tipoBaixa));
            }
            if (dataBaixaDe != null && !dataBaixaDe.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataPagamento"), LocalDate.parse(dataBaixaDe)));
            }
            if (dataBaixaAte != null && !dataBaixaAte.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataPagamento"), LocalDate.parse(dataBaixaAte)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public BaixaArrecadacaoResponseDTO findById(Long id) {
        BaixaArrecadacao b = baixaArrecadacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_BAIXA_NOT_FOUND", "Baixa de arrecadação não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(b);
    }

    @Transactional
    public BaixaArrecadacaoResponseDTO create(BaixaArrecadacaoRequestDTO dto) {
        BaixaArrecadacao b = toEntity(dto);
        b = baixaArrecadacaoRepository.save(b);
        return toResponseDTO(b);
    }

    @Transactional
    public BaixaArrecadacaoResponseDTO update(Long id, BaixaArrecadacaoRequestDTO dto) {
        BaixaArrecadacao b = baixaArrecadacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_BAIXA_NOT_FOUND", "Baixa de arrecadação não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(b, dto);
        b = baixaArrecadacaoRepository.save(b);
        return toResponseDTO(b);
    }

    @Transactional
    public void delete(Long id) {
        if (!baixaArrecadacaoRepository.existsById(id)) {
            throw new BusinessException("ME_BAIXA_NOT_FOUND", "Baixa de arrecadação não encontrada", HttpStatus.NOT_FOUND);
        }
        baixaArrecadacaoRepository.deleteById(id);
    }

    private BaixaArrecadacaoListDTO toListDTO(BaixaArrecadacao b) {
        return BaixaArrecadacaoListDTO.builder()
                .id(b.getId())
                .guiaNumero(b.getGuiaArrecadacao() != null ? b.getGuiaArrecadacao().getNumeroGuia() : null)
                .contribuinteNome(b.getGuiaArrecadacao() != null && b.getGuiaArrecadacao().getContribuinte() != null
                        ? b.getGuiaArrecadacao().getContribuinte().getNomeRazaoSocial() : null)
                .dataPagamento(b.getDataPagamento() != null ? b.getDataPagamento().toString() : null)
                .valorPago(b.getValorPago())
                .tipoBaixa(b.getTipoBaixa())
                .agenteDescricao(b.getAgenteArrecadador() != null ? b.getAgenteArrecadador().getDescricao() : null)
                .build();
    }

    private BaixaArrecadacaoResponseDTO toResponseDTO(BaixaArrecadacao b) {
        return BaixaArrecadacaoResponseDTO.builder()
                .id(b.getId())
                .guiaArrecadacaoId(b.getGuiaArrecadacao() != null ? b.getGuiaArrecadacao().getId() : null)
                .guiaNumero(b.getGuiaArrecadacao() != null ? b.getGuiaArrecadacao().getNumeroGuia() : null)
                .agenteArrecadadorId(b.getAgenteArrecadador() != null ? b.getAgenteArrecadador().getId() : null)
                .agenteDescricao(b.getAgenteArrecadador() != null ? b.getAgenteArrecadador().getDescricao() : null)
                .dataPagamento(b.getDataPagamento() != null ? b.getDataPagamento().toString() : null)
                .dataCredito(b.getDataCredito() != null ? b.getDataCredito().toString() : null)
                .valorPago(b.getValorPago())
                .valorJuros(b.getValorJuros())
                .valorMulta(b.getValorMulta())
                .valorDesconto(b.getValorDesconto())
                .tipoBaixa(b.getTipoBaixa())
                .autenticacao(b.getAutenticacao())
                .observacoes(b.getObservacoes())
                .createdAt(b.getCreatedAt())
                .build();
    }

    private BaixaArrecadacao toEntity(BaixaArrecadacaoRequestDTO dto) {
        BaixaArrecadacao b = new BaixaArrecadacao();
        updateEntity(b, dto);
        return b;
    }

    private void updateEntity(BaixaArrecadacao b, BaixaArrecadacaoRequestDTO dto) {
        b.setGuiaArrecadacao(guiaArrecadacaoRepository.findById(dto.getGuiaArrecadacaoId())
                .orElseThrow(() -> new BusinessException("ME_GUIA_NOT_FOUND", "Guia de arrecadação não encontrada")));
        if (dto.getAgenteArrecadadorId() != null) {
            b.setAgenteArrecadador(agenteArrecadadorRepository.findById(dto.getAgenteArrecadadorId())
                    .orElseThrow(() -> new BusinessException("ME_AGENTE_NOT_FOUND", "Agente arrecadador não encontrado")));
        }
        b.setDataPagamento(LocalDate.parse(dto.getDataPagamento()));
        if (dto.getDataCredito() != null && !dto.getDataCredito().isBlank()) {
            b.setDataCredito(LocalDate.parse(dto.getDataCredito()));
        }
        b.setValorPago(dto.getValorPago());
        b.setValorJuros(dto.getValorJuros() != null ? dto.getValorJuros() : java.math.BigDecimal.ZERO);
        b.setValorMulta(dto.getValorMulta() != null ? dto.getValorMulta() : java.math.BigDecimal.ZERO);
        b.setValorDesconto(dto.getValorDesconto() != null ? dto.getValorDesconto() : java.math.BigDecimal.ZERO);
        b.setTipoBaixa(dto.getTipoBaixa() != null ? dto.getTipoBaixa() : "NORMAL");
        b.setAutenticacao(dto.getAutenticacao());
        b.setObservacoes(dto.getObservacoes());
    }
}
