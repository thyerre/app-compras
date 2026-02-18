-- =====================================================
-- V5 — Módulo de Compras e Licitações
-- Processos licitatórios, empenhos, integração orçamentária
-- =====================================================

-- ─────────────────────────────────────────────────────
-- Modalidade de Licitação (Lei 14.133/2021)
-- ─────────────────────────────────────────────────────

CREATE TABLE modalidades_licitacao (
    id          SERIAL PRIMARY KEY,
    descricao   VARCHAR(100) NOT NULL UNIQUE,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO modalidades_licitacao (descricao) VALUES
('Pregão Eletrônico'),
('Pregão Presencial'),
('Concorrência'),
('Concurso'),
('Leilão'),
('Diálogo Competitivo'),
('Dispensa de Licitação'),
('Inexigibilidade'),
('Dispensa Eletrônica'),
('Chamada Pública');

-- ─────────────────────────────────────────────────────
-- Tipo de Julgamento
-- ─────────────────────────────────────────────────────

CREATE TABLE tipos_julgamento (
    id          SERIAL PRIMARY KEY,
    descricao   VARCHAR(100) NOT NULL UNIQUE,
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO tipos_julgamento (descricao) VALUES
('Menor Preço'),
('Maior Desconto'),
('Melhor Técnica'),
('Técnica e Preço'),
('Maior Lance'),
('Maior Retorno Econômico');

-- ─────────────────────────────────────────────────────
-- Status do Processo
-- ─────────────────────────────────────────────────────

CREATE TABLE status_processo (
    id          SERIAL PRIMARY KEY,
    descricao   VARCHAR(100) NOT NULL UNIQUE,
    cor         VARCHAR(7),
    ativo       BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO status_processo (descricao, cor) VALUES
('Rascunho', '#9E9E9E'),
('Em Elaboração', '#2196F3'),
('Aguardando Parecer Jurídico', '#FF9800'),
('Parecer Jurídico Emitido', '#00BCD4'),
('Publicado', '#4CAF50'),
('Em Andamento', '#3F51B5'),
('Suspenso', '#F44336'),
('Deserto', '#795548'),
('Fracassado', '#E91E63'),
('Homologado', '#8BC34A'),
('Adjudicado', '#009688'),
('Anulado', '#F44336'),
('Revogado', '#FF5722'),
('Concluído', '#4CAF50'),
('Arquivado', '#607D8B');

-- ─────────────────────────────────────────────────────
-- Processo de Compras / Licitação
-- ─────────────────────────────────────────────────────

CREATE TABLE processos_compra (
    id                      BIGSERIAL PRIMARY KEY,
    numero_processo         VARCHAR(30) NOT NULL UNIQUE,
    ano                     INTEGER NOT NULL,
    objeto                  TEXT NOT NULL,
    justificativa           TEXT,
    modalidade_id           INTEGER NOT NULL REFERENCES modalidades_licitacao(id),
    tipo_julgamento_id      INTEGER REFERENCES tipos_julgamento(id),
    status_id               INTEGER NOT NULL REFERENCES status_processo(id),
    
    -- Classificação orçamentária (integração com orçamento)
    dotacao_id              BIGINT REFERENCES dotacoes_orcamentarias(id),
    orgao_id                INTEGER REFERENCES orgaos(id),
    unidade_id              INTEGER REFERENCES unidades(id),
    
    -- Valores
    valor_estimado          NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_homologado        NUMERIC(18,2),
    valor_economizado       NUMERIC(18,2),
    
    -- Datas
    data_abertura           DATE,
    data_publicacao         DATE,
    data_encerramento       DATE,
    data_homologacao        DATE,
    data_adjudicacao        DATE,
    
    -- Publicação
    numero_edital           VARCHAR(50),
    diario_oficial          VARCHAR(255),
    
    -- Responsáveis
    pregoeiro_nome          VARCHAR(200),
    pregoeiro_matricula     VARCHAR(50),
    
    -- Controle
    observacoes             TEXT,
    ativo                   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_processo_numero ON processos_compra(numero_processo);
CREATE INDEX idx_processo_ano ON processos_compra(ano);
CREATE INDEX idx_processo_modalidade ON processos_compra(modalidade_id);
CREATE INDEX idx_processo_status ON processos_compra(status_id);

-- ─────────────────────────────────────────────────────
-- Itens do Processo de Compras
-- ─────────────────────────────────────────────────────

CREATE TABLE processo_itens (
    id                      BIGSERIAL PRIMARY KEY,
    processo_id             BIGINT NOT NULL REFERENCES processos_compra(id) ON DELETE CASCADE,
    numero_item             INTEGER NOT NULL,
    descricao               TEXT NOT NULL,
    unidade_medida          VARCHAR(50) NOT NULL,
    quantidade              NUMERIC(18,4) NOT NULL,
    valor_unitario_estimado NUMERIC(18,4),
    valor_total_estimado    NUMERIC(18,2),
    valor_unitario_homologado NUMERIC(18,4),
    valor_total_homologado  NUMERIC(18,2),
    fornecedor_vencedor_id  BIGINT REFERENCES fornecedores(id),
    marca                   VARCHAR(255),
    situacao                VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(processo_id, numero_item)
);

COMMENT ON COLUMN processo_itens.situacao IS 'PENDENTE, ADJUDICADO, DESERTO, FRACASSADO, CANCELADO, REVOGADO';

-- ─────────────────────────────────────────────────────
-- Participantes / Propostas
-- ─────────────────────────────────────────────────────

CREATE TABLE processo_participantes (
    id                      BIGSERIAL PRIMARY KEY,
    processo_id             BIGINT NOT NULL REFERENCES processos_compra(id) ON DELETE CASCADE,
    fornecedor_id           BIGINT NOT NULL REFERENCES fornecedores(id),
    situacao                VARCHAR(50) NOT NULL DEFAULT 'HABILITADO',
    data_habilitacao        DATE,
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(processo_id, fornecedor_id)
);

COMMENT ON COLUMN processo_participantes.situacao IS 'HABILITADO, INABILITADO, DESCLASSIFICADO, VENCEDOR';

CREATE TABLE processo_propostas (
    id                      BIGSERIAL PRIMARY KEY,
    item_id                 BIGINT NOT NULL REFERENCES processo_itens(id) ON DELETE CASCADE,
    participante_id         BIGINT NOT NULL REFERENCES processo_participantes(id) ON DELETE CASCADE,
    valor_unitario          NUMERIC(18,4) NOT NULL,
    valor_total             NUMERIC(18,2) NOT NULL,
    marca                   VARCHAR(255),
    classificacao           INTEGER,
    vencedor                BOOLEAN NOT NULL DEFAULT FALSE,
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(item_id, participante_id)
);

-- ─────────────────────────────────────────────────────
-- Documentos do Processo
-- ─────────────────────────────────────────────────────

CREATE TABLE processo_documentos (
    id                      BIGSERIAL PRIMARY KEY,
    processo_id             BIGINT NOT NULL REFERENCES processos_compra(id) ON DELETE CASCADE,
    tipo                    VARCHAR(100) NOT NULL,
    descricao               VARCHAR(255) NOT NULL,
    arquivo_nome            VARCHAR(255),
    arquivo_path            VARCHAR(500),
    data_documento          DATE,
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN processo_documentos.tipo IS 'EDITAL, TERMO_REFERENCIA, PARECER_JURIDICO, ATA, HOMOLOGACAO, ADJUDICACAO, CONTRATO, OUTROS';

-- ─────────────────────────────────────────────────────
-- Empenhos
-- ─────────────────────────────────────────────────────

CREATE TABLE empenhos (
    id                      BIGSERIAL PRIMARY KEY,
    numero_empenho          VARCHAR(30) NOT NULL UNIQUE,
    exercicio               INTEGER NOT NULL,
    tipo                    VARCHAR(30) NOT NULL DEFAULT 'ORDINARIO',
    
    -- Vinculação
    processo_id             BIGINT REFERENCES processos_compra(id),
    fornecedor_id           BIGINT NOT NULL REFERENCES fornecedores(id),
    dotacao_id              BIGINT NOT NULL REFERENCES dotacoes_orcamentarias(id),
    
    -- Valores
    valor_empenhado         NUMERIC(18,2) NOT NULL,
    valor_liquidado         NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_pago              NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_anulado           NUMERIC(18,2) NOT NULL DEFAULT 0,
    
    -- Datas
    data_empenho            DATE NOT NULL,
    data_liquidacao         DATE,
    data_pagamento          DATE,
    
    -- Informações
    descricao               TEXT NOT NULL,
    status                  VARCHAR(30) NOT NULL DEFAULT 'EMITIDO',
    observacoes             TEXT,
    
    ativo                   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN empenhos.tipo IS 'ORDINARIO, ESTIMATIVO, GLOBAL';
COMMENT ON COLUMN empenhos.status IS 'EMITIDO, LIQUIDADO, PAGO, ANULADO, PARCIALMENTE_LIQUIDADO, PARCIALMENTE_PAGO';

CREATE INDEX idx_empenho_numero ON empenhos(numero_empenho);
CREATE INDEX idx_empenho_exercicio ON empenhos(exercicio);
CREATE INDEX idx_empenho_processo ON empenhos(processo_id);
CREATE INDEX idx_empenho_fornecedor ON empenhos(fornecedor_id);

-- ─────────────────────────────────────────────────────
-- Histórico / Tramitação do Processo
-- ─────────────────────────────────────────────────────

CREATE TABLE processo_historico (
    id                      BIGSERIAL PRIMARY KEY,
    processo_id             BIGINT NOT NULL REFERENCES processos_compra(id) ON DELETE CASCADE,
    status_anterior_id      INTEGER REFERENCES status_processo(id),
    status_novo_id          INTEGER NOT NULL REFERENCES status_processo(id),
    descricao               TEXT NOT NULL,
    usuario_nome            VARCHAR(200),
    data_registro           TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ─────────────────────────────────────────────────────
-- Inserir menus do módulo de Compras e Licitações
-- Reutiliza o menu pai "Compras" já criado na V1 (id=2)
-- ─────────────────────────────────────────────────────

-- Desativar submenus antigos da V1 que não possuem rotas implementadas
UPDATE menus SET ativo = FALSE
WHERE parent_id = (SELECT id FROM menus WHERE label = 'Compras' AND parent_id IS NULL LIMIT 1)
  AND rota IN ('/compras/solicitacoes', '/compras/processos', '/compras/contratos');

-- Novos submenus sob o "Compras" existente
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo)
VALUES
    ('Processos', 'gavel', '/processos', 1, (SELECT id FROM menus WHERE label = 'Compras' AND parent_id IS NULL LIMIT 1), TRUE),
    ('Empenhos', 'receipt_long', '/empenhos', 2, (SELECT id FROM menus WHERE label = 'Compras' AND parent_id IS NULL LIMIT 1), TRUE);

-- Associar novos submenus com role ADMIN
INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id
FROM menus m, roles r
WHERE m.rota IN ('/processos', '/empenhos')
  AND r.nome = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM menu_roles mr WHERE mr.menu_id = m.id AND mr.role_id = r.id
  );
