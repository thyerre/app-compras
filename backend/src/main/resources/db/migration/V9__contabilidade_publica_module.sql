-- =====================================================
-- V9 — Módulo de Contabilidade Pública
--
-- Submódulos:
--   1. Plano de Contas (PCASP)
--   2. Contabilidade Orçamentária (execução receita/despesa)
--   3. Contabilidade Financeira (contas bancárias, conciliação)
--   4. Contabilidade Patrimonial (lançamentos, bens, dívida)
--   5. Demonstrativos Legais (RREO, RGF, Balanços)
--
-- Base legal: Lei 4.320/64, LC 101/2000 (LRF), MCASP 9ª edição
-- =====================================================


-- ═══════════════════════════════════════════════════════
-- 1. PLANO DE CONTAS APLICADO AO SETOR PÚBLICO (PCASP)
-- ═══════════════════════════════════════════════════════

CREATE TABLE plano_contas (
    id              BIGSERIAL PRIMARY KEY,
    codigo          VARCHAR(30) NOT NULL,
    descricao       VARCHAR(500) NOT NULL,
    classe          SMALLINT NOT NULL CHECK (classe BETWEEN 1 AND 8),
    natureza_saldo  VARCHAR(1) NOT NULL CHECK (natureza_saldo IN ('D', 'C')),
    tipo            VARCHAR(1) NOT NULL CHECK (tipo IN ('S', 'A')),  -- S=sintética, A=analítica
    nivel           SMALLINT NOT NULL CHECK (nivel BETWEEN 1 AND 7),
    parent_id       BIGINT REFERENCES plano_contas(id),
    escrituravel    BOOLEAN NOT NULL DEFAULT FALSE,
    ativo           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(codigo)
);

CREATE INDEX idx_plano_contas_classe ON plano_contas(classe);
CREATE INDEX idx_plano_contas_parent ON plano_contas(parent_id);
CREATE INDEX idx_plano_contas_tipo   ON plano_contas(tipo);

COMMENT ON TABLE  plano_contas IS 'PCASP — Plano de Contas Aplicado ao Setor Público (8 classes, 7 níveis)';
COMMENT ON COLUMN plano_contas.classe IS '1=Ativo, 2=Passivo/PL, 3=VPD, 4=VPA, 5=CAPO, 6=CEPO, 7=Controles Devedores, 8=Controles Credores';
COMMENT ON COLUMN plano_contas.natureza_saldo IS 'D=Devedora, C=Credora';
COMMENT ON COLUMN plano_contas.tipo IS 'S=Sintética (agrupadora), A=Analítica (recebe lançamentos)';

-- ═══════════════════════════════════════════════════════
-- 2. CONTABILIDADE ORÇAMENTÁRIA
-- ═══════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────
-- 2.1 Execução da Receita Orçamentária
-- Estágios: Previsão → Lançamento → Arrecadação → Recolhimento
-- ─────────────────────────────────────────────────────

