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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServidorService {

    private final ServidorRepository servidorRepository;
    private final CargoRepository cargoRepository;
    private final NivelSalarialRepository nivelSalarialRepository;
    private final TipoVinculoRepository tipoVinculoRepository;
    private final MunicipioRepository municipioRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<ServidorListDTO> findAll(String nome, String matricula, String cpf,
                                          String situacao, Long cargoId, Pageable pageable) {
        Specification<Servidor> spec = buildFilterSpec(nome, matricula, cpf, situacao, cargoId);
        Page<Servidor> page = servidorRepository.findAll(spec, pageable);

        if (page.isEmpty()) return page.map(s -> null);

        List<Long> ids = page.getContent().stream().map(Servidor::getId).toList();
        List<Servidor> items = servidorRepository.findByIdsWithAssociations(ids);
        Map<Long, Servidor> map = items.stream().collect(Collectors.toMap(Servidor::getId, Function.identity()));

        return page.map(s -> toListDTO(map.get(s.getId())));
    }

    private Specification<Servidor> buildFilterSpec(String nome, String matricula, String cpf,
                                                      String situacao, Long cargoId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (matricula != null && !matricula.isBlank()) {
                predicates.add(cb.like(root.get("matricula"), "%" + matricula + "%"));
            }
            if (cpf != null && !cpf.isBlank()) {
                predicates.add(cb.like(root.get("cpf"), "%" + cpf + "%"));
            }
            if (situacao != null && !situacao.isBlank()) {
                predicates.add(cb.equal(root.get("situacao"), situacao));
            }
            if (cargoId != null) {
                predicates.add(cb.equal(root.get("cargo").get("id"), cargoId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public ServidorResponseDTO findById(Long id) {
        Servidor s = servidorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        return toResponseDTO(s);
    }

    @Transactional
    public ServidorResponseDTO create(ServidorRequestDTO dto) {
        validateMatriculaUnique(dto.getMatricula(), null);
        validateCpfUnique(dto.getCpf(), null);
        Servidor s = toEntity(dto);
        handleUsuario(s, dto);
        s = servidorRepository.save(s);
        return toResponseDTO(s);
    }

    @Transactional
    public ServidorResponseDTO update(Long id, ServidorRequestDTO dto) {
        Servidor s = servidorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND));
        validateMatriculaUnique(dto.getMatricula(), id);
        validateCpfUnique(dto.getCpf(), id);
        updateEntity(s, dto);
        handleUsuario(s, dto);
        s = servidorRepository.save(s);
        return toResponseDTO(s);
    }

    @Transactional
    public void delete(Long id) {
        if (!servidorRepository.existsById(id)) {
            throw new BusinessException("ME_SERVIDOR_NOT_FOUND", "Servidor não encontrado", HttpStatus.NOT_FOUND);
        }
        servidorRepository.deleteById(id);
    }

    private void validateMatriculaUnique(String matricula, Long excludeId) {
        boolean exists = excludeId == null
                ? servidorRepository.existsByMatricula(matricula)
                : servidorRepository.existsByMatriculaAndIdNot(matricula, excludeId);
        if (exists) {
            throw new BusinessException("ME_SERVIDOR_MATRICULA_DUPLICATE", "Já existe um servidor com esta matrícula");
        }
    }

    private void validateCpfUnique(String cpf, Long excludeId) {
        boolean exists = excludeId == null
                ? servidorRepository.existsByCpf(cpf)
                : servidorRepository.existsByCpfAndIdNot(cpf, excludeId);
        if (exists) {
            throw new BusinessException("ME_SERVIDOR_CPF_DUPLICATE", "Já existe um servidor com este CPF");
        }
    }

    private ServidorListDTO toListDTO(Servidor s) {
        return ServidorListDTO.builder()
                .id(s.getId())
                .matricula(s.getMatricula())
                .nome(s.getNome())
                .cpf(s.getCpf())
                .cargoDescricao(s.getCargo() != null ? s.getCargo().getDescricao() : null)
                .tipoVinculoDescricao(s.getTipoVinculo() != null ? s.getTipoVinculo().getDescricao() : null)
                .dataAdmissao(s.getDataAdmissao() != null ? s.getDataAdmissao().toString() : null)
                .situacao(s.getSituacao())
                .build();
    }

    private ServidorResponseDTO toResponseDTO(Servidor s) {
        ServidorResponseDTO.ServidorResponseDTOBuilder builder = ServidorResponseDTO.builder()
                .id(s.getId())
                .matricula(s.getMatricula())
                .nome(s.getNome())
                .cpf(s.getCpf())
                .rg(s.getRg())
                .rgOrgaoEmissor(s.getRgOrgaoEmissor())
                .dataNascimento(s.getDataNascimento() != null ? s.getDataNascimento().toString() : null)
                .sexo(s.getSexo())
                .estadoCivil(s.getEstadoCivil())
                .naturalidade(s.getNaturalidade())
                .nacionalidade(s.getNacionalidade())
                .email(s.getEmail())
                .telefone(s.getTelefone())
                .celular(s.getCelular())
                .cep(s.getCep())
                .logradouro(s.getLogradouro())
                .numero(s.getNumero())
                .complemento(s.getComplemento())
                .bairro(s.getBairro())
                .pisPasep(s.getPisPasep())
                .ctpsNumero(s.getCtpsNumero())
                .ctpsSerie(s.getCtpsSerie())
                .cnhNumero(s.getCnhNumero())
                .cnhCategoria(s.getCnhCategoria())
                .tituloEleitor(s.getTituloEleitor())
                .zonaEleitoral(s.getZonaEleitoral())
                .secaoEleitoral(s.getSecaoEleitoral())
                .grauInstrucao(s.getGrauInstrucao())
                .nivelSalarialId(s.getNivelSalarial() != null ? s.getNivelSalarial().getId() : null)
                .dataAdmissao(s.getDataAdmissao() != null ? s.getDataAdmissao().toString() : null)
                .dataPosse(s.getDataPosse() != null ? s.getDataPosse().toString() : null)
                .dataExercicio(s.getDataExercicio() != null ? s.getDataExercicio().toString() : null)
                .dataDemissao(s.getDataDemissao() != null ? s.getDataDemissao().toString() : null)
                .motivoDemissao(s.getMotivoDemissao())
                .bancoCodigo(s.getBancoCodigo())
                .bancoNome(s.getBancoNome())
                .agencia(s.getAgencia())
                .conta(s.getConta())
                .tipoConta(s.getTipoConta())
                .regimePrevidencia(s.getRegimePrevidencia())
                .numeroPrevidencia(s.getNumeroPrevidencia())
                .situacao(s.getSituacao())
                .ativo(s.getAtivo())
                .observacoes(s.getObservacoes())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt());

        // Dados do usuário vinculado (sem senha)
        if (s.getUsuario() != null) {
            Usuario u = s.getUsuario();
            builder.usuarioId(u.getId())
                    .usuarioEmail(u.getEmail())
                    .usuarioNome(u.getNome())
                    .usuarioAtivo(u.getAtivo())
                    .roles(u.getRoles().stream()
                            .map(Role::getNome)
                            .sorted()
                            .collect(Collectors.toList()));
        } else {
            builder.roles(Collections.emptyList());
        }

        return builder.build();
    }

    private Servidor toEntity(ServidorRequestDTO dto) {
        Servidor s = new Servidor();
        updateEntity(s, dto);
        return s;
    }

    private void handleUsuario(Servidor s, ServidorRequestDTO dto) {
        boolean hasPassword = dto.getSenhaUsuario() != null && !dto.getSenhaUsuario().isBlank();
        boolean hasRoles = dto.getRoleNames() != null && !dto.getRoleNames().isEmpty();

        // Se não informou senha nem roles e não tem usuário, não faz nada
        if (!hasPassword && !hasRoles && s.getUsuario() == null) {
            return;
        }

        String emailUsuario = dto.getEmail();
        if (emailUsuario == null || emailUsuario.isBlank()) {
            throw new BusinessException("ME_SERVIDOR_EMAIL_REQUIRED",
                    "O e-mail do servidor é obrigatório para criar acesso ao sistema");
        }

        if (s.getUsuario() == null) {
            // Criar novo usuário
            if (!hasPassword) {
                throw new BusinessException("ME_SERVIDOR_SENHA_REQUIRED",
                        "A senha é obrigatória para criar um novo acesso ao sistema");
            }

            // Verificar se já existe usuário com esse e-mail
            Optional<Usuario> existente = usuarioRepository.findByEmail(emailUsuario);
            if (existente.isPresent()) {
                throw new BusinessException("ME_SERVIDOR_EMAIL_DUPLICATE",
                        "Já existe um usuário com este e-mail");
            }

            Usuario usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(emailUsuario)
                    .senha(passwordEncoder.encode(dto.getSenhaUsuario()))
                    .ativo(true)
                    .build();

            if (hasRoles) {
                usuario.setRoles(resolveRoles(dto.getRoleNames()));
            } else {
                // Role padrão
                roleRepository.findByNome("USUARIO").ifPresent(r -> {
                    Set<Role> defaultRoles = new HashSet<>();
                    defaultRoles.add(r);
                    usuario.setRoles(defaultRoles);
                });
            }

            Usuario savedUsuario = usuarioRepository.save(usuario);
            s.setUsuario(savedUsuario);
        } else {
            // Atualizar usuário existente
            Usuario usuario = s.getUsuario();
            usuario.setNome(dto.getNome());

            // Se mudou o e-mail, verificar unicidade
            if (!usuario.getEmail().equals(emailUsuario)) {
                Optional<Usuario> existente = usuarioRepository.findByEmail(emailUsuario);
                if (existente.isPresent()) {
                    throw new BusinessException("ME_SERVIDOR_EMAIL_DUPLICATE",
                            "Já existe um usuário com este e-mail");
                }
                usuario.setEmail(emailUsuario);
            }

            // Atualizar senha somente se informada
            if (hasPassword) {
                usuario.setSenha(passwordEncoder.encode(dto.getSenhaUsuario()));
            }

            // Atualizar roles se informadas
            if (hasRoles) {
                usuario.setRoles(resolveRoles(dto.getRoleNames()));
            }

            usuarioRepository.save(usuario);
        }
    }

    private Set<Role> resolveRoles(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByNome(roleName)
                    .orElseThrow(() -> new BusinessException("ME_ROLE_NOT_FOUND",
                            "Permissão não encontrada: " + roleName));
            roles.add(role);
        }
        return roles;
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream()
                .filter(Role::getAtivo)
                .map(r -> new RoleDTO(r.getId(), r.getNome(), r.getDescricao()))
                .collect(Collectors.toList());
    }

    private void updateEntity(Servidor s, ServidorRequestDTO dto) {
        s.setMatricula(dto.getMatricula());
        s.setNome(dto.getNome());
        s.setCpf(dto.getCpf());
        s.setRg(dto.getRg());
        s.setRgOrgaoEmissor(dto.getRgOrgaoEmissor());
        if (dto.getDataNascimento() != null) s.setDataNascimento(LocalDate.parse(dto.getDataNascimento()));
        s.setSexo(dto.getSexo());
        s.setEstadoCivil(dto.getEstadoCivil());
        s.setNaturalidade(dto.getNaturalidade());
        s.setNacionalidade(dto.getNacionalidade());
        s.setEmail(dto.getEmail());
        s.setTelefone(dto.getTelefone());
        s.setCelular(dto.getCelular());
        s.setCep(dto.getCep());
        s.setLogradouro(dto.getLogradouro());
        s.setNumero(dto.getNumero());
        s.setComplemento(dto.getComplemento());
        s.setBairro(dto.getBairro());

        if (dto.getMunicipioId() != null) {
            s.setMunicipio(municipioRepository.findById(dto.getMunicipioId())
                    .orElseThrow(() -> new BusinessException("ME_MUNICIPIO_NOT_FOUND", "Município não encontrado")));
        }
        if (dto.getEstadoId() != null) {
            s.setEstado(estadoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new BusinessException("ME_ESTADO_NOT_FOUND", "Estado não encontrado")));
        }

        s.setPisPasep(dto.getPisPasep());
        s.setCtpsNumero(dto.getCtpsNumero());
        s.setCtpsSerie(dto.getCtpsSerie());
        s.setTituloEleitor(dto.getTituloEleitor());
        s.setZonaEleitoral(dto.getZonaEleitoral());
        s.setSecaoEleitoral(dto.getSecaoEleitoral());
        s.setGrauInstrucao(dto.getGrauInstrucao());

        s.setCargo(cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new BusinessException("ME_CARGO_NOT_FOUND", "Cargo não encontrado")));
        if (dto.getNivelSalarialId() != null) {
            s.setNivelSalarial(nivelSalarialRepository.findById(dto.getNivelSalarialId())
                    .orElseThrow(() -> new BusinessException("ME_NIVEL_SALARIAL_NOT_FOUND", "Nível salarial não encontrado")));
        }
        s.setTipoVinculo(tipoVinculoRepository.findById(dto.getTipoVinculoId())
                .orElseThrow(() -> new BusinessException("ME_TIPO_VINCULO_NOT_FOUND", "Tipo de vínculo não encontrado")));

        s.setDataAdmissao(LocalDate.parse(dto.getDataAdmissao()));
        if (dto.getDataPosse() != null && !dto.getDataPosse().isBlank()) s.setDataPosse(LocalDate.parse(dto.getDataPosse()));
        if (dto.getDataExercicio() != null && !dto.getDataExercicio().isBlank()) s.setDataExercicio(LocalDate.parse(dto.getDataExercicio()));
        if (dto.getDataDemissao() != null && !dto.getDataDemissao().isBlank()) s.setDataDemissao(LocalDate.parse(dto.getDataDemissao()));
        s.setMotivoDemissao(dto.getMotivoDemissao());

        s.setBancoCodigo(dto.getBancoCodigo());
        s.setBancoNome(dto.getBancoNome());
        s.setAgencia(dto.getAgencia());
        s.setConta(dto.getConta());
        s.setTipoConta(dto.getTipoConta());
        s.setRegimePrevidencia(dto.getRegimePrevidencia());
        s.setNumeroPrevidencia(dto.getNumeroPrevidencia());
        s.setSituacao(dto.getSituacao() != null ? dto.getSituacao() : "ATIVO");
        s.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        s.setObservacoes(dto.getObservacoes());
    }
}
