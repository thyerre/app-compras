# App Compras — Backend

## Overview

RESTful API for the **Procurement Management System** (Sistema de Gestão de Compras para Prefeituras), built with **Spring Boot 3.4** and **Java 21**.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 (LTS) | Runtime & language |
| Spring Boot | 3.4.3 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | ORM & data access |
| PostgreSQL | 16 | Relational database |
| Flyway | 10.x | Database migration |
| JWT (jjwt) | 0.12.6 | Stateless authentication tokens |
| Lombok | latest | Boilerplate reduction |
| Maven | 3.9 | Build & dependency management |
| Docker | — | Containerization |

---

## Architecture

The project follows a **Layered Architecture** pattern with clear separation of concerns:

```
com.compras/
├── ComprasApplication.java            # Spring Boot entrypoint
│
├── config/                            # Configuration layer
│   ├── SecurityConfig.java            #   Spring Security & JWT filter chain
│   ├── CorsConfig.java               #   CORS policy
│   └── DataSeeder.java               #   Seed data on startup
│
├── controller/                        # Presentation layer (REST endpoints)
│   ├── AuthController.java            #   POST /api/auth/login
│   ├── MenuController.java            #   GET  /api/menus
│   ├── FornecedorController.java      #   CRUD /api/fornecedores
│   └── DominioController.java         #   GET  /api/dominios/* (estados, municípios, tipos, classificações)
│
├── dto/                               # Data Transfer Objects
│   ├── LoginRequestDTO.java           #   Login input (email + senha)
│   ├── LoginResponseDTO.java          #   Login output (token + user)
│   ├── UsuarioDTO.java                #   User projection
│   ├── MenuDTO.java                   #   Menu tree projection
│   ├── FornecedorRequestDTO.java      #   Fornecedor create/update input
│   ├── FornecedorResponseDTO.java     #   Fornecedor detail output
│   ├── FornecedorListDTO.java         #   Fornecedor listing projection
│   ├── CertidaoFiscalDTO.java         #   Certidão fiscal nested DTO
│   ├── HistoricoLicitacaoDTO.java     #   Histórico licitação nested DTO
│   ├── EstadoDTO.java                 #   Estado projection
│   ├── MunicipioDTO.java              #   Município projection
│   ├── TipoFornecedorDTO.java         #   Tipo fornecedor projection
│   └── ClassificacaoFornecedorDTO.java #  Classificação projection
│
├── entity/                            # Domain / JPA Entities
│   ├── Usuario.java                   #   Users table
│   ├── Role.java                      #   Roles table
│   ├── Menu.java                      #   Hierarchical menus
│   ├── Fornecedor.java                #   Fornecedores (suppliers)
│   ├── CertidaoFiscal.java            #   Certidões fiscais (@OneToMany → Fornecedor)
│   ├── HistoricoLicitacao.java        #   Histórico de licitações (@OneToMany → Fornecedor)
│   ├── Estado.java                    #   States lookup table
│   ├── Municipio.java                 #   Municipalities lookup table
│   ├── TipoFornecedor.java            #   Supplier type lookup table
│   └── ClassificacaoFornecedor.java   #   Supplier classification lookup table
│
├── repository/                        # Data Access layer (Spring Data JPA)
│   ├── UsuarioRepository.java
│   ├── RoleRepository.java
│   ├── MenuRepository.java
│   ├── FornecedorRepository.java      #   JpaSpecificationExecutor for dynamic filters
│   ├── CertidaoFiscalRepository.java
│   ├── HistoricoLicitacaoRepository.java
│   ├── EstadoRepository.java
│   ├── MunicipioRepository.java
│   ├── TipoFornecedorRepository.java
│   └── ClassificacaoFornecedorRepository.java
│
├── security/                          # Security infrastructure
│   ├── JwtService.java                #   Token generation / validation
│   ├── JwtAuthenticationFilter.java   #   OncePerRequestFilter for JWT
│   └── CustomUserDetailsService.java  #   UserDetailsService impl
│
├── service/                           # Business logic layer
│   ├── AuthService.java               #   Authentication logic
│   ├── MenuService.java               #   Menu tree building
│   ├── FornecedorService.java         #   Fornecedor CRUD + Specification filters
│   └── DominioService.java            #   Domain data (estados, municípios, tipos, classificações)
│
└── exception/                         # Exception handling
    ├── GlobalExceptionHandler.java    #   @RestControllerAdvice
    ├── BusinessException.java         #   Domain-specific exception
    └── ErrorResponse.java             #   Uniform error response DTO
```

---

## Design Patterns & Principles

### 1. Layered Architecture
- **Controller → Service → Repository → Entity**
- Each layer has a single responsibility and communicates only with adjacent layers.

### 2. DTO Pattern
- Entities are never exposed directly to the API.
- DTOs decouple the internal model from the external contract.
- Builder pattern (via Lombok `@Builder`) used for DTO construction.

### 3. Specification Pattern (Dynamic Filters)
- `FornecedorRepository` extends `JpaSpecificationExecutor<Fornecedor>`.
- `FornecedorService.buildFilterSpec()` builds predicates dynamically using Criteria API.
- Only non-null/non-blank filter values generate predicates, avoiding Hibernate 6 type inference bugs.
- Two-query approach for pagination with associations: first query fetches IDs, second fetches entities with `LEFT JOIN FETCH`.

### 4. Stateless JWT Authentication
- No server-side session; each request carries a signed JWT in the `Authorization: Bearer <token>` header.
- `JwtAuthenticationFilter` intercepts every request, validates the token, and sets the `SecurityContext`.
- Token expiration: **24 hours** (configurable via `app.jwt.expiration`).

