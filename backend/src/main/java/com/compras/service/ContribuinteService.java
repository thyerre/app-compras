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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContribuinteService {

    private final ContribuinteRepository contribuinteRepository;
    private final MunicipioRepository municipioRepository;
    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public Page<ContribuinteListDTO> findAll(String nomeRazaoSocial, String cpfCnpj,
                                              String tipoPessoa, Boolean ativo, Pageable pageable) {
        Specification<Contribuinte> spec = buildFilterSpec(nomeRazaoSocial, cpfCnpj, tipoPessoa, ativo);
        Page<Contribuinte> page = contribuinteRepository.findAll(spec, pageable);

        if (page.isEmpty()) return page.map(c -> null);

        List<Long> ids = page.getContent().stream().map(Contribuinte::getId).toList();
        List<Contribuinte> items = contribuinteRepository.findByIdsWithAssociations(ids);
        Map<Long, Contribuinte> map = items.stream().collect(Collectors.toMap(Contribuinte::getId, Function.identity()));

        return page.map(c -> toListDTO(map.get(c.getId())));
    }

    private Specification<Contribuinte> buildFilterSpec(String nomeRazaoSocial, String cpfCnpj,
                                                         String tipoPessoa, Boolean ativo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nomeRazaoSocial != null && !nomeRazaoSocial.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nomeRazaoSocial")), "%" + nomeRazaoSocial.toLowerCase() + "%"));
            }
            if (cpfCnpj != null && !cpfCnpj.isBlank()) {
                predicates.add(cb.like(root.get("cpfCnpj"), "%" + cpfCnpj + "%"));
            }
            if (tipoPessoa != null && !tipoPessoa.isBlank()) {
                predicates.add(cb.equal(root.get("tipoPessoa"), tipoPessoa));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public ContribuinteResponseDTO findById(Long id) {
        Contribuinte c = contribuinteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_CONTRIBUINTE_NOT_FOUND", "Contribuinte não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(c);
    }

    @Transactional
    public ContribuinteResponseDTO create(ContribuinteRequestDTO dto) {
        validateCpfCnpjUnique(dto.getCpfCnpj(), null);
        Contribuinte c = toEntity(dto);
        c = contribuinteRepository.save(c);
        return toResponseDTO(c);
    }

    @Transactional
    public ContribuinteResponseDTO update(Long id, ContribuinteRequestDTO dto) {
        Contribuinte c = contribuinteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_CONTRIBUINTE_NOT_FOUND", "Contribuinte não encontrado", HttpStatus.NOT_FOUND));
        validateCpfCnpjUnique(dto.getCpfCnpj(), id);
        updateEntity(c, dto);
        c = contribuinteRepository.save(c);
        return toResponseDTO(c);
    }

    @Transactional
    public void delete(Long id) {
        if (!contribuinteRepository.existsById(id)) {
            throw new BusinessException("ME_CONTRIBUINTE_NOT_FOUND", "Contribuinte não encontrado", HttpStatus.NOT_FOUND);
        }
        contribuinteRepository.deleteById(id);
    }

    private void validateCpfCnpjUnique(String cpfCnpj, Long excludeId) {
        boolean exists = excludeId == null
                ? contribuinteRepository.existsByCpfCnpj(cpfCnpj)
                : contribuinteRepository.existsByCpfCnpjAndIdNot(cpfCnpj, excludeId);
        if (exists) {
            throw new BusinessException("ME_CONTRIBUINTE_CPF_CNPJ_DUPLICATE", "Já existe um contribuinte com este CPF/CNPJ");
        }
    }

    private ContribuinteListDTO toListDTO(Contribuinte c) {
        return ContribuinteListDTO.builder()
                .id(c.getId())
                .tipoPessoa(c.getTipoPessoa())
                .cpfCnpj(c.getCpfCnpj())
                .nomeRazaoSocial(c.getNomeRazaoSocial())
                .nomeFantasia(c.getNomeFantasia())
                .inscricaoMunicipal(c.getInscricaoMunicipal())
                .telefone(c.getTelefone())
                .email(c.getEmail())
                .municipioNome(c.getMunicipio() != null ? c.getMunicipio().getNome() : null)
                .estadoSigla(c.getEstado() != null ? c.getEstado().getSigla() : null)
                .ativo(c.getAtivo())
                .build();
    }

    private ContribuinteResponseDTO toResponseDTO(Contribuinte c) {
        return ContribuinteResponseDTO.builder()
                .id(c.getId())
                .tipoPessoa(c.getTipoPessoa())
                .cpfCnpj(c.getCpfCnpj())
                .nomeRazaoSocial(c.getNomeRazaoSocial())
                .nomeFantasia(c.getNomeFantasia())
                .inscricaoMunicipal(c.getInscricaoMunicipal())
                .email(c.getEmail())
                .telefone(c.getTelefone())
                .celular(c.getCelular())
                .cep(c.getCep())
                .logradouro(c.getLogradouro())
                .numero(c.getNumero())
                .complemento(c.getComplemento())
                .bairro(c.getBairro())
                .municipio(c.getMunicipio() != null ? MunicipioDTO.builder()
                        .id(c.getMunicipio().getId()).nome(c.getMunicipio().getNome())
                        .codigoIbge(c.getMunicipio().getCodigoIbge())
                        .estadoId(c.getMunicipio().getEstado() != null ? c.getMunicipio().getEstado().getId() : null)
                        .estadoSigla(c.getMunicipio().getEstado() != null ? c.getMunicipio().getEstado().getSigla() : null)
                        .build() : null)
                .estado(c.getEstado() != null ? EstadoDTO.builder()
                        .id(c.getEstado().getId()).sigla(c.getEstado().getSigla()).nome(c.getEstado().getNome())
                        .build() : null)
                .ativo(c.getAtivo())
                .observacoes(c.getObservacoes())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }

    private Contribuinte toEntity(ContribuinteRequestDTO dto) {
        Contribuinte c = new Contribuinte();
        updateEntity(c, dto);
        return c;
    }

    private void updateEntity(Contribuinte c, ContribuinteRequestDTO dto) {
        c.setTipoPessoa(dto.getTipoPessoa());
        c.setCpfCnpj(dto.getCpfCnpj());
        c.setNomeRazaoSocial(dto.getNomeRazaoSocial());
        c.setNomeFantasia(dto.getNomeFantasia());
        c.setInscricaoMunicipal(dto.getInscricaoMunicipal());
        c.setEmail(dto.getEmail());
        c.setTelefone(dto.getTelefone());
        c.setCelular(dto.getCelular());
        c.setCep(dto.getCep());
        c.setLogradouro(dto.getLogradouro());
        c.setNumero(dto.getNumero());
        c.setComplemento(dto.getComplemento());
        c.setBairro(dto.getBairro());

        if (dto.getMunicipioId() != null) {
            c.setMunicipio(municipioRepository.findById(dto.getMunicipioId())
                    .orElseThrow(() -> new BusinessException("ME_MUNICIPIO_NOT_FOUND", "Município não encontrado")));
        }
        if (dto.getEstadoId() != null) {
            c.setEstado(estadoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new BusinessException("ME_ESTADO_NOT_FOUND", "Estado não encontrado")));
        }

        c.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        c.setObservacoes(dto.getObservacoes());
    }
}