CREATE TABLE receita_orcamentaria (
    id                  BIGSERIAL PRIMARY KEY,
    exercicio           INTEGER NOT NULL,
    loa_id              BIGINT NOT NULL REFERENCES loas(id),
    receita_prevista_id BIGINT REFERENCES receitas_previstas(id),
    codigo_receita      VARCHAR(20) NOT NULL,
    descricao           VARCHAR(500) NOT NULL,
    categoria_economica VARCHAR(1) NOT NULL CHECK (categoria_economica IN ('1','2')),
    origem              VARCHAR(1),
    especie             VARCHAR(2),
    fonte_recurso_id    INTEGER REFERENCES fontes_recurso(id),
    valor_previsto_inicial   NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_previsto_atualizado NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_lancado       NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_arrecadado    NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_recolhido     NUMERIC(18,2) NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_receita_orc_exercicio ON receita_orcamentaria(exercicio);
CREATE INDEX idx_receita_orc_loa       ON receita_orcamentaria(loa_id);

COMMENT ON TABLE receita_orcamentaria IS 'Execução da receita orçamentária — acompanhamento dos estágios';

-- ─────────────────────────────────────────────────────
-- 2.2 Arrecadações (registros individuais)
-- ─────────────────────────────────────────────────────

CREATE TABLE arrecadacoes (
    id                      BIGSERIAL PRIMARY KEY,
    receita_orcamentaria_id BIGINT NOT NULL REFERENCES receita_orcamentaria(id),
    data_arrecadacao        DATE NOT NULL,
    valor                   NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    conta_bancaria_id       BIGINT,  -- FK adicionada após criar conta_bancaria
    documento_numero        VARCHAR(50),
    contribuinte            VARCHAR(255),
    historico               TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_arrecadacoes_data ON arrecadacoes(data_arrecadacao);

COMMENT ON TABLE arrecadacoes IS 'Registros individuais de arrecadação (estágio Arrecadação da receita)';

-- ─────────────────────────────────────────────────────
-- 2.3 Liquidação da Despesa (Art. 63, Lei 4.320/64)
-- ─────────────────────────────────────────────────────

CREATE TABLE liquidacoes (
    id                  BIGSERIAL PRIMARY KEY,
    empenho_id          BIGINT NOT NULL REFERENCES empenhos(id),
    numero_liquidacao   VARCHAR(30) NOT NULL,
    data_liquidacao     DATE NOT NULL,
    valor               NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    documento_tipo      VARCHAR(50),
    documento_numero    VARCHAR(50),
    documento_data      DATE,
    descricao           TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_liquidacoes_empenho ON liquidacoes(empenho_id);
CREATE INDEX idx_liquidacoes_data    ON liquidacoes(data_liquidacao);

COMMENT ON TABLE liquidacoes IS 'Liquidação da despesa — verificação do direito adquirido pelo credor (Art. 63, Lei 4.320/64)';

-- ─────────────────────────────────────────────────────
-- 2.4 Ordem de Pagamento / Pagamento (Art. 64-65, Lei 4.320/64)
-- ─────────────────────────────────────────────────────

CREATE TABLE pagamentos (
    id                  BIGSERIAL PRIMARY KEY,
    liquidacao_id       BIGINT NOT NULL REFERENCES liquidacoes(id),
    numero_pagamento    VARCHAR(30) NOT NULL,
    data_pagamento      DATE NOT NULL,
    valor               NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    conta_bancaria_id   BIGINT,  -- FK adicionada após criar conta_bancaria
    forma_pagamento     VARCHAR(30) NOT NULL DEFAULT 'TRANSFERENCIA',
    documento_bancario  VARCHAR(50),
    descricao           TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'EFETIVADO',
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pagamentos_liquidacao ON pagamentos(liquidacao_id);
CREATE INDEX idx_pagamentos_data       ON pagamentos(data_pagamento);

COMMENT ON TABLE pagamentos IS 'Pagamento da despesa — entrega do numerário ao credor (Art. 64-65, Lei 4.320/64)';

-- ─────────────────────────────────────────────────────
-- 2.5 Anulação de Empenho
-- ─────────────────────────────────────────────────────

CREATE TABLE anulacoes_empenho (
    id              BIGSERIAL PRIMARY KEY,
    empenho_id      BIGINT NOT NULL REFERENCES empenhos(id),
    data_anulacao   DATE NOT NULL,
    valor           NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    motivo          TEXT NOT NULL,
    tipo            VARCHAR(20) NOT NULL DEFAULT 'PARCIAL' CHECK (tipo IN ('PARCIAL', 'TOTAL')),
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_anulacoes_empenho ON anulacoes_empenho(empenho_id);

-- ─────────────────────────────────────────────────────
-- 2.6 Créditos Adicionais (Arts. 40-46, Lei 4.320/64)
-- ─────────────────────────────────────────────────────

CREATE TABLE creditos_adicionais (
    id                  BIGSERIAL PRIMARY KEY,
    exercicio           INTEGER NOT NULL,
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('SUPLEMENTAR', 'ESPECIAL', 'EXTRAORDINARIO')),
    numero_decreto      VARCHAR(50),
    data_decreto        DATE,
    numero_lei          VARCHAR(50),
    data_lei            DATE,
    dotacao_id          BIGINT NOT NULL REFERENCES dotacoes_orcamentarias(id),
    valor               NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    fonte_anulacao      TEXT,
    justificativa       TEXT NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'VIGENTE',
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_creditos_adicionais_exercicio ON creditos_adicionais(exercicio);

COMMENT ON TABLE creditos_adicionais IS 'Créditos adicionais: suplementares, especiais e extraordinários (Arts. 40-46, Lei 4.320/64)';

-- ─────────────────────────────────────────────────────
-- 2.7 Restos a Pagar (Art. 36, Lei 4.320/64)
-- ─────────────────────────────────────────────────────

CREATE TABLE restos_a_pagar (
    id                  BIGSERIAL PRIMARY KEY,
    exercicio_origem    INTEGER NOT NULL,
    empenho_id          BIGINT NOT NULL REFERENCES empenhos(id),
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('PROCESSADO', 'NAO_PROCESSADO')),
    valor_inscrito      NUMERIC(18,2) NOT NULL,
    valor_cancelado     NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_liquidado     NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_pago          NUMERIC(18,2) NOT NULL DEFAULT 0,
    data_inscricao      DATE NOT NULL,
    data_cancelamento   DATE,
    data_pagamento      DATE,
    status              VARCHAR(20) NOT NULL DEFAULT 'INSCRITO',
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_rp_exercicio ON restos_a_pagar(exercicio_origem);
CREATE INDEX idx_rp_empenho   ON restos_a_pagar(empenho_id);
CREATE INDEX idx_rp_tipo      ON restos_a_pagar(tipo);

COMMENT ON TABLE restos_a_pagar IS 'Restos a Pagar — despesas empenhadas não pagas até 31/12 (Art. 36, Lei 4.320/64). Processados = já liquidados; Não Processados = apenas empenhados.';


-- ═══════════════════════════════════════════════════════
-- 3. CONTABILIDADE FINANCEIRA
-- ═══════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────
-- 3.1 Contas Bancárias
-- ─────────────────────────────────────────────────────

CREATE TABLE contas_bancarias (
    id              BIGSERIAL PRIMARY KEY,
    banco_codigo    VARCHAR(10) NOT NULL,
    banco_nome      VARCHAR(100) NOT NULL,
    agencia         VARCHAR(20) NOT NULL,
    numero_conta    VARCHAR(30) NOT NULL,
    digito          VARCHAR(5),
    descricao       VARCHAR(255) NOT NULL,
    tipo            VARCHAR(30) NOT NULL DEFAULT 'MOVIMENTO' CHECK (tipo IN ('MOVIMENTO', 'VINCULADA', 'ARRECADACAO', 'PAGAMENTO', 'APLICACAO')),
    fonte_recurso_id INTEGER REFERENCES fontes_recurso(id),
    saldo_atual     NUMERIC(18,2) NOT NULL DEFAULT 0,
    ativo           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(banco_codigo, agencia, numero_conta)
);

COMMENT ON TABLE contas_bancarias IS 'Contas bancárias do município — princípio de unidade de tesouraria (Art. 56, Lei 4.320/64)';

-- Adicionar FK de conta_bancaria nas tabelas que ficaram pendentes
ALTER TABLE arrecadacoes    ADD CONSTRAINT fk_arrecadacoes_conta    FOREIGN KEY (conta_bancaria_id) REFERENCES contas_bancarias(id);
ALTER TABLE pagamentos      ADD CONSTRAINT fk_pagamentos_conta      FOREIGN KEY (conta_bancaria_id) REFERENCES contas_bancarias(id);

-- ─────────────────────────────────────────────────────
-- 3.2 Movimentações Financeiras Extra-Orçamentárias
-- ─────────────────────────────────────────────────────

CREATE TABLE movimentacoes_financeiras (
    id                  BIGSERIAL PRIMARY KEY,
    exercicio           INTEGER NOT NULL,
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('INGRESSO', 'DISPENDIO')),
    categoria           VARCHAR(30) NOT NULL DEFAULT 'EXTRA_ORCAMENTARIO',
    conta_bancaria_id   BIGINT NOT NULL REFERENCES contas_bancarias(id),
    data_movimento      DATE NOT NULL,
    valor               NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    historico           TEXT NOT NULL,
    documento_numero    VARCHAR(50),
    plano_conta_id      BIGINT REFERENCES plano_contas(id),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mov_fin_exercicio ON movimentacoes_financeiras(exercicio);
CREATE INDEX idx_mov_fin_data      ON movimentacoes_financeiras(data_movimento);
CREATE INDEX idx_mov_fin_conta     ON movimentacoes_financeiras(conta_bancaria_id);

COMMENT ON TABLE movimentacoes_financeiras IS 'Recebimentos e pagamentos extra-orçamentários — compõem o Balanço Financeiro';

-- ─────────────────────────────────────────────────────
-- 3.3 Conciliação Bancária
-- ─────────────────────────────────────────────────────

CREATE TABLE conciliacoes_bancarias (
    id                  BIGSERIAL PRIMARY KEY,
    conta_bancaria_id   BIGINT NOT NULL REFERENCES contas_bancarias(id),
    mes_referencia      INTEGER NOT NULL CHECK (mes_referencia BETWEEN 1 AND 12),
    ano_referencia      INTEGER NOT NULL,
    saldo_extrato       NUMERIC(18,2) NOT NULL,
    saldo_contabil      NUMERIC(18,2) NOT NULL,
    diferenca           NUMERIC(18,2) NOT NULL DEFAULT 0,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'CONCILIADA', 'DIVERGENTE')),
    observacoes         TEXT,
    responsavel         VARCHAR(200),
    data_conciliacao    DATE,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(conta_bancaria_id, mes_referencia, ano_referencia)
);

COMMENT ON TABLE conciliacoes_bancarias IS 'Conciliação bancária mensal — confronto saldo extrato × saldo contábil';

CREATE TABLE conciliacao_itens (
    id                      BIGSERIAL PRIMARY KEY,
    conciliacao_id          BIGINT NOT NULL REFERENCES conciliacoes_bancarias(id) ON DELETE CASCADE,
    tipo                    VARCHAR(30) NOT NULL CHECK (tipo IN ('PENDENTE_BANCO', 'PENDENTE_CONTABIL', 'AJUSTE')),
    data_item               DATE NOT NULL,
    descricao               VARCHAR(500) NOT NULL,
    valor                   NUMERIC(18,2) NOT NULL,
    documento_numero        VARCHAR(50),
    resolvido               BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_conc_itens_conciliacao ON conciliacao_itens(conciliacao_id);

-- ─────────────────────────────────────────────────────
-- 3.4 Programação Financeira (Art. 8°, LRF)
-- ─────────────────────────────────────────────────────

CREATE TABLE programacao_financeira (
    id              BIGSERIAL PRIMARY KEY,
    exercicio       INTEGER NOT NULL,
    mes             INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    orgao_id        INTEGER NOT NULL REFERENCES orgaos(id),
    unidade_id      INTEGER REFERENCES unidades(id),
    valor_previsto  NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_liberado  NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_executado NUMERIC(18,2) NOT NULL DEFAULT 0,
    status          VARCHAR(20) NOT NULL DEFAULT 'PREVISTO',
    observacoes     TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(exercicio, mes, orgao_id, unidade_id)
);

COMMENT ON TABLE programacao_financeira IS 'Cronograma mensal de desembolso — programação financeira (Art. 8°, LRF)';


-- ═══════════════════════════════════════════════════════
-- 4. CONTABILIDADE PATRIMONIAL
-- ═══════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────
-- 4.1 Lançamentos Contábeis (partidas dobradas — Art. 86, Lei 4.320/64)
-- ─────────────────────────────────────────────────────

CREATE TABLE lancamentos_contabeis (
    id              BIGSERIAL PRIMARY KEY,
    exercicio       INTEGER NOT NULL,
    numero          VARCHAR(30) NOT NULL,
    data_lancamento DATE NOT NULL,
    tipo            VARCHAR(30) NOT NULL DEFAULT 'NORMAL' CHECK (tipo IN ('NORMAL', 'ENCERRAMENTO', 'ABERTURA', 'ESTORNO')),
    origem          VARCHAR(30) NOT NULL DEFAULT 'MANUAL' CHECK (origem IN ('MANUAL', 'EMPENHO', 'LIQUIDACAO', 'PAGAMENTO', 'ARRECADACAO', 'AUTOMATICO')),
    historico       TEXT NOT NULL,
    valor_total     NUMERIC(18,2) NOT NULL CHECK (valor_total > 0),
    documento_ref   VARCHAR(100),
    empenho_id      BIGINT REFERENCES empenhos(id),
    liquidacao_id   BIGINT REFERENCES liquidacoes(id),
    pagamento_id    BIGINT REFERENCES pagamentos(id),
    arrecadacao_id  BIGINT REFERENCES arrecadacoes(id),
    status          VARCHAR(20) NOT NULL DEFAULT 'EFETIVADO',
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_lanc_contabeis_exercicio ON lancamentos_contabeis(exercicio);
CREATE INDEX idx_lanc_contabeis_data      ON lancamentos_contabeis(data_lancamento);
CREATE INDEX idx_lanc_contabeis_origem    ON lancamentos_contabeis(origem);

COMMENT ON TABLE lancamentos_contabeis IS 'Lançamentos contábeis em partidas dobradas (Art. 86, Lei 4.320/64)';

CREATE TABLE lancamento_itens (
    id                  BIGSERIAL PRIMARY KEY,
    lancamento_id       BIGINT NOT NULL REFERENCES lancamentos_contabeis(id) ON DELETE CASCADE,
    plano_conta_id      BIGINT NOT NULL REFERENCES plano_contas(id),
    tipo                VARCHAR(1) NOT NULL CHECK (tipo IN ('D', 'C')),
    valor               NUMERIC(18,2) NOT NULL CHECK (valor > 0),
    historico_item      TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_lanc_itens_lancamento ON lancamento_itens(lancamento_id);
CREATE INDEX idx_lanc_itens_conta      ON lancamento_itens(plano_conta_id);

COMMENT ON TABLE lancamento_itens IS 'Itens do lançamento contábil — débitos e créditos';

-- ─────────────────────────────────────────────────────
-- 4.2 Balancete de Verificação
-- ─────────────────────────────────────────────────────

CREATE TABLE balancetes (
    id              BIGSERIAL PRIMARY KEY,
    exercicio       INTEGER NOT NULL,
    mes             INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    plano_conta_id  BIGINT NOT NULL REFERENCES plano_contas(id),
    saldo_anterior  NUMERIC(18,2) NOT NULL DEFAULT 0,
    total_debitos   NUMERIC(18,2) NOT NULL DEFAULT 0,
    total_creditos  NUMERIC(18,2) NOT NULL DEFAULT 0,
    saldo_atual     NUMERIC(18,2) NOT NULL DEFAULT 0,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(exercicio, mes, plano_conta_id)
);

CREATE INDEX idx_balancetes_periodo ON balancetes(exercicio, mes);

COMMENT ON TABLE balancetes IS 'Balancete de verificação mensal — saldos por conta do PCASP';

-- ─────────────────────────────────────────────────────
-- 4.3 Bens Patrimoniais
-- ─────────────────────────────────────────────────────

CREATE TABLE bens_patrimoniais (
    id                  BIGSERIAL PRIMARY KEY,
    numero_patrimonio   VARCHAR(30) NOT NULL UNIQUE,
    descricao           VARCHAR(500) NOT NULL,
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('MOVEL', 'IMOVEL', 'INTANGIVEL')),
    categoria           VARCHAR(50),
    orgao_id            INTEGER NOT NULL REFERENCES orgaos(id),
    unidade_id          INTEGER REFERENCES unidades(id),
    data_aquisicao      DATE NOT NULL,
    valor_aquisicao     NUMERIC(18,2) NOT NULL,
    valor_atual         NUMERIC(18,2) NOT NULL,
    vida_util_meses     INTEGER,
    valor_residual      NUMERIC(18,2) DEFAULT 0,
    depreciacao_acumulada NUMERIC(18,2) NOT NULL DEFAULT 0,
    localizacao         VARCHAR(500),
    responsavel         VARCHAR(200),
    estado_conservacao  VARCHAR(30) DEFAULT 'BOM' CHECK (estado_conservacao IN ('OTIMO', 'BOM', 'REGULAR', 'RUIM', 'INSERVIVEL')),
    situacao            VARCHAR(20) NOT NULL DEFAULT 'ATIVO' CHECK (situacao IN ('ATIVO', 'BAIXADO', 'CEDIDO', 'EM_MANUTENCAO')),
    nota_fiscal         VARCHAR(50),
    fornecedor_id       BIGINT REFERENCES fornecedores(id),
    empenho_id          BIGINT REFERENCES empenhos(id),
    plano_conta_id      BIGINT REFERENCES plano_contas(id),
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_bens_orgao     ON bens_patrimoniais(orgao_id);
CREATE INDEX idx_bens_tipo      ON bens_patrimoniais(tipo);
CREATE INDEX idx_bens_situacao  ON bens_patrimoniais(situacao);

COMMENT ON TABLE bens_patrimoniais IS 'Inventário de bens patrimoniais — móveis, imóveis e intangíveis';

-- ─────────────────────────────────────────────────────
-- 4.4 Movimentação de Bens
-- ─────────────────────────────────────────────────────

CREATE TABLE movimentacoes_bens (
    id                  BIGSERIAL PRIMARY KEY,
    bem_id              BIGINT NOT NULL REFERENCES bens_patrimoniais(id),
    tipo                VARCHAR(30) NOT NULL CHECK (tipo IN ('INCORPORACAO', 'BAIXA', 'TRANSFERENCIA', 'DEPRECIACAO', 'REAVALIACAO', 'CESSAO')),
    data_movimentacao   DATE NOT NULL,
    valor               NUMERIC(18,2) NOT NULL,
    orgao_destino_id    INTEGER REFERENCES orgaos(id),
    unidade_destino_id  INTEGER REFERENCES unidades(id),
    motivo              TEXT NOT NULL,
    documento_numero    VARCHAR(50),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mov_bens_bem  ON movimentacoes_bens(bem_id);
CREATE INDEX idx_mov_bens_tipo ON movimentacoes_bens(tipo);

-- ─────────────────────────────────────────────────────
-- 4.5 Dívida Pública
-- ─────────────────────────────────────────────────────

CREATE TABLE divida_publica (
    id                  BIGSERIAL PRIMARY KEY,
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('FUNDADA', 'FLUTUANTE')),
    credor              VARCHAR(300) NOT NULL,
    numero_contrato     VARCHAR(50),
    data_contratacao    DATE NOT NULL,
    data_vencimento     DATE,
    valor_original      NUMERIC(18,2) NOT NULL,
    saldo_devedor       NUMERIC(18,2) NOT NULL,
    taxa_juros          NUMERIC(8,4),
    indice_correcao     VARCHAR(50),
    finalidade          TEXT,
    lei_autorizativa    VARCHAR(100),
    status              VARCHAR(20) NOT NULL DEFAULT 'VIGENTE' CHECK (status IN ('VIGENTE', 'QUITADA', 'RENEGOCIADA')),
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE divida_publica IS 'Dívida pública fundada e flutuante do município';

CREATE TABLE divida_publica_parcelas (
    id                  BIGSERIAL PRIMARY KEY,
    divida_id           BIGINT NOT NULL REFERENCES divida_publica(id),
    numero_parcela      INTEGER NOT NULL,
    data_vencimento     DATE NOT NULL,
    valor_principal     NUMERIC(18,2) NOT NULL,
    valor_juros         NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_encargos      NUMERIC(18,2) NOT NULL DEFAULT 0,
    valor_pago          NUMERIC(18,2) NOT NULL DEFAULT 0,
    data_pagamento      DATE,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'PAGA', 'ATRASADA')),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_divida_parcelas_divida ON divida_publica_parcelas(divida_id);


-- ═══════════════════════════════════════════════════════
-- 5. DEMONSTRATIVOS LEGAIS
-- ═══════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────
-- 5.1 Demonstrações Contábeis (Art. 101, Lei 4.320/64)
-- ─────────────────────────────────────────────────────

CREATE TABLE demonstracoes_contabeis (
    id                  BIGSERIAL PRIMARY KEY,
    exercicio           INTEGER NOT NULL,
    tipo                VARCHAR(5) NOT NULL CHECK (tipo IN ('BO', 'BF', 'BP', 'DVP', 'DFC', 'DMPL')),
    periodo_inicio      DATE NOT NULL,
    periodo_fim         DATE NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO' CHECK (status IN ('RASCUNHO', 'GERADA', 'PUBLICADA', 'RETIFICADA')),
    data_geracao        TIMESTAMP,
    data_publicacao     DATE,
    responsavel         VARCHAR(200),
    observacoes         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_demonstracoes_exercicio ON demonstracoes_contabeis(exercicio);

COMMENT ON TABLE demonstracoes_contabeis IS 'BO=Balanço Orçamentário, BF=Balanço Financeiro, BP=Balanço Patrimonial, DVP=Demonstração Variações Patrimoniais, DFC=Fluxo de Caixa, DMPL=Mutações PL';

CREATE TABLE demonstracao_linhas (
    id                  BIGSERIAL PRIMARY KEY,
    demonstracao_id     BIGINT NOT NULL REFERENCES demonstracoes_contabeis(id) ON DELETE CASCADE,
    ordem               INTEGER NOT NULL,
    nivel               SMALLINT NOT NULL DEFAULT 0,
    descricao           VARCHAR(500) NOT NULL,
    coluna1_valor       NUMERIC(18,2) DEFAULT 0,
    coluna2_valor       NUMERIC(18,2) DEFAULT 0,
    coluna3_valor       NUMERIC(18,2) DEFAULT 0,
    coluna4_valor       NUMERIC(18,2) DEFAULT 0,
    eh_totalizador      BOOLEAN NOT NULL DEFAULT FALSE,
    plano_conta_id      BIGINT REFERENCES plano_contas(id),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_demo_linhas_demo ON demonstracao_linhas(demonstracao_id);

-- ─────────────────────────────────────────────────────
-- 5.2 RREO — Relatório Resumido da Execução Orçamentária
-- (Art. 52-53, LC 101/2000 — bimestral)
-- ─────────────────────────────────────────────────────

CREATE TABLE relatorios_rreo (
    id                      BIGSERIAL PRIMARY KEY,
    exercicio               INTEGER NOT NULL,
    bimestre                INTEGER NOT NULL CHECK (bimestre BETWEEN 1 AND 6),
    periodo_inicio          DATE NOT NULL,
    periodo_fim             DATE NOT NULL,
    receita_prevista        NUMERIC(18,2) NOT NULL DEFAULT 0,
    receita_realizada       NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_fixada          NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_empenhada       NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_liquidada       NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_paga            NUMERIC(18,2) NOT NULL DEFAULT 0,
    resultado_primario      NUMERIC(18,2) NOT NULL DEFAULT 0,
    resultado_nominal       NUMERIC(18,2) NOT NULL DEFAULT 0,
    receita_corrente_liquida NUMERIC(18,2) NOT NULL DEFAULT 0,
    restos_pagar_processados NUMERIC(18,2) NOT NULL DEFAULT 0,
    restos_pagar_nao_proc   NUMERIC(18,2) NOT NULL DEFAULT 0,
    status                  VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO' CHECK (status IN ('RASCUNHO', 'GERADO', 'PUBLICADO', 'RETIFICADO')),
    data_geracao            TIMESTAMP,
    data_publicacao         DATE,
    responsavel_nome        VARCHAR(200),
    responsavel_cargo       VARCHAR(100),
    contador_nome           VARCHAR(200),
    contador_crc            VARCHAR(30),
    observacoes             TEXT,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(exercicio, bimestre)
);

COMMENT ON TABLE relatorios_rreo IS 'RREO — Relatório Resumido da Execução Orçamentária (bimestral, Art. 52-53, LRF)';

-- ─────────────────────────────────────────────────────
-- 5.3 RGF — Relatório de Gestão Fiscal
-- (Art. 54-55, LC 101/2000 — quadrimestral)
-- ─────────────────────────────────────────────────────

CREATE TABLE relatorios_rgf (
    id                              BIGSERIAL PRIMARY KEY,
    exercicio                       INTEGER NOT NULL,
    quadrimestre                    INTEGER NOT NULL CHECK (quadrimestre BETWEEN 1 AND 3),
    periodo_inicio                  DATE NOT NULL,
    periodo_fim                     DATE NOT NULL,
    receita_corrente_liquida        NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_pessoal_executivo       NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_pessoal_legislativo     NUMERIC(18,2) NOT NULL DEFAULT 0,
    despesa_pessoal_total           NUMERIC(18,2) NOT NULL DEFAULT 0,
    percentual_pessoal_executivo    NUMERIC(8,4) NOT NULL DEFAULT 0,
    percentual_pessoal_legislativo  NUMERIC(8,4) NOT NULL DEFAULT 0,
    percentual_pessoal_total        NUMERIC(8,4) NOT NULL DEFAULT 0,
    limite_maximo                   NUMERIC(8,4) NOT NULL DEFAULT 60.00,
    limite_prudencial               NUMERIC(8,4) NOT NULL DEFAULT 57.00,
    limite_alerta                   NUMERIC(8,4) NOT NULL DEFAULT 54.00,
    divida_consolidada              NUMERIC(18,2) NOT NULL DEFAULT 0,
    limite_divida                   NUMERIC(18,2) NOT NULL DEFAULT 0,
    percentual_divida               NUMERIC(8,4) NOT NULL DEFAULT 0,
    disponibilidade_caixa           NUMERIC(18,2) NOT NULL DEFAULT 0,
    obrigacoes_financeiras          NUMERIC(18,2) NOT NULL DEFAULT 0,
    status                          VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO' CHECK (status IN ('RASCUNHO', 'GERADO', 'PUBLICADO', 'RETIFICADO')),
    data_geracao                    TIMESTAMP,
    data_publicacao                 DATE,
    responsavel_nome                VARCHAR(200),
    responsavel_cargo               VARCHAR(100),
    contador_nome                   VARCHAR(200),
    contador_crc                    VARCHAR(30),
    observacoes                     TEXT,
    created_at                      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(exercicio, quadrimestre)
);

COMMENT ON TABLE relatorios_rgf IS 'RGF — Relatório de Gestão Fiscal (quadrimestral, Art. 54-55, LRF). Limites de pessoal município: 60% RCL (54% Executivo + 6% Legislativo).';


-- ═══════════════════════════════════════════════════════
-- 6. MENUS — Contabilidade
-- ═══════════════════════════════════════════════════════

-- Menu pai "Contabilidade"
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo) VALUES
('Contabilidade', 'account_balance_wallet', NULL, 4, NULL, TRUE);

-- Submenus de Contabilidade
INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo) VALUES
('Plano de Contas',         'account_tree',    '/contabilidade/plano-contas',       1, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Lançamentos Contábeis',   'edit_note',       '/contabilidade/lancamentos',        2, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Liquidações',             'fact_check',      '/contabilidade/liquidacoes',        3, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Pagamentos',              'payments',        '/contabilidade/pagamentos',         4, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Contas Bancárias',        'account_balance', '/contabilidade/contas-bancarias',   5, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Receita Orçamentária',    'trending_up',     '/contabilidade/receita-orcamentaria', 6, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Créditos Adicionais',     'add_card',        '/contabilidade/creditos-adicionais', 7, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Restos a Pagar',          'schedule',        '/contabilidade/restos-a-pagar',     8, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Bens Patrimoniais',       'real_estate_agent', '/contabilidade/bens-patrimoniais', 9, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Dívida Pública',          'credit_card',     '/contabilidade/divida-publica',    10, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Conciliação Bancária',    'compare_arrows',  '/contabilidade/conciliacao-bancaria', 11, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Balancetes',              'summarize',       '/contabilidade/balancetes',        12, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('Demonstrações Contábeis', 'description',     '/contabilidade/demonstracoes',     13, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('RREO',                    'assessment',      '/contabilidade/rreo',             14, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE),
('RGF',                     'analytics',       '/contabilidade/rgf',              15, (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1), TRUE);

-- Vincular todos os menus de contabilidade à role ADMIN
INSERT INTO menu_roles (menu_id, role_id)
SELECT id, 1 FROM menus
WHERE parent_id = (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1)
   OR (label = 'Contabilidade' AND parent_id IS NULL);

-- Vincular à role USUARIO (menus de consulta)
INSERT INTO menu_roles (menu_id, role_id)
SELECT id, 2 FROM menus
WHERE parent_id = (SELECT id FROM menus WHERE label = 'Contabilidade' AND parent_id IS NULL LIMIT 1)
   OR (label = 'Contabilidade' AND parent_id IS NULL);


-- ═══════════════════════════════════════════════════════
-- 7. SEED — Plano de Contas (Nível 1 e 2 — PCASP)
-- ═══════════════════════════════════════════════════════

-- Classes (nível 1)
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('1',   'Ativo',                                          1, 'D', 'S', 1, NULL, FALSE),
('2',   'Passivo e Patrimônio Líquido',                   2, 'C', 'S', 1, NULL, FALSE),
('3',   'Variações Patrimoniais Diminutivas',             3, 'D', 'S', 1, NULL, FALSE),
('4',   'Variações Patrimoniais Aumentativas',            4, 'C', 'S', 1, NULL, FALSE),
('5',   'Controles da Aprovação do Planejamento e Orçamento', 5, 'D', 'S', 1, NULL, FALSE),
('6',   'Controles da Execução do Planejamento e Orçamento', 6, 'C', 'S', 1, NULL, FALSE),
('7',   'Controles Devedores',                            7, 'D', 'S', 1, NULL, FALSE),
('8',   'Controles Credores',                             8, 'C', 'S', 1, NULL, FALSE);

-- Grupos (nível 2) — Classe 1: Ativo
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('1.1', 'Ativo Circulante',                    1, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '1'), FALSE),
('1.2', 'Ativo Não Circulante',                1, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '1'), FALSE);

-- Grupos (nível 2) — Classe 2: Passivo
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('2.1', 'Passivo Circulante',                  2, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '2'), FALSE),
('2.2', 'Passivo Não Circulante',              2, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '2'), FALSE),
('2.3', 'Patrimônio Líquido',                  2, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '2'), FALSE);

-- Grupos (nível 2) — Classe 3: VPD
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('3.1', 'Pessoal e Encargos',                  3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE),
('3.2', 'Benefícios Previdenciários e Assistenciais', 3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE),
('3.3', 'Uso de Bens, Serviços e Consumo de Capital Fixo', 3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE),
('3.4', 'Variações Patrimoniais Diminutivas Financeiras', 3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE),
('3.5', 'Transferências e Delegações Concedidas', 3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE),
('3.6', 'Desvalorização e Perda de Ativos e Incorporação de Passivos', 3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE),
('3.9', 'Outras Variações Patrimoniais Diminutivas', 3, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '3'), FALSE);

-- Grupos (nível 2) — Classe 4: VPA
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('4.1', 'Impostos, Taxas e Contribuições de Melhoria', 4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE),
('4.2', 'Contribuições',                       4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE),
('4.3', 'Exploração e Venda de Bens, Serviços e Direitos', 4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE),
('4.4', 'Variações Patrimoniais Aumentativas Financeiras', 4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE),
('4.5', 'Transferências e Delegações Recebidas', 4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE),
('4.6', 'Valorização e Ganhos com Ativos e Desincorporação de Passivos', 4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE),
('4.9', 'Outras Variações Patrimoniais Aumentativas', 4, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '4'), FALSE);

