-- =====================================================
-- V11 — Módulo de Recursos Humanos
--
-- Submódulos:
--   1. Cargos e Tabela Salarial
--   2. Tipos de Vínculo
--   3. Servidores (com login)
--   4. Dependentes
--   5. Eventos de Folha
--   6. Folha de Pagamento
--   7. Ficha Financeira
--   8. Férias
--   9. Licenças e Afastamentos
--  10. Histórico Funcional
--  11. Consignados
--
-- Base legal: Estatuto do Servidor, CLT, LC 101/2000, eSocial
-- =====================================================


-- ═══════════════════════════════════════════════════════
-- 1. CARGOS
-- ═══════════════════════════════════════════════════════

CREATE TABLE cargos (
    id                      SERIAL PRIMARY KEY,
    codigo                  VARCHAR(10)  NOT NULL,
    descricao               VARCHAR(255) NOT NULL,
    tipo                    VARCHAR(30)  NOT NULL CHECK (tipo IN ('EFETIVO','COMISSIONADO','TEMPORARIO','ELETIVO')),
    cbo                     VARCHAR(10),
    vagas                   INTEGER      NOT NULL DEFAULT 0,
    vagas_ocupadas          INTEGER      NOT NULL DEFAULT 0,
    escolaridade_minima     VARCHAR(30),
    carga_horaria_semanal   INTEGER,
    ativo                   BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(codigo)
);

COMMENT ON TABLE cargos IS 'Cargos públicos municipais — quadro de pessoal';


-- ═══════════════════════════════════════════════════════
-- 2. NÍVEIS SALARIAIS
-- ═══════════════════════════════════════════════════════

CREATE TABLE niveis_salariais (
    id                  SERIAL PRIMARY KEY,
    cargo_id            INTEGER       NOT NULL REFERENCES cargos(id),
    nivel               VARCHAR(10)   NOT NULL,
    classe              VARCHAR(10),
    referencia          VARCHAR(10),
    valor_base          NUMERIC(12,2) NOT NULL,
    vigencia_inicio     DATE          NOT NULL,
    vigencia_fim        DATE,
    ativo               BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(cargo_id, nivel, classe, referencia)
);

CREATE INDEX idx_niveis_cargo ON niveis_salariais(cargo_id);

COMMENT ON TABLE niveis_salariais IS 'Tabela salarial — níveis, classes e referências por cargo';


-- ═══════════════════════════════════════════════════════
-- 3. TIPOS DE VÍNCULO
-- ═══════════════════════════════════════════════════════

CREATE TABLE tipos_vinculo (
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(10)  NOT NULL,
    descricao   VARCHAR(100) NOT NULL,
    regime      VARCHAR(30)  NOT NULL CHECK (regime IN ('ESTATUTARIO','CLT','COMISSIONADO','TEMPORARIO','ESTAGIARIO','ELETIVO')),
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE(codigo)
);

COMMENT ON TABLE tipos_vinculo IS 'Tipos de vínculo empregatício — estatutário, CLT, comissionado, etc.';


-- ═══════════════════════════════════════════════════════
-- 4. SERVIDORES
-- ═══════════════════════════════════════════════════════

