-- =====================================================
-- V10 — Módulo de Arrecadação Tributária e Não Tributária
--
-- Submódulos:
--   1. Cadastro de Contribuintes (PF/PJ)
--   2. Cadastro Imobiliário (IPTU)
--   3. Atividades Econômicas (ISS/Taxas)
--   4. Tributos Configuráveis
--   5. Lançamentos Tributários
--   6. Guias de Arrecadação (DAM)
--   7. Baixas / Pagamentos
--   8. Dívida Ativa
--   9. Parcelamentos
--  10. Agentes Arrecadadores
--  11. Certidões Tributárias
--
-- Base legal: CTN, Lei 4.320/64, CTM
-- =====================================================


-- ═══════════════════════════════════════════════════════
-- 1. CADASTRO DE CONTRIBUINTES
-- ═══════════════════════════════════════════════════════

CREATE TABLE contribuintes (
    id                  BIGSERIAL PRIMARY KEY,
    tipo_pessoa         VARCHAR(2)   NOT NULL CHECK (tipo_pessoa IN ('PF','PJ')),
    cpf_cnpj            VARCHAR(18)  NOT NULL,
    nome_razao_social   VARCHAR(255) NOT NULL,
    nome_fantasia       VARCHAR(255),
    inscricao_municipal VARCHAR(20),
    email               VARCHAR(255),
    telefone            VARCHAR(20),
    celular             VARCHAR(20),
    -- Endereço
    cep                 VARCHAR(10),
    logradouro          VARCHAR(255),
    numero              VARCHAR(20),
    complemento         VARCHAR(100),
    bairro              VARCHAR(100),
    municipio_id        INTEGER REFERENCES municipios(id),
    estado_id           INTEGER REFERENCES estados(id),
    -- Status
    ativo               BOOLEAN   NOT NULL DEFAULT TRUE,
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(cpf_cnpj)
);

CREATE INDEX idx_contribuintes_cpf_cnpj ON contribuintes(cpf_cnpj);
CREATE INDEX idx_contribuintes_nome     ON contribuintes(nome_razao_social);

COMMENT ON TABLE contribuintes IS 'Cadastro de contribuintes — pessoas físicas e jurídicas do município';


-- ═══════════════════════════════════════════════════════
-- 2. CADASTRO IMOBILIÁRIO
-- ═══════════════════════════════════════════════════════

CREATE TABLE imoveis (
    id                      BIGSERIAL PRIMARY KEY,
    inscricao_imobiliaria   VARCHAR(30)  NOT NULL,
    contribuinte_id         BIGINT       NOT NULL REFERENCES contribuintes(id),
    tipo_imovel             VARCHAR(20)  NOT NULL CHECK (tipo_imovel IN ('PREDIAL','TERRITORIAL')),
    logradouro              VARCHAR(255) NOT NULL,
    numero                  VARCHAR(20),
    complemento             VARCHAR(100),
    bairro                  VARCHAR(100) NOT NULL,
    cep                     VARCHAR(10),
    area_terreno            NUMERIC(12,2),
    area_construida         NUMERIC(12,2),
    valor_venal             NUMERIC(18,2),
    uso                     VARCHAR(50),
    setor                   VARCHAR(10),
    quadra                  VARCHAR(10),
    lote                    VARCHAR(10),
    ativo                   BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(inscricao_imobiliaria)
);

CREATE INDEX idx_imoveis_contribuinte ON imoveis(contribuinte_id);

COMMENT ON TABLE imoveis IS 'Cadastro imobiliário municipal — imóveis prediais e territoriais';


-- ═══════════════════════════════════════════════════════
-- 3. ATIVIDADES ECONÔMICAS
-- ═══════════════════════════════════════════════════════