-- Grupos (nível 2) — Classe 5: CAPO
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('5.1', 'Planejamento Aprovado',               5, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '5'), FALSE),
('5.2', 'Orçamento Aprovado',                  5, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '5'), FALSE),
('5.3', 'Inscrição de Restos a Pagar',         5, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '5'), FALSE);

-- Grupos (nível 2) — Classe 6: CEPO
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('6.1', 'Execução do Planejamento',            6, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '6'), FALSE),
('6.2', 'Execução do Orçamento',               6, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '6'), FALSE),
('6.3', 'Execução de Restos a Pagar',          6, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '6'), FALSE);

-- Grupos (nível 2) — Classe 7: Controles Devedores
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('7.1', 'Atos Potenciais',                     7, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '7'), FALSE),
('7.2', 'Administração Financeira',            7, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '7'), FALSE),
('7.3', 'Dívida Ativa',                        7, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '7'), FALSE),
('7.8', 'Custos',                              7, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '7'), FALSE),
('7.9', 'Outros Controles',                    7, 'D', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '7'), FALSE);

-- Grupos (nível 2) — Classe 8: Controles Credores
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('8.1', 'Execução dos Atos Potenciais',        8, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '8'), FALSE),
('8.2', 'Execução da Administração Financeira', 8, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '8'), FALSE),
('8.3', 'Execução da Dívida Ativa',            8, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '8'), FALSE),
('8.8', 'Apuração de Custos',                  8, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '8'), FALSE),
('8.9', 'Outros Controles',                    8, 'C', 'S', 2, (SELECT id FROM plano_contas WHERE codigo = '8'), FALSE);

