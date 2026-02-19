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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LicencaService {

    private final LicencaRepository licencaRepository;
    private final ServidorRepository servidorRepository;

    @Transactional(readOnly = true)
    public Page<LicencaListDTO> findAll(Long servidorId, String tipoLicenca, Pageable pageable) {
        Specification<Licenca> spec = buildFilterSpec(servidorId, tipoLicenca);
        Page<Licenca> page = licencaRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<Licenca> buildFilterSpec(Long servidorId, String tipoLicenca) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (servidorId != null) {
                predicates.add(cb.equal(root.get("servidor").get("id"), servidorId));
            }
            if (tipoLicenca != null && !tipoLicenca.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("tipoLicenca")), "%" + tipoLicenca.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public LicencaResponseDTO findById(Long id) {
        Licenca e = licencaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_LICENCA_NOT_FOUND", "Licença não encontrada", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public LicencaResponseDTO create(LicencaRequestDTO dto) {
        Licenca e = toEntity(dto);
        e = licencaRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public LicencaResponseDTO update(Long id, LicencaRequestDTO dto) {
        Licenca e = licencaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_LICENCA_NOT_FOUND", "Licença não encontrada", HttpStatus.NOT_FOUND));
        updateEntity(e, dto);
        e = licencaRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!licencaRepository.existsById(id)) {
            throw new BusinessException("ME_LICENCA_NOT_FOUND", "Licença não encontrada", HttpStatus.NOT_FOUND);
        }
        licencaRepository.deleteById(id);
    }

    private LicencaListDTO toListDTO(Licenca e) {
        return LicencaListDTO.builder()
                .id(e.getId())
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .servidorMatricula(e.getServidor() != null ? e.getServidor().getMatricula() : null)
                .tipoLicenca(e.getTipoLicenca())
                .dataInicio(e.getDataInicio())
                .dataFim(e.getDataFim())
                .quantidadeDias(e.getQuantidadeDias())
                .remunerada(e.getRemunerada())
                .build();
    }

    private LicencaResponseDTO toResponseDTO(Licenca e) {
        return LicencaResponseDTO.builder()
                .id(e.getId())
                .servidorId(e.getServidor() != null ? e.getServidor().getId() : null)
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .tipoLicenca(e.getTipoLicenca())
                .dataInicio(e.getDataInicio())
                .dataFim(e.getDataFim())
                .quantidadeDias(e.getQuantidadeDias())
                .remunerada(e.getRemunerada())
                .cid(e.getCid())
                .medicoNome(e.getMedicoNome())
                .medicoCrm(e.getMedicoCrm())
                .numeroDocumento(e.getNumeroDocumento())
                .observacoes(e.getObservacoes())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private Licenca toEntity(LicencaRequestDTO dto) {
        Licenca e = new Licenca();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(Licenca e, LicencaRequestDTO dto) {
        Servidor servidor = servidorRepository.findById(dto.getServidorId())
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        e.setServidor(servidor);
        e.setTipoLicenca(dto.getTipoLicenca());
        e.setDataInicio(dto.getDataInicio());
        e.setDataFim(dto.getDataFim());
        e.setQuantidadeDias(dto.getQuantidadeDias());
        e.setRemunerada(dto.getRemunerada() != null ? dto.getRemunerada() : true);
        e.setCid(dto.getCid());
        e.setMedicoNome(dto.getMedicoNome());
        e.setMedicoCrm(dto.getMedicoCrm());
        e.setNumeroDocumento(dto.getNumeroDocumento());
        e.setObservacoes(dto.getObservacoes());
    }
}