CREATE TABLE atividades_economicas (
    id                  BIGSERIAL PRIMARY KEY,
    contribuinte_id     BIGINT       NOT NULL REFERENCES contribuintes(id),
    cnae                VARCHAR(10)  NOT NULL,
    descricao           VARCHAR(500) NOT NULL,
    tipo_atividade      VARCHAR(20)  NOT NULL CHECK (tipo_atividade IN ('PRINCIPAL','SECUNDARIA')),
    data_inicio         DATE         NOT NULL,
    data_encerramento   DATE,
    alvara_numero       VARCHAR(50),
    alvara_validade     DATE,
    ativo               BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_atividades_contribuinte ON atividades_economicas(contribuinte_id);

COMMENT ON TABLE atividades_economicas IS 'Atividades econômicas de contribuintes PJ — base para ISS e taxas';


-- ═══════════════════════════════════════════════════════
-- 4. TRIBUTOS
-- ═══════════════════════════════════════════════════════

CREATE TABLE tributos (
    id                      SERIAL PRIMARY KEY,
    codigo                  VARCHAR(10)  NOT NULL,
    descricao               VARCHAR(255) NOT NULL,
    tipo                    VARCHAR(30)  NOT NULL CHECK (tipo IN ('IMPOSTO','TAXA','CONTRIBUICAO','PRECO_PUBLICO')),
    fato_gerador            TEXT,
    aliquota_padrao         NUMERIC(8,4),
    valor_fixo              NUMERIC(18,2),
    receita_classificacao   VARCHAR(20),
    ativo                   BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(codigo)
);

COMMENT ON TABLE tributos IS 'Configuração de tributos do município — IPTU, ISS, ITBI, Taxas, Contribuições';


-- ═══════════════════════════════════════════════════════
-- 5. LANÇAMENTOS TRIBUTÁRIOS
-- ═══════════════════════════════════════════════════════

CREATE TABLE lancamentos_tributarios (
    id                      BIGSERIAL PRIMARY KEY,
    numero_lancamento       VARCHAR(20)  NOT NULL,
    exercicio               INTEGER      NOT NULL,
    contribuinte_id         BIGINT       NOT NULL REFERENCES contribuintes(id),
    tributo_id              INTEGER      NOT NULL REFERENCES tributos(id),
    imovel_id               BIGINT       REFERENCES imoveis(id),
    atividade_economica_id  BIGINT       REFERENCES atividades_economicas(id),
    data_lancamento         DATE         NOT NULL,
    data_vencimento         DATE         NOT NULL,
    base_calculo            NUMERIC(18,2),
    aliquota                NUMERIC(8,4),
    valor_principal         NUMERIC(18,2) NOT NULL,
    valor_desconto          NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_juros             NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_multa             NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_total             NUMERIC(18,2) NOT NULL,
    status                  VARCHAR(20)  NOT NULL DEFAULT 'ABERTO'
                            CHECK (status IN ('ABERTO','PAGO','PARCIAL','CANCELADO','DIVIDA_ATIVA','PARCELADO')),
    historico               TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(numero_lancamento)
);

CREATE INDEX idx_lanc_trib_contribuinte ON lancamentos_tributarios(contribuinte_id);
CREATE INDEX idx_lanc_trib_exercicio    ON lancamentos_tributarios(exercicio);
CREATE INDEX idx_lanc_trib_tributo      ON lancamentos_tributarios(tributo_id);
CREATE INDEX idx_lanc_trib_status       ON lancamentos_tributarios(status);

COMMENT ON TABLE lancamentos_tributarios IS 'Lançamentos tributários — art. 142 do CTN';


-- ═══════════════════════════════════════════════════════
-- 6. GUIAS DE ARRECADAÇÃO (DAM)
-- ═══════════════════════════════════════════════════════

CREATE TABLE guias_arrecadacao (
    id                          BIGSERIAL PRIMARY KEY,
    numero_guia                 VARCHAR(30)   NOT NULL,
    lancamento_tributario_id    BIGINT        NOT NULL REFERENCES lancamentos_tributarios(id),
    contribuinte_id             BIGINT        NOT NULL REFERENCES contribuintes(id),
    data_emissao                DATE          NOT NULL,
    data_vencimento             DATE          NOT NULL,
    valor_principal             NUMERIC(18,2) NOT NULL,
    valor_desconto              NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_juros                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_multa                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_total                 NUMERIC(18,2) NOT NULL,
    codigo_barras               VARCHAR(60),
    linha_digitavel             VARCHAR(60),
    status                      VARCHAR(20)   NOT NULL DEFAULT 'EMITIDA'
                                CHECK (status IN ('EMITIDA','PAGA','VENCIDA','CANCELADA')),
    created_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(numero_guia)
);

CREATE INDEX idx_guias_lancamento   ON guias_arrecadacao(lancamento_tributario_id);
CREATE INDEX idx_guias_contribuinte ON guias_arrecadacao(contribuinte_id);
CREATE INDEX idx_guias_status       ON guias_arrecadacao(status);

COMMENT ON TABLE guias_arrecadacao IS 'Guias de arrecadação municipal (DAM) — documento de cobrança';


-- ═══════════════════════════════════════════════════════
-- 7. AGENTES ARRECADADORES
-- ═══════════════════════════════════════════════════════

CREATE TABLE agentes_arrecadadores (
    id                  SERIAL PRIMARY KEY,
    codigo              VARCHAR(10)  NOT NULL,
    descricao           VARCHAR(255) NOT NULL,
    tipo                VARCHAR(30)  NOT NULL CHECK (tipo IN ('BANCO','CARTORIO','TESOURARIA','PIX')),
    conta_bancaria_id   BIGINT       REFERENCES contas_bancarias(id),
    ativo               BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(codigo)
);

COMMENT ON TABLE agentes_arrecadadores IS 'Agentes arrecadadores — bancos, cartórios e tesouraria';


-- ═══════════════════════════════════════════════════════
-- 8. BAIXAS DE ARRECADAÇÃO
-- ═══════════════════════════════════════════════════════

CREATE TABLE baixas_arrecadacao (
    id                          BIGSERIAL PRIMARY KEY,
    guia_arrecadacao_id         BIGINT        NOT NULL REFERENCES guias_arrecadacao(id),
    agente_arrecadador_id       INTEGER       REFERENCES agentes_arrecadadores(id),
    data_pagamento              DATE          NOT NULL,
    data_credito                DATE,
    valor_pago                  NUMERIC(18,2) NOT NULL,
    valor_juros                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_multa                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_desconto              NUMERIC(18,2) NOT NULL DEFAULT 0,
    tipo_baixa                  VARCHAR(20)   NOT NULL CHECK (tipo_baixa IN ('AUTOMATICA','MANUAL','PIX')),
    autenticacao                VARCHAR(100),
    receita_orcamentaria_id     BIGINT        REFERENCES receita_orcamentaria(id),
    observacoes                 TEXT,
    created_at                  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_baixas_guia ON baixas_arrecadacao(guia_arrecadacao_id);
CREATE INDEX idx_baixas_data ON baixas_arrecadacao(data_pagamento);

COMMENT ON TABLE baixas_arrecadacao IS 'Registros de baixa/pagamento de guias de arrecadação';


-- ═══════════════════════════════════════════════════════
-- 9. DÍVIDA ATIVA
-- ═══════════════════════════════════════════════════════

CREATE TABLE divida_ativa (
    id                          BIGSERIAL PRIMARY KEY,
    numero_inscricao            VARCHAR(20)   NOT NULL,
    lancamento_tributario_id    BIGINT        NOT NULL REFERENCES lancamentos_tributarios(id),
    contribuinte_id             BIGINT        NOT NULL REFERENCES contribuintes(id),
    data_inscricao              DATE          NOT NULL,
    valor_original              NUMERIC(18,2) NOT NULL,
    valor_atualizado            NUMERIC(18,2) NOT NULL,
    valor_juros                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_multa                 NUMERIC(18,2) NOT NULL DEFAULT 0,
    fundamentacao_legal         TEXT,
    status                      VARCHAR(20)   NOT NULL DEFAULT 'INSCRITA'
                                CHECK (status IN ('INSCRITA','PARCELADA','AJUIZADA','PAGA','CANCELADA','PRESCRITA')),
    data_ajuizamento            DATE,
    numero_processo_judicial    VARCHAR(30),
    observacoes                 TEXT,
    created_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(numero_inscricao)
);

CREATE INDEX idx_divida_contribuinte ON divida_ativa(contribuinte_id);
CREATE INDEX idx_divida_status       ON divida_ativa(status);

COMMENT ON TABLE divida_ativa IS 'Inscrição de débitos em dívida ativa — art. 201 do CTN';


-- ═══════════════════════════════════════════════════════
-- 10. PARCELAMENTOS
-- ═══════════════════════════════════════════════════════

CREATE TABLE parcelamentos (
    id                          BIGSERIAL PRIMARY KEY,
    numero_parcelamento         VARCHAR(20)   NOT NULL,
    contribuinte_id             BIGINT        NOT NULL REFERENCES contribuintes(id),
    divida_ativa_id             BIGINT        REFERENCES divida_ativa(id),
    lancamento_tributario_id    BIGINT        REFERENCES lancamentos_tributarios(id),
    data_parcelamento           DATE          NOT NULL,
    valor_total                 NUMERIC(18,2) NOT NULL,
    quantidade_parcelas         INTEGER       NOT NULL,
    valor_entrada               NUMERIC(18,2) NOT NULL DEFAULT 0,
    taxa_juros                  NUMERIC(8,4)  NOT NULL DEFAULT 0,
    status                      VARCHAR(20)   NOT NULL DEFAULT 'ATIVO'
                                CHECK (status IN ('ATIVO','QUITADO','CANCELADO','INADIMPLENTE')),
    observacoes                 TEXT,
    created_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(numero_parcelamento)
);

CREATE TABLE parcelas_parcelamento (
    id                  BIGSERIAL PRIMARY KEY,
    parcelamento_id     BIGINT        NOT NULL REFERENCES parcelamentos(id),
    numero_parcela      INTEGER       NOT NULL,
    data_vencimento     DATE          NOT NULL,
    valor_parcela       NUMERIC(18,2) NOT NULL,
    valor_juros         NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_multa         NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_pago          NUMERIC(18,2),
    data_pagamento      DATE,
    status              VARCHAR(20)   NOT NULL DEFAULT 'ABERTA'
                        CHECK (status IN ('ABERTA','PAGA','VENCIDA','CANCELADA')),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(parcelamento_id, numero_parcela)
);

CREATE INDEX idx_parcelamentos_contribuinte ON parcelamentos(contribuinte_id);
CREATE INDEX idx_parcelas_parcelamento      ON parcelas_parcelamento(parcelamento_id);

COMMENT ON TABLE parcelamentos IS 'Parcelamento de débitos tributários e dívida ativa';
COMMENT ON TABLE parcelas_parcelamento IS 'Parcelas individuais do parcelamento';


-- ═══════════════════════════════════════════════════════
-- 11. CERTIDÕES TRIBUTÁRIAS
-- ═══════════════════════════════════════════════════════

CREATE TABLE certidoes_tributarias (
    id                      BIGSERIAL PRIMARY KEY,
    numero_certidao         VARCHAR(30)  NOT NULL,
    contribuinte_id         BIGINT       NOT NULL REFERENCES contribuintes(id),
    tipo                    VARCHAR(30)  NOT NULL
                            CHECK (tipo IN ('NEGATIVA','POSITIVA','POSITIVA_EFEITO_NEGATIVA')),
    finalidade              VARCHAR(255),
    data_emissao            DATE         NOT NULL,
    data_validade           DATE         NOT NULL,
    codigo_verificacao      VARCHAR(50),
    texto                   TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(numero_certidao)
);

CREATE INDEX idx_certidoes_contribuinte ON certidoes_tributarias(contribuinte_id);

COMMENT ON TABLE certidoes_tributarias IS 'Certidões tributárias emitidas — negativa, positiva ou positiva com efeito de negativa';


-- ═══════════════════════════════════════════════════════
-- MENUS
-- ═══════════════════════════════════════════════════════

-- Menu pai
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo)
VALUES ('Arrecadação', 'account_balance_wallet', NULL, 5, NULL, TRUE);

-- Submenus
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo) VALUES
('Contribuintes',            'people',          '/arrecadacao/contribuintes',            1, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Imóveis',                  'home',            '/arrecadacao/imoveis',                  2, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Atividades Econômicas',    'store',           '/arrecadacao/atividades-economicas',    3, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Tributos',                 'receipt_long',    '/arrecadacao/tributos',                 4, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Lançamentos Tributários',  'playlist_add',    '/arrecadacao/lancamentos-tributarios',  5, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Guias de Arrecadação',     'request_quote',   '/arrecadacao/guias',                    6, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Baixas',                   'check_circle',    '/arrecadacao/baixas',                   7, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Dívida Ativa',             'gavel',           '/arrecadacao/divida-ativa',             8, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Parcelamentos',            'view_list',       '/arrecadacao/parcelamentos',            9, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Agentes Arrecadadores',    'account_balance', '/arrecadacao/agentes-arrecadadores',   10, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE),
('Certidões',                'verified',        '/arrecadacao/certidoes',               11, (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL), TRUE);

-- Vincular ao perfil ADMIN
INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id FROM menus m, roles r
WHERE r.nome = 'ADMIN'
  AND m.label = 'Arrecadação' AND m.parent_id IS NULL;

INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id FROM menus m, roles r
WHERE r.nome = 'ADMIN'
  AND m.parent_id = (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL);


-- ═══════════════════════════════════════════════════════
-- SEED — TRIBUTOS PADRÃO
-- ═══════════════════════════════════════════════════════

INSERT INTO tributos (codigo, descricao, tipo, fato_gerador, aliquota_padrao, ativo) VALUES
('IPTU',  'Imposto Predial e Territorial Urbano',           'IMPOSTO',       'Propriedade de imóvel urbano',                    1.0000, TRUE),
('ISS',   'Imposto Sobre Serviços',                         'IMPOSTO',       'Prestação de serviços',                           5.0000, TRUE),
('ITBI',  'Imposto de Transmissão de Bens Imóveis',         'IMPOSTO',       'Transmissão inter vivos de bens imóveis',         2.0000, TRUE),
('TLF',   'Taxa de Licença e Funcionamento',                'TAXA',          'Exercício regular do poder de polícia',           NULL,   TRUE),
('TVSE',  'Taxa de Vigilância Sanitária',                   'TAXA',          'Exercício do poder de polícia sanitária',         NULL,   TRUE),
('COSIP', 'Contribuição para Iluminação Pública',           'CONTRIBUICAO',  'Custeio do serviço de iluminação pública',        NULL,   TRUE),
('TFE',   'Taxa de Feirantes',                              'TAXA',          'Ocupação de espaço público para comércio',        NULL,   TRUE),
('TOAP',  'Taxa de Ocupação de Área Pública',               'TAXA',          'Ocupação de área pública',                        NULL,   TRUE);