-- ═══════════════════════════════════════════════════════
-- 8. SEED — Contas analíticas essenciais (nível 3+)
-- ═══════════════════════════════════════════════════════

-- Ativo Circulante — subcontas essenciais
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('1.1.1', 'Caixa e Equivalentes de Caixa',  1, 'D', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '1.1'), FALSE),
('1.1.2', 'Créditos a Curto Prazo',         1, 'D', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '1.1'), FALSE),
('1.1.3', 'Demais Créditos e Valores a CP', 1, 'D', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '1.1'), FALSE),
('1.1.1.1', 'Caixa',                        1, 'D', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '1.1.1'), TRUE),
('1.1.1.2', 'Bancos Conta Movimento',       1, 'D', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '1.1.1'), TRUE);

-- Passivo Circulante — subcontas essenciais
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('2.1.1', 'Obrigações Trabalhistas, Previdenciárias e Assistenciais', 2, 'C', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '2.1'), FALSE),
('2.1.2', 'Empréstimos e Financiamentos a CP',  2, 'C', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '2.1'), FALSE),
('2.1.3', 'Fornecedores e Contas a Pagar',      2, 'C', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '2.1'), FALSE),
('2.1.3.1', 'Fornecedores Nacionais',           2, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '2.1.3'), TRUE);

