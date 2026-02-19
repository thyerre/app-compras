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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioRgfService {

    private final RelatorioRgfRepository relatorioRgfRepository;

    @Transactional(readOnly = true)
    public Page<RelatorioRgfListDTO> findAll(Integer exercicio, Integer quadrimestre,
                                               Pageable pageable) {
        Page<RelatorioRgf> page = relatorioRgfRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (quadrimestre != null) {
                predicates.add(cb.equal(root.get("quadrimestre"), quadrimestre));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<RelatorioRgfListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public RelatorioRgfResponseDTO findById(Long id) {
        RelatorioRgf relatorio = relatorioRgfRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RGF_NOT_FOUND",
                        "Relatório RGF não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(relatorio);
    }

    @Transactional
    public RelatorioRgfResponseDTO create(RelatorioRgfRequestDTO dto) {
        RelatorioRgf relatorio = new RelatorioRgf();
        updateEntity(relatorio, dto);
        return toResponseDTO(relatorioRgfRepository.save(relatorio));
    }

    @Transactional
    public RelatorioRgfResponseDTO update(Long id, RelatorioRgfRequestDTO dto) {
        RelatorioRgf relatorio = relatorioRgfRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RGF_NOT_FOUND",
                        "Relatório RGF não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(relatorio, dto);
        return toResponseDTO(relatorioRgfRepository.save(relatorio));
    }

    @Transactional
    public void delete(Long id) {
        if (!relatorioRgfRepository.existsById(id)) {
            throw new BusinessException("MC_RGF_NOT_FOUND",
                    "Relatório RGF não encontrado", HttpStatus.NOT_FOUND);
        }
        relatorioRgfRepository.deleteById(id);
    }

    private void updateEntity(RelatorioRgf r, RelatorioRgfRequestDTO dto) {
        r.setExercicio(dto.getExercicio());
        r.setQuadrimestre(dto.getQuadrimestre());
        r.setPeriodoInicio(dto.getPeriodoInicio());
        r.setPeriodoFim(dto.getPeriodoFim());
        r.setReceitaCorrenteLiquida(dto.getReceitaCorrenteLiquida() != null ? dto.getReceitaCorrenteLiquida() : BigDecimal.ZERO);
        r.setDespesaPessoalExecutivo(dto.getDespesaPessoalExecutivo() != null ? dto.getDespesaPessoalExecutivo() : BigDecimal.ZERO);
        r.setDespesaPessoalLegislativo(dto.getDespesaPessoalLegislativo() != null ? dto.getDespesaPessoalLegislativo() : BigDecimal.ZERO);
        r.setDespesaPessoalTotal(dto.getDespesaPessoalTotal() != null ? dto.getDespesaPessoalTotal() : BigDecimal.ZERO);
        r.setPercentualPessoalExecutivo(dto.getPercentualPessoalExecutivo() != null ? dto.getPercentualPessoalExecutivo() : BigDecimal.ZERO);
        r.setPercentualPessoalLegislativo(dto.getPercentualPessoalLegislativo() != null ? dto.getPercentualPessoalLegislativo() : BigDecimal.ZERO);
        r.setPercentualPessoalTotal(dto.getPercentualPessoalTotal() != null ? dto.getPercentualPessoalTotal() : BigDecimal.ZERO);
        r.setLimiteMaximo(dto.getLimiteMaximo() != null ? dto.getLimiteMaximo() : new BigDecimal("60.00"));
        r.setLimitePrudencial(dto.getLimitePrudencial() != null ? dto.getLimitePrudencial() : new BigDecimal("57.00"));
        r.setLimiteAlerta(dto.getLimiteAlerta() != null ? dto.getLimiteAlerta() : new BigDecimal("54.00"));
        r.setDividaConsolidada(dto.getDividaConsolidada() != null ? dto.getDividaConsolidada() : BigDecimal.ZERO);
        r.setLimiteDivida(dto.getLimiteDivida() != null ? dto.getLimiteDivida() : BigDecimal.ZERO);
        r.setPercentualDivida(dto.getPercentualDivida() != null ? dto.getPercentualDivida() : BigDecimal.ZERO);
        r.setDisponibilidadeCaixa(dto.getDisponibilidadeCaixa() != null ? dto.getDisponibilidadeCaixa() : BigDecimal.ZERO);
        r.setObrigacoesFinanceiras(dto.getObrigacoesFinanceiras() != null ? dto.getObrigacoesFinanceiras() : BigDecimal.ZERO);
        r.setStatus(dto.getStatus() != null ? dto.getStatus() : "RASCUNHO");
        r.setResponsavelNome(dto.getResponsavelNome());
        r.setResponsavelCargo(dto.getResponsavelCargo());
        r.setContadorNome(dto.getContadorNome());
        r.setContadorCrc(dto.getContadorCrc());
        r.setObservacoes(dto.getObservacoes());
    }

    private RelatorioRgfListDTO toListDTO(RelatorioRgf r) {
        return RelatorioRgfListDTO.builder()
                .id(r.getId())
                .exercicio(r.getExercicio())
                .quadrimestre(r.getQuadrimestre())
                .receitaCorrenteLiquida(r.getReceitaCorrenteLiquida())
                .despesaPessoalTotal(r.getDespesaPessoalTotal())
                .percentualPessoalTotal(r.getPercentualPessoalTotal())
                .status(r.getStatus())
                .build();
    }

    private RelatorioRgfResponseDTO toResponseDTO(RelatorioRgf r) {
        return RelatorioRgfResponseDTO.builder()
                .id(r.getId())
                .exercicio(r.getExercicio())
                .quadrimestre(r.getQuadrimestre())
                .periodoInicio(r.getPeriodoInicio())
                .periodoFim(r.getPeriodoFim())
                .receitaCorrenteLiquida(r.getReceitaCorrenteLiquida())
                .despesaPessoalExecutivo(r.getDespesaPessoalExecutivo())
                .despesaPessoalLegislativo(r.getDespesaPessoalLegislativo())
                .despesaPessoalTotal(r.getDespesaPessoalTotal())
                .percentualPessoalExecutivo(r.getPercentualPessoalExecutivo())
                .percentualPessoalLegislativo(r.getPercentualPessoalLegislativo())
                .percentualPessoalTotal(r.getPercentualPessoalTotal())
                .limiteMaximo(r.getLimiteMaximo())
                .limitePrudencial(r.getLimitePrudencial())
                .limiteAlerta(r.getLimiteAlerta())
                .dividaConsolidada(r.getDividaConsolidada())
                .limiteDivida(r.getLimiteDivida())
                .percentualDivida(r.getPercentualDivida())
                .disponibilidadeCaixa(r.getDisponibilidadeCaixa())
                .obrigacoesFinanceiras(r.getObrigacoesFinanceiras())
                .status(r.getStatus())
                .dataGeracao(r.getDataGeracao())
                .dataPublicacao(r.getDataPublicacao())
                .responsavelNome(r.getResponsavelNome())
                .responsavelCargo(r.getResponsavelCargo())
                .contadorNome(r.getContadorNome())
                .contadorCrc(r.getContadorCrc())
                .observacoes(r.getObservacoes())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
