-- =====================================================
-- V4 — Módulo de Planejamento e Orçamento
-- PPA, LDO, LOA, Estrutura Orçamentária
-- =====================================================

-- ─────────────────────────────────────────────────────
-- Tabelas de Estrutura Organizacional
-- ─────────────────────────────────────────────────────

-- Órgão
CREATE TABLE orgaos (
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(10) NOT NULL UNIQUE,
    descricao   VARCHAR(255) NOT NULL,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Unidade Orçamentária
CREATE TABLE unidades (
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(10) NOT NULL,
    descricao   VARCHAR(255) NOT NULL,
    orgao_id    INTEGER NOT NULL REFERENCES orgaos(id),
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(codigo, orgao_id)
);

-- ─────────────────────────────────────────────────────
-- Classificação Funcional (Portaria 42/1999)
-- ─────────────────────────────────────────────────────

-- Função
CREATE TABLE funcoes (
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(5) NOT NULL UNIQUE,
    descricao   VARCHAR(255) NOT NULL,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

-- Subfunção
CREATE TABLE subfuncoes (
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(5) NOT NULL UNIQUE,
    descricao   VARCHAR(255) NOT NULL,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

-- ─────────────────────────────────────────────────────
-- Classificação Programática
-- ─────────────────────────────────────────────────────

-- Programa
CREATE TABLE programas (
    id                  SERIAL PRIMARY KEY,
    codigo              VARCHAR(10) NOT NULL UNIQUE,
    descricao           VARCHAR(255) NOT NULL,
    objetivo            TEXT,
    publico_alvo        TEXT,
    tipo                VARCHAR(50) NOT NULL DEFAULT 'TEMATICO',
    indicador           VARCHAR(255),
    unidade_medida      VARCHAR(100),
    meta_fisica         NUMERIC(18,2),
    ativo               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN programas.tipo IS 'TEMATICO, GESTAO, ESPECIAL';

-- Projeto / Atividade / Operação Especial (Ação)
CREATE TABLE acoes (
    id                  SERIAL PRIMARY KEY,
    codigo              VARCHAR(10) NOT NULL UNIQUE,
    descricao           VARCHAR(255) NOT NULL,
    tipo                VARCHAR(30) NOT NULL DEFAULT 'ATIVIDADE',
    programa_id         INTEGER NOT NULL REFERENCES programas(id),
    funcao_id           INTEGER REFERENCES funcoes(id),
    subfuncao_id        INTEGER REFERENCES subfuncoes(id),
    produto             VARCHAR(255),
    unidade_medida      VARCHAR(100),
    meta_fisica         NUMERIC(18,2),
    ativo               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN acoes.tipo IS 'PROJETO, ATIVIDADE, OPERACAO_ESPECIAL';

-- ─────────────────────────────────────────────────────
-- Natureza da Despesa (classificação por natureza)
-- ─────────────────────────────────────────────────────

CREATE TABLE naturezas_despesa (
    id                  SERIAL PRIMARY KEY,
    codigo              VARCHAR(15) NOT NULL UNIQUE,
    descricao           VARCHAR(255) NOT NULL,
    categoria_economica VARCHAR(1) NOT NULL,
    grupo_despesa       VARCHAR(1) NOT NULL,
    modalidade          VARCHAR(2) NOT NULL,
    elemento            VARCHAR(2),
    subelemento         VARCHAR(2),
    ativo               BOOLEAN NOT NULL DEFAULT TRUE
);

COMMENT ON COLUMN naturezas_despesa.categoria_economica IS '3=Corrente, 4=Capital';
COMMENT ON COLUMN naturezas_despesa.grupo_despesa IS '1=Pessoal, 2=Juros, 3=Outras Correntes, 4=Investimentos, 5=Inversões, 6=Amortização';

-- ─────────────────────────────────────────────────────
-- Fontes de Recurso (conforme STN)
-- ─────────────────────────────────────────────────────

CREATE TABLE fontes_recurso (
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(10) NOT NULL UNIQUE,
    descricao   VARCHAR(255) NOT NULL,
    grupo       VARCHAR(100),
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

-- ─────────────────────────────────────────────────────
-- PPA — Plano Plurianual
-- ─────────────────────────────────────────────────────

CREATE TABLE ppas (
    id                  BIGSERIAL PRIMARY KEY,
    descricao           VARCHAR(255) NOT NULL,
    ano_inicio          INTEGER NOT NULL,
    ano_fim             INTEGER NOT NULL,
    lei_numero          VARCHAR(50),
    lei_data            DATE,
    status              VARCHAR(30) NOT NULL DEFAULT 'ELABORACAO',
    observacoes         TEXT,
    ativo               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT ck_ppa_anos CHECK (ano_fim > ano_inicio),
    UNIQUE(ano_inicio, ano_fim)
);

COMMENT ON COLUMN ppas.status IS 'ELABORACAO, VIGENTE, ENCERRADO, REVISAO';

-- Programas do PPA (associação N:N com metas quadrienais)
CREATE TABLE ppa_programas (
    id                  BIGSERIAL PRIMARY KEY,
    ppa_id              BIGINT NOT NULL REFERENCES ppas(id) ON DELETE CASCADE,
    programa_id         INTEGER NOT NULL REFERENCES programas(id),
    orgao_id            INTEGER REFERENCES orgaos(id),
    valor_global        NUMERIC(18,2) NOT NULL DEFAULT 0,
    meta_fisica_global  NUMERIC(18,2),
    observacoes         TEXT,
    UNIQUE(ppa_id, programa_id)
);

-- Metas anuais do PPA para cada programa
CREATE TABLE ppa_programa_metas (
    id                  BIGSERIAL PRIMARY KEY,
    ppa_programa_id     BIGINT NOT NULL REFERENCES ppa_programas(id) ON DELETE CASCADE,
    exercicio           INTEGER NOT NULL,
    valor_previsto      NUMERIC(18,2) NOT NULL DEFAULT 0,
    meta_fisica         NUMERIC(18,2),
    observacoes         TEXT,
    UNIQUE(ppa_programa_id, exercicio)
);

-- ─────────────────────────────────────────────────────
-- LDO — Lei de Diretrizes Orçamentárias
-- ─────────────────────────────────────────────────────

CREATE TABLE ldos (
    id                  BIGSERIAL PRIMARY KEY,
    ppa_id              BIGINT NOT NULL REFERENCES ppas(id),
    exercicio           INTEGER NOT NULL UNIQUE,
    descricao           VARCHAR(255) NOT NULL,
    lei_numero          VARCHAR(50),
    lei_data            DATE,
    meta_fiscal_receita NUMERIC(18,2),
    meta_fiscal_despesa NUMERIC(18,2),
    meta_resultado_primario  NUMERIC(18,2),
    meta_resultado_nominal   NUMERIC(18,2),
    status              VARCHAR(30) NOT NULL DEFAULT 'ELABORACAO',
    observacoes         TEXT,
    ativo               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN ldos.status IS 'ELABORACAO, APROVADA, VIGENTE, ENCERRADA';

-- Prioridades da LDO (ações priorizadas)
CREATE TABLE ldo_prioridades (
    id                  BIGSERIAL PRIMARY KEY,
    ldo_id              BIGINT NOT NULL REFERENCES ldos(id) ON DELETE CASCADE,
    acao_id             INTEGER NOT NULL REFERENCES acoes(id),
    unidade_id          INTEGER REFERENCES unidades(id),
    valor_estimado      NUMERIC(18,2),
    meta_fisica         NUMERIC(18,2),
    justificativa       TEXT,
    UNIQUE(ldo_id, acao_id)
);

-- ─────────────────────────────────────────────────────
-- LOA — Lei Orçamentária Anual
-- ─────────────────────────────────────────────────────

CREATE TABLE loas (
    id                  BIGSERIAL PRIMARY KEY,
    ldo_id              BIGINT NOT NULL REFERENCES ldos(id),
    exercicio           INTEGER NOT NULL UNIQUE,
    descricao           VARCHAR(255) NOT NULL,
    lei_numero          VARCHAR(50),
    lei_data            DATE,
    valor_total_receita NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_total_despesa NUMERIC(18,2) NOT NULL DEFAULT 0,
    status              VARCHAR(30) NOT NULL DEFAULT 'ELABORACAO',
    observacoes         TEXT,
    ativo               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN loas.status IS 'ELABORACAO, APROVADA, VIGENTE, ENCERRADA';

-- ─────────────────────────────────────────────────────
-- Dotação Orçamentária (cruzamento da LOA)
-- ─────────────────────────────────────────────────────

CREATE TABLE dotacoes_orcamentarias (
    id                      BIGSERIAL PRIMARY KEY,
    loa_id                  BIGINT NOT NULL REFERENCES loas(id) ON DELETE CASCADE,
    orgao_id                INTEGER NOT NULL REFERENCES orgaos(id),
    unidade_id              INTEGER NOT NULL REFERENCES unidades(id),
    funcao_id               INTEGER NOT NULL REFERENCES funcoes(id),
    subfuncao_id            INTEGER NOT NULL REFERENCES subfuncoes(id),
    programa_id             INTEGER NOT NULL REFERENCES programas(id),
    acao_id                 INTEGER NOT NULL REFERENCES acoes(id),
    natureza_despesa_id     INTEGER NOT NULL REFERENCES naturezas_despesa(id),
    fonte_recurso_id        INTEGER NOT NULL REFERENCES fontes_recurso(id),
    valor_inicial           NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_suplementado      NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_anulado           NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_empenhado         NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_liquidado         NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_pago              NUMERIC(18,2) NOT NULL DEFAULT 0,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_dotacao_loa ON dotacoes_orcamentarias(loa_id);
CREATE INDEX idx_dotacao_orgao ON dotacoes_orcamentarias(orgao_id);
CREATE INDEX idx_dotacao_programa ON dotacoes_orcamentarias(programa_id);

-- ─────────────────────────────────────────────────────
-- Projeção de Receitas
-- ─────────────────────────────────────────────────────

CREATE TABLE receitas_previstas (
    id                      BIGSERIAL PRIMARY KEY,
    loa_id                  BIGINT NOT NULL REFERENCES loas(id) ON DELETE CASCADE,
    codigo                  VARCHAR(20) NOT NULL,
    descricao               VARCHAR(255) NOT NULL,
    categoria_economica     VARCHAR(1) NOT NULL,
    origem                  VARCHAR(1),
    especie                 VARCHAR(2),
    desdobramento           VARCHAR(2),
    fonte_recurso_id        INTEGER REFERENCES fontes_recurso(id),
    valor_previsto          NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_arrecadado        NUMERIC(18,2) NOT NULL DEFAULT 0,
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN receitas_previstas.categoria_economica IS '1=Corrente, 2=Capital';

-- ─────────────────────────────────────────────────────
-- Dados de Semente — Funções (Portaria 42/1999)
-- ─────────────────────────────────────────────────────

INSERT INTO funcoes (codigo, descricao) VALUES
('01', 'Legislativa'),
('02', 'Judiciária'),
('03', 'Essencial à Justiça'),
('04', 'Administração'),
('05', 'Defesa Nacional'),
('06', 'Segurança Pública'),
('07', 'Relações Exteriores'),
('08', 'Assistência Social'),
('09', 'Previdência Social'),
('10', 'Saúde'),
('11', 'Trabalho'),
('12', 'Educação'),
('13', 'Cultura'),
('14', 'Direitos da Cidadania'),
('15', 'Urbanismo'),
('16', 'Habitação'),
('17', 'Saneamento'),
('18', 'Gestão Ambiental'),
('19', 'Ciência e Tecnologia'),
('20', 'Agricultura'),
('21', 'Organização Agrária'),
('22', 'Indústria'),
('23', 'Comércio e Serviços'),
('24', 'Comunicações'),
('25', 'Energia'),
('26', 'Transporte'),
('27', 'Desporto e Lazer'),
('28', 'Encargos Especiais'),
('99', 'Reserva de Contingência');

-- Subfunções (Portaria 42/1999 — seleção das mais usadas em prefeituras)
INSERT INTO subfuncoes (codigo, descricao) VALUES
('031', 'Ação Legislativa'),
('032', 'Controle Externo'),
('061', 'Ação Judiciária'),
('091', 'Defesa da Ordem Jurídica'),
('121', 'Planejamento e Orçamento'),
('122', 'Administração Geral'),
('123', 'Administração Financeira'),
('124', 'Controle Interno'),
('125', 'Normatização e Fiscalização'),
('126', 'Tecnologia da Informação'),
('127', 'Ordenamento Territorial'),
('128', 'Formação de Recursos Humanos'),
('129', 'Administração de Receitas'),
('130', 'Administração de Concessões'),
('131', 'Comunicação Social'),
('181', 'Policiamento'),
('182', 'Defesa Civil'),
('241', 'Assistência ao Idoso'),
('242', 'Assistência ao Portador de Deficiência'),
('243', 'Assistência à Criança e ao Adolescente'),
('244', 'Assistência Comunitária'),
('271', 'Previdência Básica'),
('272', 'Previdência do Regime Estatutário'),
('301', 'Atenção Básica'),
('302', 'Assistência Hospitalar e Ambulatorial'),
('303', 'Suporte Profilático e Terapêutico'),
('304', 'Vigilância Sanitária'),
('305', 'Vigilância Epidemiológica'),
('306', 'Alimentação e Nutrição'),
('331', 'Proteção e Benefícios ao Trabalhador'),
('332', 'Relações de Trabalho'),
('333', 'Empregabilidade'),
('334', 'Fomento ao Trabalho'),
('361', 'Ensino Fundamental'),
('362', 'Ensino Médio'),
('363', 'Ensino Profissional'),
('364', 'Ensino Superior'),
('365', 'Educação Infantil'),
('366', 'Educação de Jovens e Adultos'),
('367', 'Educação Especial'),
('391', 'Patrimônio Histórico, Artístico e Arqueológico'),
('392', 'Difusão Cultural'),
('421', 'Custódia e Reintegração Social'),
('422', 'Direitos Individuais, Coletivos e Difusos'),
('451', 'Infra-Estrutura Urbana'),
('452', 'Serviços Urbanos'),
('453', 'Transportes Coletivos Urbanos'),
('481', 'Habitação Rural'),
('482', 'Habitação Urbana'),
('511', 'Saneamento Básico Rural'),
('512', 'Saneamento Básico Urbano'),
('541', 'Preservação e Conservação Ambiental'),
('542', 'Controle Ambiental'),
('543', 'Recuperação de Áreas Degradadas'),
('571', 'Desenvolvimento Científico'),
('572', 'Desenvolvimento Tecnológico e Engenharia'),
('573', 'Difusão do Conhecimento Científico e Tecnológico'),
('601', 'Promoção da Produção Vegetal'),
('602', 'Promoção da Produção Animal'),
('603', 'Defesa Sanitária Vegetal'),
('604', 'Defesa Sanitária Animal'),
('605', 'Abastecimento'),
('606', 'Extensão Rural'),
('631', 'Reforma Agrária'),
('632', 'Colonização'),
('691', 'Promoção Industrial'),
('692', 'Comercialização'),
('693', 'Comércio Exterior'),
('694', 'Serviços Financeiros'),
('695', 'Turismo'),
('751', 'Conservação de Energia'),
('752', 'Energia Elétrica'),
('781', 'Transporte Aéreo'),
('782', 'Transporte Rodoviário'),
('783', 'Transporte Ferroviário'),
('784', 'Transporte Hidroviário'),
('785', 'Transportes Especiais'),
('811', 'Desporto de Rendimento'),
('812', 'Desporto Comunitário'),
('813', 'Lazer'),
('841', 'Refinanciamento da Dívida Interna'),
('842', 'Refinanciamento da Dívida Externa'),
('843', 'Serviço da Dívida Interna'),
('844', 'Serviço da Dívida Externa'),
('845', 'Outras Transferências'),
('846', 'Outros Encargos Especiais'),
('997', 'Reserva do RPPS'),
('999', 'Reserva de Contingência');

-- Naturezas de despesa mais usadas em prefeituras
INSERT INTO naturezas_despesa (codigo, descricao, categoria_economica, grupo_despesa, modalidade, elemento) VALUES
('3.1.90.04', 'Contratação por Tempo Determinado', '3', '1', '90', '04'),
('3.1.90.11', 'Vencimentos e Vantagens Fixas – Pessoal Civil', '3', '1', '90', '11'),
('3.1.90.13', 'Obrigações Patronais', '3', '1', '90', '13'),
('3.1.90.16', 'Outras Despesas Variáveis – Pessoal Civil', '3', '1', '90', '16'),
('3.1.91.13', 'Obrigações Patronais – Intra-Orçamentário', '3', '1', '91', '13'),
('3.2.90.21', 'Juros sobre a Dívida por Contrato', '3', '2', '90', '21'),
('3.3.50.41', 'Contribuições', '3', '3', '50', '41'),
('3.3.50.43', 'Subvenções Sociais', '3', '3', '50', '43'),
('3.3.71.70', 'Rateio pela Participação em Consórcio Público', '3', '3', '71', '70'),
('3.3.90.14', 'Diárias – Pessoal Civil', '3', '3', '90', '14'),
('3.3.90.30', 'Material de Consumo', '3', '3', '90', '30'),
('3.3.90.32', 'Material, Bem ou Serviço para Distribuição Gratuita', '3', '3', '90', '32'),
('3.3.90.33', 'Passagens e Despesas com Locomoção', '3', '3', '90', '33'),
('3.3.90.34', 'Outras Despesas de Pessoal – Terceirização', '3', '3', '90', '34'),
('3.3.90.36', 'Outros Serviços de Terceiros – Pessoa Física', '3', '3', '90', '36'),
('3.3.90.39', 'Outros Serviços de Terceiros – Pessoa Jurídica', '3', '3', '90', '39'),
('3.3.90.40', 'Serviços de Tecnologia da Informação', '3', '3', '90', '40'),
('3.3.90.47', 'Obrigações Tributárias e Contributivas', '3', '3', '90', '47'),
('3.3.90.48', 'Outros Auxílios Financeiros a Pessoas Físicas', '3', '3', '90', '48'),
('3.3.90.92', 'Despesas de Exercícios Anteriores', '3', '3', '90', '92'),
('3.3.90.93', 'Indenizações e Restituições', '3', '3', '90', '93'),
('4.4.90.51', 'Obras e Instalações', '4', '4', '90', '51'),
('4.4.90.52', 'Equipamentos e Material Permanente', '4', '4', '90', '52'),
('4.4.90.61', 'Aquisição de Imóveis', '4', '4', '90', '61'),
('4.5.90.65', 'Constituição ou Aumento de Capital de Empresas', '4', '5', '90', '65'),
('4.6.90.71', 'Principal da Dívida Contratual Resgatado', '4', '6', '90', '71');

-- Fontes de recurso (conforme STN — principais)
INSERT INTO fontes_recurso (codigo, descricao, grupo) VALUES
('1500', 'Recursos não Vinculados de Impostos', 'Recursos do Tesouro'),
('1501', 'Recursos não Vinculados de Impostos – MDE', 'Recursos do Tesouro'),
('1540', 'Transferências do FUNDEB – Impostos 70%', 'Transferências'),
('1541', 'Transferências do FUNDEB – Impostos 30%', 'Transferências'),
('1550', 'Transferência do Salário-Educação', 'Transferências'),
('1553', 'Transferência de Recursos do FNDE', 'Transferências'),
('1570', 'Transferências do Governo Federal – Convênios', 'Transferências'),
('1571', 'Transferências do Governo Estadual – Convênios', 'Transferências'),
('1600', 'Transferências Fundo a Fundo – SUS', 'Transferências Saúde'),
('1601', 'Transferências Fundo a Fundo – SUS – COVID', 'Transferências Saúde'),
('1602', 'Transferências Fundo a Fundo – SUS – Emendas', 'Transferências Saúde'),
('1621', 'Transferências Fundo a Fundo – SUAS', 'Transferências Assistência'),
('1660', 'Transferência de Recursos do FNAS', 'Transferências Assistência'),
('1700', 'Outras Receitas Vinculadas', 'Recursos Vinculados'),
('1701', 'Receita de Impostos – ASPS', 'Recursos Vinculados'),
('1749', 'Programa Estadual de Transporte', 'Recursos Vinculados'),
('1750', 'Recursos Vinculados ao RPPS', 'Recursos Vinculados'),
('1751', 'Recursos de Operações de Crédito', 'Recursos Vinculados'),
('1800', 'Recursos de Alienação de Bens', 'Outros'),
('1899', 'Outros Recursos não Vinculados', 'Outros');

-- ─────────────────────────────────────────────────────
-- Inserir menus do módulo de Planejamento
-- ─────────────────────────────────────────────────────

-- Menu pai: Planejamento e Orçamento
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo)
VALUES ('Planejamento', 'account_balance', NULL, 20, NULL, TRUE);

-- Submenus
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo)
VALUES
    ('Órgãos', 'corporate_fare', '/orgaos', 1, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('Unidades', 'apartment', '/unidades', 2, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('Programas', 'flag', '/programas', 3, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('Ações', 'assignment', '/acoes', 4, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('PPA', 'date_range', '/ppa', 5, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('LDO', 'description', '/ldo', 6, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('LOA', 'request_quote', '/loa', 7, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE),
    ('Receitas', 'trending_up', '/receitas', 8, (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL), TRUE);

-- Associar menus com role ADMIN
INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id
FROM menus m, roles r
WHERE m.label IN ('Planejamento', 'Órgãos', 'Unidades', 'Programas', 'Ações', 'PPA', 'LDO', 'LOA', 'Receitas')
  AND r.nome = 'ADMIN';