-- Execução Orçamentária — subcontas de controle
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('6.2.1', 'Receita Orçamentária',           6, 'C', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '6.2'), FALSE),
('6.2.1.1', 'Receita a Realizar',           6, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '6.2.1'), TRUE),
('6.2.1.2', 'Receita Realizada',            6, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '6.2.1'), TRUE),
('6.2.2', 'Despesa Orçamentária',           6, 'C', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '6.2'), FALSE),
('6.2.2.1', 'Crédito Disponível',           6, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '6.2.2'), TRUE),
('6.2.2.2', 'Crédito Empenhado a Liquidar', 6, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '6.2.2'), TRUE),
('6.2.2.3', 'Crédito Empenhado Liquidado a Pagar', 6, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '6.2.2'), TRUE),
('6.2.2.4', 'Crédito Empenhado Pago',       6, 'C', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '6.2.2'), TRUE);

-- Previsão Orçamentária — CAPO
INSERT INTO plano_contas (codigo, descricao, classe, natureza_saldo, tipo, nivel, parent_id, escrituravel) VALUES
('5.2.1', 'Previsão da Receita',            5, 'D', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '5.2'), FALSE),
('5.2.1.1', 'Previsão Inicial da Receita',  5, 'D', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '5.2.1'), TRUE),
('5.2.2', 'Fixação da Despesa',             5, 'D', 'S', 3, (SELECT id FROM plano_contas WHERE codigo = '5.2'), FALSE),
('5.2.2.1', 'Dotação Inicial',              5, 'D', 'A', 4, (SELECT id FROM plano_contas WHERE codigo = '5.2.2'), TRUE);

-- ═══════════════════════════════════════════════════════
-- 9. SEED — Contas bancárias exemplo
-- ═══════════════════════════════════════════════════════

INSERT INTO contas_bancarias (banco_codigo, banco_nome, agencia, numero_conta, descricao, tipo) VALUES
('001', 'Banco do Brasil',   '1234-5', '12345-6',  'Conta Movimento – Prefeitura',     'MOVIMENTO'),
('001', 'Banco do Brasil',   '1234-5', '12346-7',  'Conta Vinculada – FUNDEB',         'VINCULADA'),
('001', 'Banco do Brasil',   '1234-5', '12347-8',  'Conta Vinculada – SUS',            'VINCULADA'),
('104', 'Caixa Econômica',   '0001',   '54321-0',  'Conta Vinculada – FNAS/SUAS',      'VINCULADA'),
('001', 'Banco do Brasil',   '1234-5', '12348-9',  'Conta Arrecadação – Tributos',     'ARRECADACAO');
