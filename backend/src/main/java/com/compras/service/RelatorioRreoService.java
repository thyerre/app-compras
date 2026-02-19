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
public class RelatorioRreoService {

    private final RelatorioRreoRepository relatorioRreoRepository;

    @Transactional(readOnly = true)
    public Page<RelatorioRreoListDTO> findAll(Integer exercicio, Integer bimestre,
                                                Pageable pageable) {
        Page<RelatorioRreo> page = relatorioRreoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (exercicio != null) {
                predicates.add(cb.equal(root.get("exercicio"), exercicio));
            }
            if (bimestre != null) {
                predicates.add(cb.equal(root.get("bimestre"), bimestre));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<RelatorioRreoListDTO> dtos = page.getContent().stream()
                .map(this::toListDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public RelatorioRreoResponseDTO findById(Long id) {
        RelatorioRreo relatorio = relatorioRreoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RREO_NOT_FOUND",
                        "Relatório RREO não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(relatorio);
    }

    @Transactional
    public RelatorioRreoResponseDTO create(RelatorioRreoRequestDTO dto) {
        RelatorioRreo relatorio = new RelatorioRreo();
        updateEntity(relatorio, dto);
        return toResponseDTO(relatorioRreoRepository.save(relatorio));
    }

    @Transactional
    public RelatorioRreoResponseDTO update(Long id, RelatorioRreoRequestDTO dto) {
        RelatorioRreo relatorio = relatorioRreoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("MC_RREO_NOT_FOUND",
                        "Relatório RREO não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(relatorio, dto);
        return toResponseDTO(relatorioRreoRepository.save(relatorio));
    }

    @Transactional
    public void delete(Long id) {
        if (!relatorioRreoRepository.existsById(id)) {
            throw new BusinessException("MC_RREO_NOT_FOUND",
                    "Relatório RREO não encontrado", HttpStatus.NOT_FOUND);
        }
        relatorioRreoRepository.deleteById(id);
    }

    private void updateEntity(RelatorioRreo r, RelatorioRreoRequestDTO dto) {
        r.setExercicio(dto.getExercicio());
        r.setBimestre(dto.getBimestre());
        r.setPeriodoInicio(dto.getPeriodoInicio());
        r.setPeriodoFim(dto.getPeriodoFim());
        r.setReceitaPrevista(dto.getReceitaPrevista() != null ? dto.getReceitaPrevista() : BigDecimal.ZERO);
        r.setReceitaRealizada(dto.getReceitaRealizada() != null ? dto.getReceitaRealizada() : BigDecimal.ZERO);
        r.setDespesaFixada(dto.getDespesaFixada() != null ? dto.getDespesaFixada() : BigDecimal.ZERO);
        r.setDespesaEmpenhada(dto.getDespesaEmpenhada() != null ? dto.getDespesaEmpenhada() : BigDecimal.ZERO);
        r.setDespesaLiquidada(dto.getDespesaLiquidada() != null ? dto.getDespesaLiquidada() : BigDecimal.ZERO);
        r.setDespesaPaga(dto.getDespesaPaga() != null ? dto.getDespesaPaga() : BigDecimal.ZERO);
        r.setResultadoPrimario(dto.getResultadoPrimario() != null ? dto.getResultadoPrimario() : BigDecimal.ZERO);
        r.setResultadoNominal(dto.getResultadoNominal() != null ? dto.getResultadoNominal() : BigDecimal.ZERO);
        r.setReceitaCorrenteLiquida(dto.getReceitaCorrenteLiquida() != null ? dto.getReceitaCorrenteLiquida() : BigDecimal.ZERO);
        r.setRestosPagarProcessados(dto.getRestosPagarProcessados() != null ? dto.getRestosPagarProcessados() : BigDecimal.ZERO);
        r.setRestosPagarNaoProc(dto.getRestosPagarNaoProc() != null ? dto.getRestosPagarNaoProc() : BigDecimal.ZERO);
        r.setStatus(dto.getStatus() != null ? dto.getStatus() : "RASCUNHO");
        r.setResponsavelNome(dto.getResponsavelNome());
        r.setResponsavelCargo(dto.getResponsavelCargo());
        r.setContadorNome(dto.getContadorNome());
        r.setContadorCrc(dto.getContadorCrc());
        r.setObservacoes(dto.getObservacoes());
    }

    private RelatorioRreoListDTO toListDTO(RelatorioRreo r) {
        return RelatorioRreoListDTO.builder()
                .id(r.getId())
                .exercicio(r.getExercicio())
                .bimestre(r.getBimestre())
                .receitaRealizada(r.getReceitaRealizada())
                .despesaEmpenhada(r.getDespesaEmpenhada())
                .receitaCorrenteLiquida(r.getReceitaCorrenteLiquida())
                .status(r.getStatus())
                .build();
    }

    private RelatorioRreoResponseDTO toResponseDTO(RelatorioRreo r) {
        return RelatorioRreoResponseDTO.builder()
                .id(r.getId())
                .exercicio(r.getExercicio())
                .bimestre(r.getBimestre())
                .periodoInicio(r.getPeriodoInicio())
                .periodoFim(r.getPeriodoFim())
                .receitaPrevista(r.getReceitaPrevista())
                .receitaRealizada(r.getReceitaRealizada())
                .despesaFixada(r.getDespesaFixada())
                .despesaEmpenhada(r.getDespesaEmpenhada())
                .despesaLiquidada(r.getDespesaLiquidada())
                .despesaPaga(r.getDespesaPaga())
                .resultadoPrimario(r.getResultadoPrimario())
                .resultadoNominal(r.getResultadoNominal())
                .receitaCorrenteLiquida(r.getReceitaCorrenteLiquida())
                .restosPagarProcessados(r.getRestosPagarProcessados())
                .restosPagarNaoProc(r.getRestosPagarNaoProc())
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
