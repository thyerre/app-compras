-- =============================================
-- V2: Módulo de Fornecedores
-- =============================================

-- Tabela de estados (UF)
CREATE TABLE estados (
    id      SERIAL PRIMARY KEY,
    sigla   CHAR(2)      NOT NULL UNIQUE,
    nome    VARCHAR(100) NOT NULL
);

-- Tabela de municípios
CREATE TABLE municipios (
    id         SERIAL PRIMARY KEY,
    nome       VARCHAR(200) NOT NULL,
    codigo_ibge VARCHAR(10),
    estado_id  INT NOT NULL REFERENCES estados(id)
);
CREATE INDEX idx_municipios_estado ON municipios(estado_id);
CREATE INDEX idx_municipios_nome   ON municipios(nome);

-- Tipo de fornecedor (PF / PJ)
CREATE TABLE tipos_fornecedor (
    id        SERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

-- Classificação do fornecedor
CREATE TABLE classificacoes_fornecedor (
    id        SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE
);

-- Fornecedores
CREATE TABLE fornecedores (
    id                      BIGSERIAL PRIMARY KEY,
    razao_social            VARCHAR(255) NOT NULL,
    nome_fantasia           VARCHAR(255),
    cnpj_cpf                VARCHAR(18)  NOT NULL UNIQUE,
    inscricao_estadual      VARCHAR(20),
    inscricao_municipal     VARCHAR(20),
    tipo_fornecedor_id      INT NOT NULL REFERENCES tipos_fornecedor(id),
    classificacao_id        INT REFERENCES classificacoes_fornecedor(id),

    -- Endereço
    cep                     VARCHAR(10)  NOT NULL,
    logradouro              VARCHAR(255) NOT NULL,
    numero                  VARCHAR(20)  NOT NULL,
    complemento             VARCHAR(100),
    bairro                  VARCHAR(100) NOT NULL,
    municipio_id            INT          NOT NULL REFERENCES municipios(id),
    estado_id               INT          NOT NULL REFERENCES estados(id),

    -- Contato
    telefone                VARCHAR(20),
    celular                 VARCHAR(20),
    email                   VARCHAR(255),

    -- Responsável legal
    responsavel_nome        VARCHAR(200),
    responsavel_cpf         VARCHAR(14),
    responsavel_rg          VARCHAR(20),
    responsavel_cargo       VARCHAR(100),

    -- Dados bancários
    banco_nome              VARCHAR(100),
    banco_agencia           VARCHAR(20),
    banco_conta             VARCHAR(30),

    -- Status
    ativo                   BOOLEAN   NOT NULL DEFAULT TRUE,
    observacoes             TEXT,

    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_fornecedores_cnpj   ON fornecedores(cnpj_cpf);
CREATE INDEX idx_fornecedores_nome   ON fornecedores(razao_social);
CREATE INDEX idx_fornecedores_tipo   ON fornecedores(tipo_fornecedor_id);
CREATE INDEX idx_fornecedores_mun    ON fornecedores(municipio_id);

-- Certidões fiscais (1:N)
CREATE TABLE certidoes_fiscais (
    id              BIGSERIAL PRIMARY KEY,
    fornecedor_id   BIGINT       NOT NULL REFERENCES fornecedores(id) ON DELETE CASCADE,
    nome            VARCHAR(200) NOT NULL,
    numero          VARCHAR(100),
    data_emissao    DATE,
    data_validade   DATE,
    arquivo_nome    VARCHAR(255),
    arquivo_path    VARCHAR(500),
    observacoes     TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_certidoes_fornecedor ON certidoes_fiscais(fornecedor_id);

-- Histórico de participação em licitações (1:N)
CREATE TABLE historico_licitacoes (
    id              BIGSERIAL PRIMARY KEY,
    fornecedor_id   BIGINT       NOT NULL REFERENCES fornecedores(id) ON DELETE CASCADE,
    numero_processo VARCHAR(100) NOT NULL,
    descricao       VARCHAR(500),
    data_participacao DATE,
    resultado       VARCHAR(50),
    valor           DECIMAL(15,2),
    observacoes     TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_historico_fornecedor ON historico_licitacoes(fornecedor_id);

-- =============================================
-- Dados iniciais de domínio
-- =============================================

-- Tipos de fornecedor
INSERT INTO tipos_fornecedor (descricao) VALUES
    ('Pessoa Jurídica'),
    ('Pessoa Física');

-- Classificações de fornecedor
INSERT INTO classificacoes_fornecedor (descricao) VALUES
    ('ME - Microempresa'),
    ('EPP - Empresa de Pequeno Porte'),
    ('Média Empresa'),
    ('Grande Empresa'),
    ('MEI - Microempreendedor Individual');

-- Estados brasileiros
INSERT INTO estados (sigla, nome) VALUES
    ('AC', 'Acre'), ('AL', 'Alagoas'), ('AP', 'Amapá'), ('AM', 'Amazonas'),
    ('BA', 'Bahia'), ('CE', 'Ceará'), ('DF', 'Distrito Federal'), ('ES', 'Espírito Santo'),
    ('GO', 'Goiás'), ('MA', 'Maranhão'), ('MT', 'Mato Grosso'), ('MS', 'Mato Grosso do Sul'),
    ('MG', 'Minas Gerais'), ('PA', 'Pará'), ('PB', 'Paraíba'), ('PR', 'Paraná'),
    ('PE', 'Pernambuco'), ('PI', 'Piauí'), ('RJ', 'Rio de Janeiro'), ('RN', 'Rio Grande do Norte'),
    ('RS', 'Rio Grande do Sul'), ('RO', 'Rondônia'), ('RR', 'Roraima'), ('SC', 'Santa Catarina'),
    ('SP', 'São Paulo'), ('SE', 'Sergipe'), ('TO', 'Tocantins');

-- Municípios (amostra — os demais podem ser carregados por API/import)
INSERT INTO municipios (nome, codigo_ibge, estado_id) VALUES
    ('São Paulo', '3550308', (SELECT id FROM estados WHERE sigla = 'SP')),
    ('Rio de Janeiro', '3304557', (SELECT id FROM estados WHERE sigla = 'RJ')),
    ('Belo Horizonte', '3106200', (SELECT id FROM estados WHERE sigla = 'MG')),
    ('Brasília', '5300108', (SELECT id FROM estados WHERE sigla = 'DF')),
    ('Salvador', '2927408', (SELECT id FROM estados WHERE sigla = 'BA')),
    ('Curitiba', '4106902', (SELECT id FROM estados WHERE sigla = 'PR')),
    ('Fortaleza', '2304400', (SELECT id FROM estados WHERE sigla = 'CE')),
    ('Recife', '2611606', (SELECT id FROM estados WHERE sigla = 'PE')),
    ('Porto Alegre', '4314902', (SELECT id FROM estados WHERE sigla = 'RS')),
    ('Manaus', '1302603', (SELECT id FROM estados WHERE sigla = 'AM')),
    ('Goiânia', '5208707', (SELECT id FROM estados WHERE sigla = 'GO')),
    ('Belém', '1501402', (SELECT id FROM estados WHERE sigla = 'PA')),
    ('Campinas', '3509502', (SELECT id FROM estados WHERE sigla = 'SP')),
    ('São Luís', '2111300', (SELECT id FROM estados WHERE sigla = 'MA')),
    ('Maceió', '2704302', (SELECT id FROM estados WHERE sigla = 'AL'));