### 5. Password Hashing
- **BCrypt** with strength 12 (`2^12` hashing rounds).
- Salt is automatically embedded in the hash by BCrypt.

### 6. Database Migrations
- **Flyway** manages schema versioning.
- Migration files: `V1__init_auth_tables.sql`, `V2__fornecedores_module.sql`, `V3__fix_estados_sigla_type.sql`.
- `ddl-auto: validate` ensures Hibernate only validates the schema, never modifies it.

### 7. Global Exception Handling
- `@RestControllerAdvice` captures exceptions and returns uniform JSON error responses.
- Handles: `BadCredentialsException`, `UsernameNotFoundException`, `BusinessException`, `MethodArgumentNotValidException`, and generic `Exception`.

### 8. Role-Based Access Control (RBAC)
- `ManyToMany` relationship between `Usuario` and `Role`.
- Menus are filtered by the user's roles via JPQL query.
- Spring Security method-level security enabled via `@EnableMethodSecurity`.

---

## Development Rules

1. **Never expose JPA entities** directly in controllers — always use DTOs.
2. **Flyway** manages the database schema. Never use `ddl-auto: create` or `update`.
3. **Business exceptions** must use `BusinessException` (caught by `GlobalExceptionHandler`).
4. **New endpoints** must be documented in this README's API Endpoints section.
5. **Specification pattern** must be used for paginated list endpoints with optional filters.
6. **Two-query approach** for paginated queries with `LEFT JOIN FETCH` associations.
7. Follow existing naming conventions: `*Controller`, `*Service`, `*Repository`, `*DTO`, `*Entity`.

---

## Database Schema

```
roles ──────────── usuario_roles ──────────── usuarios
  id (PK)           usuario_id (FK)            id (PK)
  nome (UNIQUE)     role_id (FK)               nome
  descricao                                    email (UNIQUE)
  ativo                                        senha (BCrypt)
                                               ativo

menus ──────────── menu_roles
  id (PK)           menu_id (FK)
  label             role_id (FK)
  icone
  rota
  ordem
  parent_id (FK → menus)    ← Self-referential hierarchy
  ativo

fornecedores ──────────────────────────── certidoes_fiscais
  id (PK)                                  id (PK)
  razao_social                             fornecedor_id (FK)
  nome_fantasia                            nome
  cnpj_cpf (UNIQUE)                        numero
  tipo_fornecedor_id (FK)                  data_emissao
  classificacao_id (FK)                    data_validade
  estado_id (FK)                           observacoes
  municipio_id (FK)
  cep, logradouro, numero...             historico_licitacoes
  telefone, celular, email                 id (PK)
  responsavel_*                            fornecedor_id (FK)
  banco_*                                  numero_processo
  ativo                                    descricao
  created_at, updated_at                   data_participacao, resultado

estados                    municipios
  id (PK)                    id (PK)
  nome                       nome
  sigla                      codigo_ibge
                             estado_id (FK)

tipos_fornecedor           classificacoes_fornecedor
  id (PK)                    id (PK)
  descricao                  descricao
```

---

## API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/auth/login` | Public | Authenticate user, returns JWT |
| `GET` | `/api/menus` | Bearer | Get user's menu tree by roles |
| `GET` | `/api/fornecedores` | Bearer | List suppliers (paginated, with filters) |
| `GET` | `/api/fornecedores/:id` | Bearer | Get supplier detail |
| `POST` | `/api/fornecedores` | Bearer | Create supplier |
| `PUT` | `/api/fornecedores/:id` | Bearer | Update supplier |
| `DELETE` | `/api/fornecedores/:id` | Bearer | Delete supplier |
| `GET` | `/api/dominios/estados` | Bearer | List states |
| `GET` | `/api/dominios/municipios?estadoId=` | Bearer | List municipalities by state |
| `GET` | `/api/dominios/tipos-fornecedor` | Bearer | List supplier types |
| `GET` | `/api/dominios/classificacoes-fornecedor` | Bearer | List supplier classifications |

### Fornecedores Filters (query params)

| Param | Type | Description |
|---|---|---|
| `razaoSocial` | string | Filter by razão social (LIKE, case-insensitive) |
| `cnpjCpf` | string | Filter by CNPJ/CPF (LIKE) |
| `tipoFornecedorId` | number | Filter by supplier type |
| `estadoId` | number | Filter by state |
| `ativo` | boolean | Filter by active status |
| `page` | number | Page number (0-based) |
| `size` | number | Page size (default: 20) |
| `sort` | string | Sort field and direction (e.g., `razaoSocial,asc`) |

---

## Configuration

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `compras_db` | Database name |
| `DB_USER` | `compras_user` | Database user |
| `DB_PASS` | `compras_pass` | Database password |
| `JWT_SECRET` | (dev key) | HMAC-SHA256 signing key |

---

## Running Locally

```bash
# With Docker Compose (recommended)
docker-compose up --build

# Without Docker (requires PostgreSQL running)
cd backend
./mvnw spring-boot:run
```

API available at `http://localhost:8080`.

---

## Testing

```bash
cd backend
./mvnw test
```

Tests use **JUnit 5** + **Spring Boot Test** + **Spring Security Test**.

---

## Seed Data

Flyway migration `V1__init_auth_tables.sql` seeds:
- **Roles**: `ADMIN`, `USUARIO`
- **Admin user**: `admin@compras.gov.br` / `admin123`
- **10 menu items** with hierarchical structure

Flyway migration `V2__fornecedores_module.sql` seeds:
- **27 states** + **5570 municipalities**
- **Supplier types**: Pessoa Física, Pessoa Jurídica, MEI, etc.
- **Classifications**: Microempresa, EPP, etc.