CREATE TABLE servidores (
    id                      BIGSERIAL PRIMARY KEY,
    matricula               VARCHAR(20)  NOT NULL,
    usuario_id              BIGINT       REFERENCES usuarios(id),
    nome                    VARCHAR(255) NOT NULL,
    cpf                     VARCHAR(14)  NOT NULL,
    rg                      VARCHAR(20),
    rg_orgao_emissor        VARCHAR(20),
    data_nascimento         DATE         NOT NULL,
    sexo                    VARCHAR(1)   NOT NULL CHECK (sexo IN ('M','F')),
    estado_civil            VARCHAR(20),
    nacionalidade           VARCHAR(50)  DEFAULT 'Brasileira',
    naturalidade            VARCHAR(100),
    pis_pasep               VARCHAR(15),
    titulo_eleitor          VARCHAR(15),
    zona_eleitoral          VARCHAR(5),
    secao_eleitoral         VARCHAR(5),
    ctps_numero             VARCHAR(15),
    ctps_serie              VARCHAR(10),
    cnh_numero              VARCHAR(15),
    cnh_categoria           VARCHAR(5),
    grau_instrucao          VARCHAR(30),
    email                   VARCHAR(255),
    telefone                VARCHAR(20),
    celular                 VARCHAR(20),
    -- Endereço
    cep                     VARCHAR(10),
    logradouro              VARCHAR(255),
    numero                  VARCHAR(20),
    complemento             VARCHAR(100),
    bairro                  VARCHAR(100),
    municipio_id            INTEGER REFERENCES municipios(id),
    estado_id               INTEGER REFERENCES estados(id),
    -- Dados funcionais
    cargo_id                INTEGER NOT NULL REFERENCES cargos(id),
    nivel_salarial_id       INTEGER REFERENCES niveis_salariais(id),
    tipo_vinculo_id         INTEGER NOT NULL REFERENCES tipos_vinculo(id),
    orgao_id                INTEGER REFERENCES orgaos(id),
    unidade_id              INTEGER REFERENCES unidades(id),
    data_admissao           DATE    NOT NULL,
    data_posse              DATE,
    data_exercicio          DATE,
    data_demissao           DATE,
    motivo_demissao         VARCHAR(255),
    -- Dados bancários
    banco_codigo            VARCHAR(10),
    banco_nome              VARCHAR(100),
    agencia                 VARCHAR(20),
    conta                   VARCHAR(30),
    tipo_conta              VARCHAR(20) CHECK (tipo_conta IN ('CORRENTE','POUPANCA')),
    -- Dados previdenciários
    regime_previdencia      VARCHAR(10) NOT NULL DEFAULT 'RGPS'
                            CHECK (regime_previdencia IN ('RGPS','RPPS')),
    numero_previdencia      VARCHAR(20),
    -- Status
    situacao                VARCHAR(20) NOT NULL DEFAULT 'ATIVO'
                            CHECK (situacao IN ('ATIVO','AFASTADO','FERIAS','LICENCA','APOSENTADO','DESLIGADO','CEDIDO')),
    ativo                   BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(matricula),
    UNIQUE(cpf)
);

CREATE INDEX idx_servidores_matricula ON servidores(matricula);
CREATE INDEX idx_servidores_cpf       ON servidores(cpf);
CREATE INDEX idx_servidores_nome      ON servidores(nome);
CREATE INDEX idx_servidores_cargo     ON servidores(cargo_id);
CREATE INDEX idx_servidores_orgao     ON servidores(orgao_id);
CREATE INDEX idx_servidores_unidade   ON servidores(unidade_id);
CREATE INDEX idx_servidores_situacao  ON servidores(situacao);

COMMENT ON TABLE servidores IS 'Cadastro completo de servidores públicos municipais — vínculo com usuarios para acesso ao sistema';


-- ═══════════════════════════════════════════════════════
-- 5. DEPENDENTES
-- ═══════════════════════════════════════════════════════

