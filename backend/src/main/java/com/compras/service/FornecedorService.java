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
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final TipoFornecedorRepository tipoFornecedorRepository;
    private final ClassificacaoFornecedorRepository classificacaoRepository;
    private final MunicipioRepository municipioRepository;
    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public Page<FornecedorListDTO> findAll(String razaoSocial, String cnpjCpf,
                                            Integer tipoFornecedorId, Integer estadoId,
                                            Boolean ativo, Pageable pageable) {
        Specification<Fornecedor> spec = buildFilterSpec(razaoSocial, cnpjCpf, tipoFornecedorId, estadoId, ativo);

        Page<Fornecedor> page = fornecedorRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            return page.map(f -> null);
        }

        List<Long> ids = page.getContent().stream().map(Fornecedor::getId).toList();
        List<Fornecedor> fornecedores = fornecedorRepository.findByIdsWithAssociations(ids);

        Map<Long, Fornecedor> fornecedorMap = fornecedores.stream()
                .collect(Collectors.toMap(Fornecedor::getId, Function.identity()));

        return page.map(f -> toListDTO(fornecedorMap.get(f.getId())));
    }

    private Specification<Fornecedor> buildFilterSpec(String razaoSocial, String cnpjCpf,
                                                       Integer tipoFornecedorId, Integer estadoId,
                                                       Boolean ativo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (razaoSocial != null && !razaoSocial.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("razaoSocial")),
                        "%" + razaoSocial.toLowerCase() + "%"));
            }
            if (cnpjCpf != null && !cnpjCpf.isBlank()) {
                predicates.add(cb.like(root.get("cnpjCpf"), "%" + cnpjCpf + "%"));
            }
            if (tipoFornecedorId != null) {
                predicates.add(cb.equal(root.get("tipoFornecedor").get("id"), tipoFornecedorId));
            }
            if (estadoId != null) {
                predicates.add(cb.equal(root.get("estado").get("id"), estadoId));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public FornecedorResponseDTO findById(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_FORNECEDOR_NOT_FOUND",
                        "Fornecedor não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(fornecedor);
    }

    @Transactional
    public FornecedorResponseDTO create(FornecedorRequestDTO dto) {
        validateCnpjCpfUnique(dto.getCnpjCpf(), null);
        Fornecedor fornecedor = toEntity(dto);
        fornecedor = fornecedorRepository.save(fornecedor);
        syncCertidoes(fornecedor, dto.getCertidoes());
        return toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    @Transactional
    public FornecedorResponseDTO update(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_FORNECEDOR_NOT_FOUND",
                        "Fornecedor não encontrado", HttpStatus.NOT_FOUND));

        validateCnpjCpfUnique(dto.getCnpjCpf(), id);
        updateEntity(fornecedor, dto);
        syncCertidoes(fornecedor, dto.getCertidoes());
        fornecedor = fornecedorRepository.save(fornecedor);
        return toResponseDTO(fornecedor);
    }

    @Transactional
    public void delete(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new BusinessException("ME_FORNECEDOR_NOT_FOUND",
                    "Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        fornecedorRepository.deleteById(id);
    }

    // ───── Validations ─────

    private void validateCnpjCpfUnique(String cnpjCpf, Long excludeId) {
        boolean exists = excludeId == null
                ? fornecedorRepository.existsByCnpjCpf(cnpjCpf)
                : fornecedorRepository.existsByCnpjCpfAndIdNot(cnpjCpf, excludeId);
        if (exists) {
            throw new BusinessException("ME_FORNECEDOR_CNPJ_DUPLICATE",
                    "Já existe um fornecedor com este CNPJ/CPF");
        }
    }

    // ───── Certidões sync ─────

    private void syncCertidoes(Fornecedor fornecedor, List<CertidaoFiscalDTO> dtos) {
        if (dtos == null) return;

        fornecedor.getCertidoes().clear();
        for (CertidaoFiscalDTO dto : dtos) {
            CertidaoFiscal certidao = CertidaoFiscal.builder()
                    .fornecedor(fornecedor)
                    .nome(dto.getNome())
                    .numero(dto.getNumero())
                    .dataEmissao(dto.getDataEmissao())
                    .dataValidade(dto.getDataValidade())
                    .arquivoNome(dto.getArquivoNome())
                    .arquivoPath(dto.getArquivoPath())
                    .observacoes(dto.getObservacoes())
                    .build();
            fornecedor.getCertidoes().add(certidao);
        }
    }

    // ───── Mapping Entity → DTO ─────

    private FornecedorListDTO toListDTO(Fornecedor f) {
        return FornecedorListDTO.builder()
                .id(f.getId())
                .razaoSocial(f.getRazaoSocial())
                .nomeFantasia(f.getNomeFantasia())
                .cnpjCpf(f.getCnpjCpf())
                .tipoFornecedor(f.getTipoFornecedor() != null ? f.getTipoFornecedor().getDescricao() : null)
                .classificacao(f.getClassificacao() != null ? f.getClassificacao().getDescricao() : null)
                .municipio(f.getMunicipio() != null ? f.getMunicipio().getNome() : null)
                .estadoSigla(f.getEstado() != null ? f.getEstado().getSigla() : null)
                .telefone(f.getTelefone())
                .email(f.getEmail())
                .ativo(f.getAtivo())
                .build();
    }

    private FornecedorResponseDTO toResponseDTO(Fornecedor f) {
        return FornecedorResponseDTO.builder()
                .id(f.getId())
                .razaoSocial(f.getRazaoSocial())
                .nomeFantasia(f.getNomeFantasia())
                .cnpjCpf(f.getCnpjCpf())
                .inscricaoEstadual(f.getInscricaoEstadual())
                .inscricaoMunicipal(f.getInscricaoMunicipal())
                .tipoFornecedor(f.getTipoFornecedor() != null ? TipoFornecedorDTO.builder()
                        .id(f.getTipoFornecedor().getId())
                        .descricao(f.getTipoFornecedor().getDescricao())
                        .build() : null)
                .classificacao(f.getClassificacao() != null ? ClassificacaoFornecedorDTO.builder()
                        .id(f.getClassificacao().getId())
                        .descricao(f.getClassificacao().getDescricao())
                        .build() : null)
                .cep(f.getCep())
                .logradouro(f.getLogradouro())
                .numero(f.getNumero())
                .complemento(f.getComplemento())
                .bairro(f.getBairro())
                .municipio(f.getMunicipio() != null ? MunicipioDTO.builder()
                        .id(f.getMunicipio().getId())
                        .nome(f.getMunicipio().getNome())
                        .codigoIbge(f.getMunicipio().getCodigoIbge())
                        .estadoId(f.getMunicipio().getEstado() != null ? f.getMunicipio().getEstado().getId() : null)
                        .estadoSigla(f.getMunicipio().getEstado() != null ? f.getMunicipio().getEstado().getSigla() : null)
                        .build() : null)
                .estado(f.getEstado() != null ? EstadoDTO.builder()
                        .id(f.getEstado().getId())
                        .sigla(f.getEstado().getSigla())
                        .nome(f.getEstado().getNome())
                        .build() : null)
                .telefone(f.getTelefone())
                .celular(f.getCelular())
                .email(f.getEmail())
                .responsavelNome(f.getResponsavelNome())
                .responsavelCpf(f.getResponsavelCpf())
                .responsavelRg(f.getResponsavelRg())
                .responsavelCargo(f.getResponsavelCargo())
                .bancoNome(f.getBancoNome())
                .bancoAgencia(f.getBancoAgencia())
                .bancoConta(f.getBancoConta())
                .ativo(f.getAtivo())
                .observacoes(f.getObservacoes())
                .createdAt(f.getCreatedAt())
                .updatedAt(f.getUpdatedAt())
                .certidoes(f.getCertidoes() != null ? f.getCertidoes().stream()
                        .map(c -> CertidaoFiscalDTO.builder()
                                .id(c.getId())
                                .nome(c.getNome())
                                .numero(c.getNumero())
                                .dataEmissao(c.getDataEmissao())
                                .dataValidade(c.getDataValidade())
                                .arquivoNome(c.getArquivoNome())
                                .arquivoPath(c.getArquivoPath())
                                .observacoes(c.getObservacoes())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .historicoLicitacoes(f.getHistoricoLicitacoes() != null ? f.getHistoricoLicitacoes().stream()
                        .map(h -> HistoricoLicitacaoDTO.builder()
                                .id(h.getId())
                                .numeroProcesso(h.getNumeroProcesso())
                                .descricao(h.getDescricao())
                                .dataParticipacao(h.getDataParticipacao())
                                .resultado(h.getResultado())
                                .valor(h.getValor())
                                .observacoes(h.getObservacoes())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    // ───── Mapping DTO → Entity ─────

    private Fornecedor toEntity(FornecedorRequestDTO dto) {
        Fornecedor f = new Fornecedor();
        updateEntity(f, dto);
        return f;
    }

    private void updateEntity(Fornecedor f, FornecedorRequestDTO dto) {
        f.setRazaoSocial(dto.getRazaoSocial());
        f.setNomeFantasia(dto.getNomeFantasia());
        f.setCnpjCpf(dto.getCnpjCpf());
        f.setInscricaoEstadual(dto.getInscricaoEstadual());
        f.setInscricaoMunicipal(dto.getInscricaoMunicipal());

        TipoFornecedor tipo = tipoFornecedorRepository.findById(dto.getTipoFornecedorId())
                .orElseThrow(() -> new BusinessException("ME_TIPO_FORNECEDOR_NOT_FOUND",
                        "Tipo de fornecedor não encontrado"));
        f.setTipoFornecedor(tipo);

        if (dto.getClassificacaoId() != null) {
            ClassificacaoFornecedor classificacao = classificacaoRepository.findById(dto.getClassificacaoId())
                    .orElseThrow(() -> new BusinessException("ME_CLASSIFICACAO_NOT_FOUND",
                            "Classificação não encontrada"));
            f.setClassificacao(classificacao);
        } else {
            f.setClassificacao(null);
        }

        f.setCep(dto.getCep());
        f.setLogradouro(dto.getLogradouro());
        f.setNumero(dto.getNumero());
        f.setComplemento(dto.getComplemento());
        f.setBairro(dto.getBairro());

        Municipio municipio = municipioRepository.findById(dto.getMunicipioId())
                .orElseThrow(() -> new BusinessException("ME_MUNICIPIO_NOT_FOUND",
                        "Município não encontrado"));
        f.setMunicipio(municipio);

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new BusinessException("ME_ESTADO_NOT_FOUND",
                        "Estado não encontrado"));
        f.setEstado(estado);

        f.setTelefone(dto.getTelefone());
        f.setCelular(dto.getCelular());
        f.setEmail(dto.getEmail());
        f.setResponsavelNome(dto.getResponsavelNome());
        f.setResponsavelCpf(dto.getResponsavelCpf());
        f.setResponsavelRg(dto.getResponsavelRg());
        f.setResponsavelCargo(dto.getResponsavelCargo());
        f.setBancoNome(dto.getBancoNome());
        f.setBancoAgencia(dto.getBancoAgencia());
        f.setBancoConta(dto.getBancoConta());
        f.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        f.setObservacoes(dto.getObservacoes());
    }
}
