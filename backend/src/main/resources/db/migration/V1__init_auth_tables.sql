-- =============================================
-- V1: Tabelas de autenticação e menus
-- =============================================

-- Tabela de perfis/roles
CREATE TABLE roles (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(50)  NOT NULL UNIQUE,
    descricao   VARCHAR(255),
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de usuários
CREATE TABLE usuarios (
    id              BIGSERIAL    PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,  -- BCrypt hash com salt embutido
    ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela associativa usuario <-> role
CREATE TABLE usuario_roles (
    usuario_id  BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    role_id     BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, role_id)
);

-- Tabela de menus (hierárquica)
CREATE TABLE menus (
    id          BIGSERIAL    PRIMARY KEY,
    label       VARCHAR(100) NOT NULL,
    icone       VARCHAR(100),
    rota        VARCHAR(255),
    ordem       INT          NOT NULL DEFAULT 0,
    parent_id   BIGINT       REFERENCES menus(id),
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela associativa menu <-> role (quais roles podem ver qual menu)
CREATE TABLE menu_roles (
    menu_id  BIGINT NOT NULL REFERENCES menus(id) ON DELETE CASCADE,
    role_id  BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (menu_id, role_id)
);

-- Índices
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_menus_parent   ON menus(parent_id);
CREATE INDEX idx_menus_ordem    ON menus(ordem);

-- =============================================
-- Dados iniciais
-- =============================================

-- Roles padrão
INSERT INTO roles (nome, descricao) VALUES
    ('ADMIN', 'Administrador do sistema'),
    ('USUARIO', 'Usuário padrão');

-- Usuário admin (senha: admin123 – BCrypt hash)
INSERT INTO usuarios (nome, email, senha) VALUES
    ('Administrador', 'admin@compras.gov.br',
     '$2a$12$LJ3m4ys3LzHEWMqBOGC3iOT0bCRKBNFnhIOJkY6SBm1CLMDI.GnG');

-- Vincular admin à role ADMIN
INSERT INTO usuario_roles (usuario_id, role_id) VALUES
    (1, 1);

-- Menus iniciais
INSERT INTO menus (label, icone, rota, ordem, parent_id) VALUES
    ('Dashboard',      'dashboard',        '/dashboard',        1, NULL),
    ('Compras',        'shopping_cart',     NULL,                2, NULL),
    ('Solicitações',   'description',       '/compras/solicitacoes', 1, 2),
    ('Processos',      'gavel',            '/compras/processos',    2, 2),
    ('Contratos',      'assignment',       '/compras/contratos',    3, 2),
    ('Fornecedores',   'business',         '/fornecedores',     3, NULL),
    ('Relatórios',     'bar_chart',        '/relatorios',       4, NULL),
    ('Administração',  'settings',         NULL,                5, NULL),
    ('Usuários',       'people',           '/admin/usuarios',   1, 8),
    ('Perfis',         'admin_panel_settings', '/admin/perfis', 2, 8);

-- Vincular menus à role ADMIN (todos)
INSERT INTO menu_roles (menu_id, role_id)
SELECT id, 1 FROM menus;

-- Vincular menus básicos à role USUARIO
INSERT INTO menu_roles (menu_id, role_id) VALUES
    (1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2);