CREATE TABLE dependentes (
    id                          BIGSERIAL PRIMARY KEY,
    servidor_id                 BIGINT       NOT NULL REFERENCES servidores(id),
    nome                        VARCHAR(255) NOT NULL,
    cpf                         VARCHAR(14),
    data_nascimento             DATE         NOT NULL,
    parentesco                  VARCHAR(30)  NOT NULL
                                CHECK (parentesco IN ('CONJUGE','COMPANHEIRO','FILHO','ENTEADO','MENOR_TUTELADO','PAI','MAE','OUTROS')),
    sexo                        VARCHAR(1) CHECK (sexo IN ('M','F')),
    dependente_ir               BOOLEAN NOT NULL DEFAULT FALSE,
    dependente_salario_familia  BOOLEAN NOT NULL DEFAULT FALSE,
    dependente_plano_saude      BOOLEAN NOT NULL DEFAULT FALSE,
    pensao_alimenticia          BOOLEAN NOT NULL DEFAULT FALSE,
    percentual_pensao           NUMERIC(5,2),
    data_inicio_dependencia     DATE,
    data_fim_dependencia        DATE,
    ativo                       BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at                  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_dependentes_servidor ON dependentes(servidor_id);

COMMENT ON TABLE dependentes IS 'Dependentes do servidor — IR, salário-família, plano de saúde, pensão alimentícia';


-- ═══════════════════════════════════════════════════════
-- 6. EVENTOS DE FOLHA
-- ═══════════════════════════════════════════════════════

CREATE TABLE eventos_folha (
    id                  SERIAL PRIMARY KEY,
    codigo              VARCHAR(10)  NOT NULL,
    descricao           VARCHAR(255) NOT NULL,
    tipo                VARCHAR(10)  NOT NULL CHECK (tipo IN ('PROVENTO','DESCONTO')),
    incidencia_inss     BOOLEAN NOT NULL DEFAULT FALSE,
    incidencia_irrf     BOOLEAN NOT NULL DEFAULT FALSE,
    incidencia_fgts     BOOLEAN NOT NULL DEFAULT FALSE,
    automatico          BOOLEAN NOT NULL DEFAULT FALSE,
    formula             TEXT,
    percentual          NUMERIC(8,4),
    valor_fixo          NUMERIC(12,2),
    tipo_calculo        VARCHAR(20) CHECK (tipo_calculo IN ('FIXO','PERCENTUAL','HORA','FORMULA','REFERENCIA')),
    aplica_mensal       BOOLEAN NOT NULL DEFAULT TRUE,
    aplica_ferias       BOOLEAN NOT NULL DEFAULT FALSE,
    aplica_13           BOOLEAN NOT NULL DEFAULT FALSE,
    aplica_rescisao     BOOLEAN NOT NULL DEFAULT FALSE,
    ativo               BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(codigo)
);

COMMENT ON TABLE eventos_folha IS 'Eventos de folha de pagamento — proventos e descontos configuráveis';


-- ═══════════════════════════════════════════════════════
-- 7. FOLHAS DE PAGAMENTO
-- ═══════════════════════════════════════════════════════

CREATE TABLE folhas_pagamento (
    id                      BIGSERIAL PRIMARY KEY,
    competencia             VARCHAR(7)    NOT NULL,  -- YYYY-MM
    tipo                    VARCHAR(20)   NOT NULL
                            CHECK (tipo IN ('NORMAL','COMPLEMENTAR','13_SALARIO','FERIAS','RESCISAO')),
    data_calculo            TIMESTAMP,
    data_aprovacao          TIMESTAMP,
    aprovado_por            BIGINT        REFERENCES usuarios(id),
    data_pagamento          DATE,
    total_proventos         NUMERIC(18,2) NOT NULL DEFAULT 0,
    total_descontos         NUMERIC(18,2) NOT NULL DEFAULT 0,
    total_liquido           NUMERIC(18,2) NOT NULL DEFAULT 0,
    quantidade_servidores   INTEGER       NOT NULL DEFAULT 0,
    status                  VARCHAR(20)   NOT NULL DEFAULT 'ABERTA'
                            CHECK (status IN ('ABERTA','CALCULADA','APROVADA','PAGA','FECHADA')),
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(competencia, tipo)
);

COMMENT ON TABLE folhas_pagamento IS 'Folhas de pagamento processadas — mensal, complementar, 13°, férias, rescisão';


-- ═══════════════════════════════════════════════════════
-- 8. FICHAS FINANCEIRAS
-- ═══════════════════════════════════════════════════════

CREATE TABLE fichas_financeiras (
    id                      BIGSERIAL PRIMARY KEY,
    folha_pagamento_id      BIGINT       NOT NULL REFERENCES folhas_pagamento(id),
    servidor_id             BIGINT       NOT NULL REFERENCES servidores(id),
    evento_folha_id         INTEGER      NOT NULL REFERENCES eventos_folha(id),
    referencia              NUMERIC(12,4),
    valor                   NUMERIC(12,2) NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_fichas_folha    ON fichas_financeiras(folha_pagamento_id);
CREATE INDEX idx_fichas_servidor ON fichas_financeiras(servidor_id);

COMMENT ON TABLE fichas_financeiras IS 'Itens individuais da folha — ficha financeira do servidor';


-- ═══════════════════════════════════════════════════════
-- 9. FÉRIAS
-- ═══════════════════════════════════════════════════════

CREATE TABLE ferias (
    id                              BIGSERIAL PRIMARY KEY,
    servidor_id                     BIGINT       NOT NULL REFERENCES servidores(id),
    periodo_aquisitivo_inicio       DATE         NOT NULL,
    periodo_aquisitivo_fim          DATE         NOT NULL,
    periodo_gozo_inicio             DATE,
    periodo_gozo_fim                DATE,
    dias_gozo                       INTEGER,
    dias_abono                      INTEGER DEFAULT 0,
    parcelas                        INTEGER DEFAULT 1 CHECK (parcelas BETWEEN 1 AND 3),
    valor_ferias                    NUMERIC(12,2),
    valor_abono                     NUMERIC(12,2),
    valor_13_ferias                 NUMERIC(12,2),
    status                          VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
                                    CHECK (status IN ('PENDENTE','PROGRAMADA','EM_GOZO','GOZADA','INTERROMPIDA')),
    observacoes                     TEXT,
    created_at                      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ferias_servidor ON ferias(servidor_id);

COMMENT ON TABLE ferias IS 'Controle de férias — períodos aquisitivos e gozo';


-- ═══════════════════════════════════════════════════════
-- 10. LICENÇAS E AFASTAMENTOS
-- ═══════════════════════════════════════════════════════

CREATE TABLE licencas (
    id                  BIGSERIAL PRIMARY KEY,
    servidor_id         BIGINT       NOT NULL REFERENCES servidores(id),
    tipo_licenca        VARCHAR(50)  NOT NULL
                        CHECK (tipo_licenca IN (
                            'SAUDE','MATERNIDADE','PATERNIDADE','CASAMENTO','LUTO','PREMIO',
                            'INTERESSE_PARTICULAR','ATIVIDADE_POLITICA','CAPACITACAO',
                            'ACIDENTE_TRABALHO','SERVICO_MILITAR','OUTROS'
                        )),
    data_inicio         DATE         NOT NULL,
    data_fim            DATE,
    quantidade_dias     INTEGER,
    remunerada          BOOLEAN NOT NULL DEFAULT TRUE,
    cid                 VARCHAR(10),
    medico_nome         VARCHAR(255),
    medico_crm          VARCHAR(20),
    numero_documento    VARCHAR(50),
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_licencas_servidor ON licencas(servidor_id);

COMMENT ON TABLE licencas IS 'Licenças e afastamentos de servidores — saúde, maternidade, prêmio, etc.';


-- ═══════════════════════════════════════════════════════
-- 11. HISTÓRICO FUNCIONAL
-- ═══════════════════════════════════════════════════════

CREATE TABLE historico_funcional (
    id                      BIGSERIAL PRIMARY KEY,
    servidor_id             BIGINT       NOT NULL REFERENCES servidores(id),
    tipo_evento             VARCHAR(50)  NOT NULL
                            CHECK (tipo_evento IN (
                                'ADMISSAO','POSSE','EXERCICIO','PROMOCAO','PROGRESSAO',
                                'TRANSFERENCIA','CESSAO','LOTACAO','ALTERACAO_CARGO',
                                'ALTERACAO_SALARIAL','ADVERTENCIA','SUSPENSAO',
                                'DEMISSAO','APOSENTADORIA','FALECIMENTO','OUTROS'
                            )),
    data_evento             DATE    NOT NULL,
    numero_ato              VARCHAR(50),
    descricao               TEXT    NOT NULL,
    cargo_anterior_id       INTEGER REFERENCES cargos(id),
    cargo_novo_id           INTEGER REFERENCES cargos(id),
    salario_anterior        NUMERIC(12,2),
    salario_novo            NUMERIC(12,2),
    orgao_anterior_id       INTEGER REFERENCES orgaos(id),
    orgao_novo_id           INTEGER REFERENCES orgaos(id),
    unidade_anterior_id     INTEGER REFERENCES unidades(id),
    unidade_novo_id         INTEGER REFERENCES unidades(id),
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_historico_servidor ON historico_funcional(servidor_id);

COMMENT ON TABLE historico_funcional IS 'Histórico funcional do servidor — admissão, promoção, progressão, lotação, etc.';


-- ═══════════════════════════════════════════════════════
-- 12. CONSIGNADOS
-- ═══════════════════════════════════════════════════════

CREATE TABLE consignados (
    id                  BIGSERIAL PRIMARY KEY,
    servidor_id         BIGINT       NOT NULL REFERENCES servidores(id),
    evento_folha_id     INTEGER      REFERENCES eventos_folha(id),
    consignataria       VARCHAR(255) NOT NULL,
    contrato            VARCHAR(50),
    parcela_atual       INTEGER      NOT NULL DEFAULT 0,
    parcela_total       INTEGER      NOT NULL,
    valor_parcela       NUMERIC(12,2) NOT NULL,
    valor_total         NUMERIC(18,2) NOT NULL,
    data_inicio         DATE         NOT NULL,
    data_fim            DATE,
    status              VARCHAR(20)  NOT NULL DEFAULT 'ATIVO'
                        CHECK (status IN ('ATIVO','QUITADO','CANCELADO','SUSPENSO')),
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_consignados_servidor ON consignados(servidor_id);

COMMENT ON TABLE consignados IS 'Empréstimos consignados — controle de parcelas e margem consignável';


-- ═══════════════════════════════════════════════════════
-- MENUS
-- ═══════════════════════════════════════════════════════

-- Menu pai
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo)
VALUES ('Recursos Humanos', 'badge', NULL, 6, NULL, TRUE);

-- Submenus
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo) VALUES
('Cargos',              'work',             '/rh/cargos',              1, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Níveis Salariais',    'trending_up',      '/rh/niveis-salariais',    2, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Servidores',          'people',           '/rh/servidores',          3, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Dependentes',         'family_restroom',  '/rh/dependentes',         4, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Eventos de Folha',    'tune',             '/rh/eventos-folha',       5, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Folha de Pagamento',  'payments',         '/rh/folhas-pagamento',    6, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Férias',              'beach_access',     '/rh/ferias',              7, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Licenças',            'event_busy',       '/rh/licencas',            8, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Histórico Funcional', 'history',          '/rh/historico-funcional',  9, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE),
('Consignados',         'credit_card',      '/rh/consignados',        10, (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL), TRUE);

-- Vincular ao perfil ADMIN
INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id FROM menus m, roles r
WHERE r.nome = 'ADMIN'
  AND m.label = 'Recursos Humanos' AND m.parent_id IS NULL;

INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id FROM menus m, roles r
WHERE r.nome = 'ADMIN'
  AND m.parent_id = (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL);


-- ═══════════════════════════════════════════════════════
-- SEED — TIPOS DE VÍNCULO
-- ═══════════════════════════════════════════════════════

INSERT INTO tipos_vinculo (codigo, descricao, regime) VALUES
('EFE', 'Efetivo',              'ESTATUTARIO'),
('COM', 'Comissionado',         'COMISSIONADO'),
('TMP', 'Contrato Temporário',  'TEMPORARIO'),
('EST', 'Estagiário',           'ESTAGIARIO'),
('ELT', 'Agente Político',      'ELETIVO'),
('CLT', 'Celetista',            'CLT');


-- ═══════════════════════════════════════════════════════
-- SEED — EVENTOS DE FOLHA BÁSICOS
-- ═══════════════════════════════════════════════════════

INSERT INTO eventos_folha (codigo, descricao, tipo, incidencia_inss, incidencia_irrf, automatico, tipo_calculo, aplica_mensal, aplica_ferias, aplica_13, aplica_rescisao, ativo) VALUES
('001', 'Vencimento Base',                  'PROVENTO',  TRUE,  TRUE,  TRUE,  'REFERENCIA',  TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('002', 'Adicional por Tempo de Serviço',   'PROVENTO',  TRUE,  TRUE,  FALSE, 'PERCENTUAL',  TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('003', 'Gratificação de Função',           'PROVENTO',  TRUE,  TRUE,  FALSE, 'PERCENTUAL',  TRUE,  FALSE, FALSE, FALSE, TRUE),
('004', 'Adicional de Insalubridade',       'PROVENTO',  TRUE,  TRUE,  FALSE, 'PERCENTUAL',  TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('005', 'Adicional de Periculosidade',      'PROVENTO',  TRUE,  TRUE,  FALSE, 'PERCENTUAL',  TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('006', 'Horas Extras',                     'PROVENTO',  TRUE,  TRUE,  FALSE, 'HORA',        TRUE,  FALSE, FALSE, TRUE,  TRUE),
('007', 'Adicional Noturno',                'PROVENTO',  TRUE,  TRUE,  FALSE, 'PERCENTUAL',  TRUE,  FALSE, FALSE, TRUE,  TRUE),
('008', 'Férias + 1/3',                     'PROVENTO',  TRUE,  TRUE,  TRUE,  'FORMULA',     FALSE, TRUE,  FALSE, TRUE,  TRUE),
('009', '13º Salário',                      'PROVENTO',  TRUE,  TRUE,  TRUE,  'FORMULA',     FALSE, FALSE, TRUE,  TRUE,  TRUE),
('010', 'Salário Família',                  'PROVENTO',  FALSE, FALSE, TRUE,  'REFERENCIA',  TRUE,  FALSE, FALSE, FALSE, TRUE),
('101', 'INSS Servidor',                    'DESCONTO',  FALSE, FALSE, TRUE,  'FORMULA',     TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('102', 'IRRF',                             'DESCONTO',  FALSE, FALSE, TRUE,  'FORMULA',     TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('103', 'Previdência RPPS',                 'DESCONTO',  FALSE, FALSE, TRUE,  'PERCENTUAL',  TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('104', 'Pensão Alimentícia',               'DESCONTO',  FALSE, FALSE, FALSE, 'PERCENTUAL',  TRUE,  TRUE,  TRUE,  TRUE,  TRUE),
('105', 'Consignado',                       'DESCONTO',  FALSE, FALSE, FALSE, 'FIXO',        TRUE,  FALSE, FALSE, FALSE, TRUE),
('106', 'Plano de Saúde',                   'DESCONTO',  FALSE, FALSE, FALSE, 'FIXO',        TRUE,  FALSE, FALSE, FALSE, TRUE),
('107', 'Faltas',                           'DESCONTO',  FALSE, FALSE, FALSE, 'HORA',        TRUE,  FALSE, FALSE, FALSE, TRUE),
('108', 'Vale Transporte',                  'DESCONTO',  FALSE, FALSE, FALSE, 'PERCENTUAL',  TRUE,  FALSE, FALSE, FALSE, TRUE);
