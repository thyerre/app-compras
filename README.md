# 🏛️ Compras Gov — Sistema de Gestão de Compras para Prefeituras

## Visão Geral

Sistema completo para gestão de compras de prefeituras, com autenticação segura (JWT), menus dinâmicos por perfil, cadastro de fornecedores, certidões fiscais e layout responsivo.

---

## 🛠️ Stack Tecnológica

| Camada | Tecnologia |
|---|---|
| Backend | Java 21 + Spring Boot 3.4 + Spring Security 6 |
| Frontend | Angular 19 + Angular Material 19 + Tailwind CSS 3.4 |
| Banco de Dados | PostgreSQL 16 |
| Migrações | Flyway 10 |
| Autenticação | JWT (jjwt 0.12) |
| Testes Backend | JUnit 5 + Spring Boot Test |
| Testes Frontend | Jest 29 + jest-preset-angular |
| Testes E2E | Playwright |
| Containerização | Docker + Docker Compose |

---

## 📁 Estrutura do Projeto

```
app-compras/
├── docker-compose.yml
├── README.md
│
├── backend/                         # API REST — Spring Boot
│   ├── Dockerfile
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/compras/
│       ├── config/                  # SecurityConfig, CorsConfig, DataSeeder
│       ├── controller/              # AuthController, MenuController, FornecedorController, DominioController
│       ├── dto/                     # Request/Response DTOs
│       ├── entity/                  # Entidades JPA (Usuario, Role, Menu, Fornecedor, Estado, Municipio...)
│       ├── exception/               # GlobalExceptionHandler, BusinessException
│       ├── repository/              # Repositórios JPA + Specifications
│       ├── security/                # JWT (JwtService, JwtAuthenticationFilter, CustomUserDetailsService)
│       └── service/                 # AuthService, MenuService, FornecedorService, DominioService
│
└── frontend/                        # SPA — Angular 19
    ├── Dockerfile
    ├── nginx.conf
    ├── README.md
    └── src/app/
        ├── core/                    # Guards, Interceptors, Models, Services singleton
        ├── shared/
        │   ├── bases/               # BaseFormComponent, BaseListComponent, BaseService, BaseState
        │   ├── services/            # FornecedorService (HTTP)
        │   └── ui/                  # Componentes reutilizáveis (input, select, datepicker, button, filter, data-table, layout)
        └── features/
            ├── auth/                # Login + State (Elf)
            ├── dashboard/           # Tela principal
            └── fornecedores/        # Listagem, Formulário, Detalhamento
```

---

## 🚀 Como Rodar

### Pré-requisitos

- Docker e Docker Compose instalados

### Subir tudo com um comando

```bash
docker-compose up --build
```

### Acessar

| Serviço | URL |
|---|---|
| Frontend | http://localhost |
| Backend API | http://localhost:8080 |
| PostgreSQL | localhost:5432 |

### Credenciais padrão

| Campo | Valor |
|---|---|
| E-mail | admin@compras.gov.br |
| Senha | admin123 |

---

## 🔧 Desenvolvimento Local

### Backend

```bash
# Subir apenas o banco
docker-compose up postgres

# Rodar o backend
cd backend
./mvnw spring-boot:run
```

> API disponível em http://localhost:8080

### Frontend

```bash
cd frontend
npm install
npm start
```

> App disponível em http://localhost:4200 (proxy configurado para o backend em :8080)

---

## 🔒 Segurança

- Senhas armazenadas com **BCrypt** (12 rounds de hashing)
- Autenticação via **JWT** (Bearer token, expiração de 24h)
- Endpoints protegidos por **Spring Security** com RBAC
- Guards no frontend para rotas autenticadas e convidados
- Interceptor HTTP injeta token automaticamente e trata 401

---

## 💾 Persistência

O volume `postgres_data` é mapeado no Docker Compose, garantindo que os dados do banco **não são perdidos** ao reiniciar os containers.

---

## 📐 Regras de Desenvolvimento

### Gerais

1. **Nunca** exponha entidades JPA diretamente na API — sempre use DTOs.
2. **Flyway** gerencia o schema do banco; `ddl-auto: validate` no Hibernate.
3. Toda nova feature deve seguir o padrão de camadas existente.

### Backend

4. Camadas: **Controller → Service → Repository → Entity**.
5. Exceções de negócio devem usar `BusinessException`.
6. Novos endpoints devem ser documentados na seção de API do README do backend.

### Frontend

7. Todos os componentes devem ser **standalone** (`standalone: true`).
8. Usar **Signals** (`signal()`, `computed()`, `input()`, `output()`) para estado local.
9. **Não usar `mat-form-field` diretamente em features** — usar componentes de `shared/ui` (`app-input`, `app-select`, `app-datepicker`).
10. Formulários devem estender `BaseFormComponent`; listagens devem estender `BaseListComponent`.
11. Guards e interceptors devem ser **funcionais** (não class-based).
12. Features devem usar **lazy loading** via `loadComponent()`.
13. Componentes compartilhados de formulário implementam `ControlValueAccessor`.

---

## 🧪 Testes

```bash
# Backend
cd backend && ./mvnw test

# Frontend - unitários
cd frontend && npm test

# Frontend - E2E
cd frontend && npx playwright test
```

---

## 📋 API Endpoints

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/api/auth/login` | Público | Autenticar usuário |
| `GET` | `/api/menus` | Bearer | Menus do usuário por roles |
| `GET` | `/api/fornecedores` | Bearer | Listar fornecedores (paginado + filtros) |
| `GET` | `/api/fornecedores/:id` | Bearer | Detalhar fornecedor |
| `POST` | `/api/fornecedores` | Bearer | Cadastrar fornecedor |
| `PUT` | `/api/fornecedores/:id` | Bearer | Atualizar fornecedor |
| `DELETE` | `/api/fornecedores/:id` | Bearer | Excluir fornecedor |
| `GET` | `/api/dominios/estados` | Bearer | Listar estados |
| `GET` | `/api/dominios/municipios` | Bearer | Listar municípios (por estado) |
| `GET` | `/api/dominios/tipos-fornecedor` | Bearer | Listar tipos de fornecedor |
| `GET` | `/api/dominios/classificacoes-fornecedor` | Bearer | Listar classificações |
