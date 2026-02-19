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
public class LancamentoTributarioService {

    private final LancamentoTributarioRepository lancamentoTributarioRepository;
    private final ContribuinteRepository contribuinteRepository;
    private final TributoRepository tributoRepository;

    @Transactional(readOnly = true)
    public Page<LancamentoTributarioListDTO> findAll(Integer exercicio, String contribuinteNome,
                                                       String status, Integer tributoId, Pageable pageable) {
        Specification<LancamentoTributario> spec = buildFilterSpec(exercicio, contribuinteNome, status, tributoId);
        Page<LancamentoTributario> page = lancamentoTributarioRepository.findAll(spec, pageable);

        if (page.isEmpty()) return page.map(lt -> null);

        List<Long> ids = page.getContent().stream().map(LancamentoTributario::getId).toList();
        List<LancamentoTributario> items = lancamentoTributarioRepository.findByIdsWithAssociations(ids);
        Map<Long, LancamentoTributario> map = items.stream().collect(Collectors.toMap(LancamentoTributario::getId, Function.identity()));

        return page.map(lt -> toListDTO(map.get(lt.getId())));
    }

    private Specification<LancamentoTributario> buildFilterSpec(Integer exercicio, String contribuinteNome,
                                                                  String status, Integer tributoId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) predicates.add(cb.equal(root.get("exercicio"), exercicio));
            if (contribuinteNome != null && !contribuinteNome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("contribuinte").get("nomeRazaoSocial")), "%" + contribuinteNome.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) predicates.add(cb.equal(root.get("status"), status));
            if (tributoId != null) predicates.add(cb.equal(root.get("tributo").get("id"), tributoId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public LancamentoTributarioResponseDTO findById(Long id) {
        LancamentoTributario lt = lancamentoTributarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_LANCAMENTO_TRIB_NOT_FOUND", "Lançamento tributário não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(lt);
    }

    @Transactional
    public LancamentoTributarioResponseDTO create(LancamentoTributarioRequestDTO dto) {
        LancamentoTributario lt = toEntity(dto);
        lt = lancamentoTributarioRepository.save(lt);
        return toResponseDTO(lt);
    }

    @Transactional
    public LancamentoTributarioResponseDTO update(Long id, LancamentoTributarioRequestDTO dto) {
        LancamentoTributario lt = lancamentoTributarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_LANCAMENTO_TRIB_NOT_FOUND", "Lançamento tributário não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(lt, dto);
        lt = lancamentoTributarioRepository.save(lt);
        return toResponseDTO(lt);
    }

    @Transactional
    public void delete(Long id) {
        if (!lancamentoTributarioRepository.existsById(id)) {
            throw new BusinessException("ME_LANCAMENTO_TRIB_NOT_FOUND", "Lançamento tributário não encontrado", HttpStatus.NOT_FOUND);
        }
        lancamentoTributarioRepository.deleteById(id);
    }

    private LancamentoTributarioListDTO toListDTO(LancamentoTributario lt) {
        return LancamentoTributarioListDTO.builder()
                .id(lt.getId())
                .numeroLancamento(lt.getNumeroLancamento())
                .exercicio(lt.getExercicio())
                .contribuinteNome(lt.getContribuinte() != null ? lt.getContribuinte().getNomeRazaoSocial() : null)
                .contribuinteCpfCnpj(lt.getContribuinte() != null ? lt.getContribuinte().getCpfCnpj() : null)
                .tributoCodigo(lt.getTributo() != null ? lt.getTributo().getCodigo() : null)
                .tributoDescricao(lt.getTributo() != null ? lt.getTributo().getDescricao() : null)
                .dataLancamento(lt.getDataLancamento() != null ? lt.getDataLancamento().toString() : null)
                .dataVencimento(lt.getDataVencimento() != null ? lt.getDataVencimento().toString() : null)
                .valorTotal(lt.getValorTotal())
                .status(lt.getStatus())
                .build();
    }

    private LancamentoTributarioResponseDTO toResponseDTO(LancamentoTributario lt) {
        return LancamentoTributarioResponseDTO.builder()
                .id(lt.getId())
                .numeroLancamento(lt.getNumeroLancamento())
                .exercicio(lt.getExercicio())
                .contribuinteId(lt.getContribuinte() != null ? lt.getContribuinte().getId() : null)
                .contribuinteNome(lt.getContribuinte() != null ? lt.getContribuinte().getNomeRazaoSocial() : null)
                .contribuinteCpfCnpj(lt.getContribuinte() != null ? lt.getContribuinte().getCpfCnpj() : null)
                .tributoId(lt.getTributo() != null ? lt.getTributo().getId() : null)
                .tributoCodigo(lt.getTributo() != null ? lt.getTributo().getCodigo() : null)
                .tributoDescricao(lt.getTributo() != null ? lt.getTributo().getDescricao() : null)
                .imovelId(lt.getImovel() != null ? lt.getImovel().getId() : null)
                .imovelInscricao(lt.getImovel() != null ? lt.getImovel().getInscricaoImobiliaria() : null)
                .dataLancamento(lt.getDataLancamento() != null ? lt.getDataLancamento().toString() : null)
                .dataVencimento(lt.getDataVencimento() != null ? lt.getDataVencimento().toString() : null)
                .baseCalculo(lt.getBaseCalculo())
                .aliquota(lt.getAliquota())
                .valorPrincipal(lt.getValorPrincipal())
                .valorDesconto(lt.getValorDesconto())
                .valorJuros(lt.getValorJuros())
                .valorMulta(lt.getValorMulta())
                .valorTotal(lt.getValorTotal())
                .status(lt.getStatus())
                .historico(lt.getHistorico())
                .createdAt(lt.getCreatedAt())
                .updatedAt(lt.getUpdatedAt())
                .build();
    }

    private LancamentoTributario toEntity(LancamentoTributarioRequestDTO dto) {
        LancamentoTributario lt = new LancamentoTributario();
        updateEntity(lt, dto);
        return lt;
    }

    private void updateEntity(LancamentoTributario lt, LancamentoTributarioRequestDTO dto) {
        lt.setNumeroLancamento(dto.getNumeroLancamento());
        lt.setExercicio(dto.getExercicio());
        lt.setContribuinte(contribuinteRepository.findById(dto.getContribuinteId())
                .orElseThrow(() -> new BusinessException("ME_CONTRIBUINTE_NOT_FOUND", "Contribuinte não encontrado")));
        lt.setTributo(tributoRepository.findById(dto.getTributoId())
                .orElseThrow(() -> new BusinessException("ME_TRIBUTO_NOT_FOUND", "Tributo não encontrado")));
        lt.setDataLancamento(LocalDate.parse(dto.getDataLancamento()));
        lt.setDataVencimento(LocalDate.parse(dto.getDataVencimento()));
        lt.setBaseCalculo(dto.getBaseCalculo());
        lt.setAliquota(dto.getAliquota());
        lt.setValorPrincipal(dto.getValorPrincipal());
        lt.setValorDesconto(dto.getValorDesconto() != null ? dto.getValorDesconto() : java.math.BigDecimal.ZERO);
        lt.setValorJuros(dto.getValorJuros() != null ? dto.getValorJuros() : java.math.BigDecimal.ZERO);
        lt.setValorMulta(dto.getValorMulta() != null ? dto.getValorMulta() : java.math.BigDecimal.ZERO);
        lt.setValorTotal(dto.getValorTotal());
        lt.setStatus(dto.getStatus() != null ? dto.getStatus() : "ABERTO");
        lt.setHistorico(dto.getHistorico());
    }
}
