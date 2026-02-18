-- =====================================================
-- V7 — Ajustes de schema para alinhamento entity/service
-- =====================================================

-- Subfunções: adicionar referência a função (usada em filtros e domínios)
ALTER TABLE subfuncoes ADD COLUMN funcao_id INTEGER REFERENCES funcoes(id);

-- Programas: adicionar campos de exercício para vinculação temporal
ALTER TABLE programas ADD COLUMN exercicio_inicio INTEGER;
ALTER TABLE programas ADD COLUMN exercicio_fim INTEGER;

-- Dotações orçamentárias: adicionar campo de descrição
ALTER TABLE dotacoes_orcamentarias ADD COLUMN descricao TEXT;

-- Processo documentos: adicionar campo tamanho_bytes e renomear observacoes para observacao
ALTER TABLE processo_documentos ADD COLUMN tamanho_bytes BIGINT;

-- LDO Prioridades: adicionar referências e campos faltantes
ALTER TABLE ldo_prioridades ADD COLUMN programa_id INTEGER REFERENCES programas(id);
ALTER TABLE ldo_prioridades ADD COLUMN meta VARCHAR(500);
ALTER TABLE ldo_prioridades ADD COLUMN prioridade INTEGER;

-- Preencher programa_id a partir da ação vinculada (ação já tem programa_id)
UPDATE ldo_prioridades lp
SET programa_id = a.programa_id
FROM acoes a
WHERE lp.acao_id = a.id
  AND lp.programa_id IS NULL;

-- Processo Participantes: adicionar campo motivo_inabilitacao
ALTER TABLE processo_participantes ADD COLUMN motivo_inabilitacao TEXT;

-- Empenhos: campo valor mapeia para valor_empenhado
-- (o entity Empenho tem campo 'valor' mas a tabela tem 'valor_empenhado')
-- Vamos adicionar coluna 'valor' como alias
ALTER TABLE empenhos RENAME COLUMN valor_empenhado TO valor;
