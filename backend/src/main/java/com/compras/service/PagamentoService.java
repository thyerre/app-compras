package com.compras.service;

import com.compras.dto.*;
import com.compras.entity.*;
import com.compras.exception.BusinessException;
import com.compras.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final LiquidacaoRepository liquidacaoRepository;
    private final ContaBancariaRepository contaBancariaRepository;

    @Transactional(readOnly = true)
    public Page<PagamentoListDTO> findAll(String numeroPagamento, Long liquidacaoId,
                                           String status, Pageable pageable) {
        Page<Pagamento> page = pagamentoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (numeroPagamento != null && !numeroPagamento.isBlank()) {
                predicates.add(cb.like(root.get("numeroPagamento"), "%" + numeroPagamento + "%"));
            }
            if (liquidacaoId != null) {
                predicates.add(cb.equal(root.get("liquidacao").get("id"), liquidacaoId));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        if (page.isEmpty()) return page.map(e -> null);

        List<Long> ids = page.getContent().stream().map(Pagamento::getId).toList();
        List<Pagamento> pagamentos = pagamentoRepository.findByIdsWithAssociations(ids);
        Map<Long, Pagamento> map = pagamentos.stream()
                .collect(Collectors.toMap(Pagamento::getId, Function.identity()));

        return page.map(p -> toListDTO(map.get(p.getId())));
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO findById(Long id) {
        Pagamento p = pagamentoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new BusinessException("MC_PAGAMENTO_NOT_FOUND",
                        "Pagamento não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(p);
    }

    @Transactional
    public PagamentoResponseDTO create(PagamentoRequestDTO dto) {
        Pagamento p = new Pagamento();
        updateEntity(p, dto);
        p = pagamentoRepository.save(p);
        return toResponseDTO(pagamentoRepository.findByIdWithAssociations(p.getId()).orElse(p));
    }

    @Transactional
    public PagamentoResponseDTO update(Long id, PagamentoRequestDTO dto) {
        Pagamento p = pagamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_PAGAMENTO_NOT_FOUND",
                        "Pagamento não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(p, dto);
        p = pagamentoRepository.save(p);
        return toResponseDTO(pagamentoRepository.findByIdWithAssociations(p.getId()).orElse(p));
    }

    @Transactional
    public void delete(Long id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new BusinessException("MC_PAGAMENTO_NOT_FOUND",
                    "Pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        pagamentoRepository.deleteById(id);
    }

    private void updateEntity(Pagamento p, PagamentoRequestDTO dto) {
        p.setLiquidacao(liquidacaoRepository.findById(dto.getLiquidacaoId())
                .orElseThrow(() -> new BusinessException("MC_LIQUIDACAO_NOT_FOUND", "Liquidação não encontrada")));
        p.setNumeroPagamento(dto.getNumeroPagamento());
        p.setDataPagamento(dto.getDataPagamento());
        p.setValor(dto.getValor());
        if (dto.getContaBancariaId() != null) {
            p.setContaBancaria(contaBancariaRepository.findById(dto.getContaBancariaId())
                    .orElseThrow(() -> new BusinessException("MC_CONTA_NOT_FOUND", "Conta bancária não encontrada")));
        }
        p.setFormaPagamento(dto.getFormaPagamento() != null ? dto.getFormaPagamento() : "TRANSFERENCIA");
        p.setDocumentoBancario(dto.getDocumentoBancario());
        p.setDescricao(dto.getDescricao());
        p.setStatus(dto.getStatus() != null ? dto.getStatus() : "EFETIVADO");
    }

    private PagamentoListDTO toListDTO(Pagamento p) {
        Liquidacao l = p.getLiquidacao();
        Empenho e = l != null ? l.getEmpenho() : null;
        return PagamentoListDTO.builder()
                .id(p.getId())
                .numeroPagamento(p.getNumeroPagamento())
                .numeroLiquidacao(l != null ? l.getNumeroLiquidacao() : null)
                .numeroEmpenho(e != null ? e.getNumeroEmpenho() : null)
                .fornecedorNome(e != null && e.getFornecedor() != null ? e.getFornecedor().getRazaoSocial() : null)
                .dataPagamento(p.getDataPagamento())
                .valor(p.getValor())
                .formaPagamento(p.getFormaPagamento())
                .contaBancariaDescricao(p.getContaBancaria() != null ? p.getContaBancaria().getDescricao() : null)
                .status(p.getStatus())
                .build();
    }

    private PagamentoResponseDTO toResponseDTO(Pagamento p) {
        Liquidacao l = p.getLiquidacao();
        return PagamentoResponseDTO.builder()
                .id(p.getId())
                .liquidacao(l != null ? LiquidacaoListDTO.builder()
                        .id(l.getId()).numeroLiquidacao(l.getNumeroLiquidacao())
                        .numeroEmpenho(l.getEmpenho() != null ? l.getEmpenho().getNumeroEmpenho() : null)
                        .dataLiquidacao(l.getDataLiquidacao()).valor(l.getValor()).status(l.getStatus())
                        .build() : null)
                .numeroPagamento(p.getNumeroPagamento())
                .dataPagamento(p.getDataPagamento())
                .valor(p.getValor())
                .contaBancaria(p.getContaBancaria() != null ? ContaBancariaListDTO.builder()
                        .id(p.getContaBancaria().getId())
                        .bancoNome(p.getContaBancaria().getBancoNome())
                        .agencia(p.getContaBancaria().getAgencia())
                        .numeroConta(p.getContaBancaria().getNumeroConta())
                        .descricao(p.getContaBancaria().getDescricao())
                        .tipo(p.getContaBancaria().getTipo())
                        .build() : null)
                .formaPagamento(p.getFormaPagamento())
                .documentoBancario(p.getDocumentoBancario())
                .descricao(p.getDescricao())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt())
                .build();
    }
}
