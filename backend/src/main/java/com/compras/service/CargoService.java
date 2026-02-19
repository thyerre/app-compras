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
public class CargoService {

    private final CargoRepository cargoRepository;

    @Transactional(readOnly = true)
    public Page<CargoListDTO> findAll(String descricao, String tipo, Boolean ativo, Pageable pageable) {
        Specification<Cargo> spec = buildFilterSpec(descricao, tipo, ativo);
        Page<Cargo> page = cargoRepository.findAll(spec, pageable);
        return page.map(this::toListDTO);
    }

    private Specification<Cargo> buildFilterSpec(String descricao, String tipo, Boolean ativo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (descricao != null && !descricao.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public CargoResponseDTO findById(Integer id) {
        Cargo c = cargoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_CARGO_NOT_FOUND", "Cargo não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(c);
    }

    @Transactional
    public CargoResponseDTO create(CargoRequestDTO dto) {
        validateCodigoUnique(dto.getCodigo(), null);
        Cargo c = toEntity(dto);
        c = cargoRepository.save(c);
        return toResponseDTO(c);
    }

    @Transactional
    public CargoResponseDTO update(Integer id, CargoRequestDTO dto) {
        Cargo c = cargoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_CARGO_NOT_FOUND", "Cargo não encontrado", HttpStatus.NOT_FOUND));
        validateCodigoUnique(dto.getCodigo(), id);
        updateEntity(c, dto);
        c = cargoRepository.save(c);
        return toResponseDTO(c);
    }

    @Transactional
    public void delete(Integer id) {
        if (!cargoRepository.existsById(id)) {
            throw new BusinessException("ME_CARGO_NOT_FOUND", "Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        cargoRepository.deleteById(id);
    }

    private void validateCodigoUnique(String codigo, Integer excludeId) {
        boolean exists = excludeId == null
                ? cargoRepository.existsByCodigo(codigo)
                : cargoRepository.existsByCodigoAndIdNot(codigo, excludeId);
        if (exists) {
            throw new BusinessException("ME_CARGO_CODIGO_DUPLICATE", "Já existe um cargo com este código");
        }
    }

    private CargoListDTO toListDTO(Cargo c) {
        return CargoListDTO.builder()
                .id(c.getId())
                .codigo(c.getCodigo())
                .descricao(c.getDescricao())
                .tipo(c.getTipo())
                .vagas(c.getVagas())
                .vagasOcupadas(c.getVagasOcupadas())
                .cargaHorariaSemanal(c.getCargaHorariaSemanal())
                .ativo(c.getAtivo())
                .build();
    }

    private CargoResponseDTO toResponseDTO(Cargo c) {
        return CargoResponseDTO.builder()
                .id(c.getId())
                .codigo(c.getCodigo())
                .descricao(c.getDescricao())
                .tipo(c.getTipo())
                .escolaridadeMinima(c.getEscolaridadeMinima())
                .cbo(c.getCbo())
                .vagas(c.getVagas())
                .vagasOcupadas(c.getVagasOcupadas())
                .cargaHorariaSemanal(c.getCargaHorariaSemanal())
                .ativo(c.getAtivo())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }

    private Cargo toEntity(CargoRequestDTO dto) {
        Cargo c = new Cargo();
        updateEntity(c, dto);
        return c;
    }

    private void updateEntity(Cargo c, CargoRequestDTO dto) {
        c.setCodigo(dto.getCodigo());
        c.setDescricao(dto.getDescricao());
        c.setTipo(dto.getTipo());
        c.setEscolaridadeMinima(dto.getEscolaridadeMinima());
        c.setCbo(dto.getCbo());
        c.setVagas(dto.getVagas());
        c.setVagasOcupadas(dto.getVagasOcupadas() != null ? dto.getVagasOcupadas() : 0);
        c.setCargaHorariaSemanal(dto.getCargaHorariaSemanal());
        c.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }
}
