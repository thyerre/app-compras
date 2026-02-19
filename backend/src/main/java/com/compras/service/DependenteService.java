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
public class DependenteService {

    private final DependenteRepository dependenteRepository;
    private final ServidorRepository servidorRepository;

    @Transactional(readOnly = true)
    public Page<DependenteListDTO> findAll(Long servidorId, String nome, String parentesco, Pageable pageable) {
        Specification<Dependente> spec = buildFilterSpec(servidorId, nome, parentesco);
        Page<Dependente> page = dependenteRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<Dependente> buildFilterSpec(Long servidorId, String nome, String parentesco) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (servidorId != null) {
                predicates.add(cb.equal(root.get("servidor").get("id"), servidorId));
            }
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (parentesco != null && !parentesco.isBlank()) {
                predicates.add(cb.equal(root.get("parentesco"), parentesco));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public DependenteResponseDTO findById(Long id) {
        Dependente e = dependenteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_DEPENDENTE_NOT_FOUND", "Dependente não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(e);
    }

    @Transactional
    public DependenteResponseDTO create(DependenteRequestDTO dto) {
        Dependente e = toEntity(dto);
        e = dependenteRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public DependenteResponseDTO update(Long id, DependenteRequestDTO dto) {
        Dependente e = dependenteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_DEPENDENTE_NOT_FOUND", "Dependente não encontrado", HttpStatus.NOT_FOUND));
        updateEntity(e, dto);
        e = dependenteRepository.save(e);
        return toResponseDTO(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!dependenteRepository.existsById(id)) {
            throw new BusinessException("ME_DEPENDENTE_NOT_FOUND", "Dependente não encontrado", HttpStatus.NOT_FOUND);
        }
        dependenteRepository.deleteById(id);
    }

    private DependenteListDTO toListDTO(Dependente e) {
        return DependenteListDTO.builder()
                .id(e.getId())
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .servidorMatricula(e.getServidor() != null ? e.getServidor().getMatricula() : null)
                .nome(e.getNome())
                .cpf(e.getCpf())
                .dataNascimento(e.getDataNascimento())
                .parentesco(e.getParentesco())
                .ativo(e.getAtivo())
                .build();
    }

    private DependenteResponseDTO toResponseDTO(Dependente e) {
        return DependenteResponseDTO.builder()
                .id(e.getId())
                .servidorId(e.getServidor() != null ? e.getServidor().getId() : null)
                .servidorNome(e.getServidor() != null ? e.getServidor().getNome() : null)
                .nome(e.getNome())
                .cpf(e.getCpf())
                .dataNascimento(e.getDataNascimento())
                .parentesco(e.getParentesco())
                .sexo(e.getSexo())
                .dependenteIr(e.getDependenteIr())
                .dependenteSalarioFamilia(e.getDependenteSalarioFamilia())
                .dependentePlanoSaude(e.getDependentePlanoSaude())
                .pensaoAlimenticia(e.getPensaoAlimenticia())
                .percentualPensao(e.getPercentualPensao())
                .dataInicioDependencia(e.getDataInicioDependencia())
                .dataFimDependencia(e.getDataFimDependencia())
                .ativo(e.getAtivo())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private Dependente toEntity(DependenteRequestDTO dto) {
        Dependente e = new Dependente();
        updateEntity(e, dto);
        return e;
    }

    private void updateEntity(Dependente e, DependenteRequestDTO dto) {
        Servidor servidor = servidorRepository.findById(dto.getServidorId())
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        e.setServidor(servidor);
        e.setNome(dto.getNome());
        e.setCpf(dto.getCpf());
        e.setDataNascimento(dto.getDataNascimento());
        e.setParentesco(dto.getParentesco());
        e.setSexo(dto.getSexo());
        e.setDependenteIr(dto.getDependenteIr() != null ? dto.getDependenteIr() : false);
        e.setDependenteSalarioFamilia(dto.getDependenteSalarioFamilia() != null ? dto.getDependenteSalarioFamilia() : false);
        e.setDependentePlanoSaude(dto.getDependentePlanoSaude() != null ? dto.getDependentePlanoSaude() : false);
        e.setPensaoAlimenticia(dto.getPensaoAlimenticia() != null ? dto.getPensaoAlimenticia() : false);
        e.setPercentualPensao(dto.getPercentualPensao());
        e.setDataInicioDependencia(dto.getDataInicioDependencia());
        e.setDataFimDependencia(dto.getDataFimDependencia());
        e.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }
}
