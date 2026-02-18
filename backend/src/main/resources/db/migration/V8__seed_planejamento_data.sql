-- =====================================================
-- V8 — Dados iniciais de Planejamento e Orçamento
-- Dados realistas baseados em padrões de prefeituras brasileiras
-- =====================================================

-- ─────────────────────────────────────────────────────
-- Órgãos (estrutura típica de prefeitura)
-- ─────────────────────────────────────────────────────

INSERT INTO orgaos (codigo, descricao, ativo) VALUES
('01', 'Gabinete do Prefeito', TRUE),
('02', 'Secretaria de Administração e Finanças', TRUE),
('03', 'Secretaria de Educação', TRUE),
('04', 'Secretaria de Saúde', TRUE),
('05', 'Secretaria de Obras e Infraestrutura', TRUE),
('06', 'Secretaria de Assistência Social', TRUE),
('07', 'Secretaria de Agricultura e Meio Ambiente', TRUE),
('08', 'Secretaria de Cultura, Esporte e Lazer', TRUE),
('09', 'Câmara Municipal', TRUE),
('10', 'Fundo Municipal de Saúde', TRUE),
('11', 'Fundo Municipal de Assistência Social', TRUE);

-- ─────────────────────────────────────────────────────
-- Unidades Orçamentárias
-- ─────────────────────────────────────────────────────

INSERT INTO unidades (codigo, descricao, orgao_id, ativo) VALUES
-- Gabinete do Prefeito
('01.01', 'Chefia de Gabinete', (SELECT id FROM orgaos WHERE codigo = '01'), TRUE),
-- Secretaria de Administração e Finanças
('02.01', 'Departamento de Administração', (SELECT id FROM orgaos WHERE codigo = '02'), TRUE),
('02.02', 'Departamento de Finanças e Contabilidade', (SELECT id FROM orgaos WHERE codigo = '02'), TRUE),
('02.03', 'Departamento de Compras e Licitações', (SELECT id FROM orgaos WHERE codigo = '02'), TRUE),
-- Secretaria de Educação
('03.01', 'Departamento de Ensino Fundamental', (SELECT id FROM orgaos WHERE codigo = '03'), TRUE),
('03.02', 'Departamento de Educação Infantil', (SELECT id FROM orgaos WHERE codigo = '03'), TRUE),
('03.03', 'Departamento de Transporte Escolar', (SELECT id FROM orgaos WHERE codigo = '03'), TRUE),
-- Secretaria de Saúde
('04.01', 'Departamento de Atenção Básica', (SELECT id FROM orgaos WHERE codigo = '04'), TRUE),
('04.02', 'Departamento de Vigilância em Saúde', (SELECT id FROM orgaos WHERE codigo = '04'), TRUE),
-- Secretaria de Obras
('05.01', 'Departamento de Obras', (SELECT id FROM orgaos WHERE codigo = '05'), TRUE),
('05.02', 'Departamento de Serviços Urbanos', (SELECT id FROM orgaos WHERE codigo = '05'), TRUE),
-- Secretaria de Assistência Social
('06.01', 'Departamento de Proteção Social Básica', (SELECT id FROM orgaos WHERE codigo = '06'), TRUE),
('06.02', 'Departamento de Proteção Social Especial', (SELECT id FROM orgaos WHERE codigo = '06'), TRUE),
-- Secretaria de Agricultura
('07.01', 'Departamento de Fomento Agropecuário', (SELECT id FROM orgaos WHERE codigo = '07'), TRUE),
-- Secretaria de Cultura
('08.01', 'Departamento de Cultura e Eventos', (SELECT id FROM orgaos WHERE codigo = '08'), TRUE),
('08.02', 'Departamento de Esportes', (SELECT id FROM orgaos WHERE codigo = '08'), TRUE),
-- Câmara Municipal
('09.01', 'Diretoria Geral da Câmara', (SELECT id FROM orgaos WHERE codigo = '09'), TRUE),
-- Fundo Municipal de Saúde
('10.01', 'Gestão do Fundo de Saúde', (SELECT id FROM orgaos WHERE codigo = '10'), TRUE),
-- Fundo Municipal de Assistência Social
('11.01', 'Gestão do Fundo de Assistência Social', (SELECT id FROM orgaos WHERE codigo = '11'), TRUE);

-- ─────────────────────────────────────────────────────
-- Programas (baseados em PPA real de prefeitura)
-- ─────────────────────────────────────────────────────

INSERT INTO programas (codigo, descricao, objetivo, publico_alvo, tipo, ativo) VALUES
('0001', 'Gestão Administrativa Municipal', 'Garantir o funcionamento da máquina administrativa', 'Servidores e cidadãos', 'GESTAO', TRUE),
('0002', 'Gestão Legislativa', 'Assegurar o funcionamento do Poder Legislativo', 'Vereadores e cidadãos', 'GESTAO', TRUE),
('0003', 'Gestão Financeira e Orçamentária', 'Gerenciar as finanças e o orçamento municipal', 'Administração pública', 'GESTAO', TRUE),
('1001', 'Educação de Qualidade para Todos', 'Garantir acesso e qualidade na educação municipal', 'Alunos da rede municipal', 'TEMATICO', TRUE),
('1002', 'Saúde Pública e Bem-Estar', 'Promover saúde e qualidade de vida à população', 'População em geral', 'TEMATICO', TRUE),
('1003', 'Assistência e Desenvolvimento Social', 'Fortalecer a proteção social e combater a vulnerabilidade', 'Famílias em situação de vulnerabilidade', 'TEMATICO', TRUE),
('1004', 'Infraestrutura e Desenvolvimento Urbano', 'Melhorar a infraestrutura urbana e rural', 'Comunidade local', 'TEMATICO', TRUE),
('1005', 'Agricultura e Desenvolvimento Rural', 'Fomentar a produção agropecuária e a economia rural', 'Produtores rurais', 'TEMATICO', TRUE),
('1006', 'Cultura, Esporte e Lazer', 'Promover atividades culturais, esportivas e de lazer', 'População em geral', 'TEMATICO', TRUE),
('1007', 'Meio Ambiente e Sustentabilidade', 'Promover a preservação ambiental e o desenvolvimento sustentável', 'População e meio ambiente', 'TEMATICO', TRUE),
('1008', 'Segurança e Defesa Civil', 'Garantir a segurança e a defesa civil do município', 'População em geral', 'TEMATICO', TRUE),
('9999', 'Reserva de Contingência', 'Atender passivos contingentes e eventos fiscais imprevistos', 'Administração pública', 'ESPECIAL', TRUE);

-- ─────────────────────────────────────────────────────
-- Ações (baseadas em tipologia real da SOF)
-- ─────────────────────────────────────────────────────

INSERT INTO acoes (codigo, descricao, tipo, programa_id, funcao_id, subfuncao_id, ativo) VALUES
-- Programa 0001 - Gestão Administrativa
('2001', 'Manutenção do Gabinete do Prefeito', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0001'),
  (SELECT id FROM funcoes WHERE codigo = '04'),
  (SELECT id FROM subfuncoes WHERE codigo = '122'), TRUE),
('2002', 'Manutenção da Secretaria de Administração', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0001'),
  (SELECT id FROM funcoes WHERE codigo = '04'),
  (SELECT id FROM subfuncoes WHERE codigo = '122'), TRUE),
('2003', 'Gestão de Recursos Humanos', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0001'),
  (SELECT id FROM funcoes WHERE codigo = '04'),
  (SELECT id FROM subfuncoes WHERE codigo = '128'), TRUE),
('2004', 'Gestão de Tecnologia da Informação', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0001'),
  (SELECT id FROM funcoes WHERE codigo = '04'),
  (SELECT id FROM subfuncoes WHERE codigo = '126'), TRUE),

-- Programa 0002 - Gestão Legislativa
('2010', 'Manutenção da Câmara Municipal', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0002'),
  (SELECT id FROM funcoes WHERE codigo = '01'),
  (SELECT id FROM subfuncoes WHERE codigo = '031'), TRUE),

-- Programa 0003 - Gestão Financeira
('2020', 'Gestão da Dívida Pública Municipal', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0003'),
  (SELECT id FROM funcoes WHERE codigo = '28'),
  (SELECT id FROM subfuncoes WHERE codigo = '843'), TRUE),
('2021', 'Administração Tributária e Arrecadação', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '0003'),
  (SELECT id FROM funcoes WHERE codigo = '04'),
  (SELECT id FROM subfuncoes WHERE codigo = '129'), TRUE),

-- Programa 1001 - Educação
('2101', 'Manutenção do Ensino Fundamental', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1001'),
  (SELECT id FROM funcoes WHERE codigo = '12'),
  (SELECT id FROM subfuncoes WHERE codigo = '361'), TRUE),
('2102', 'Manutenção da Educação Infantil', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1001'),
  (SELECT id FROM funcoes WHERE codigo = '12'),
  (SELECT id FROM subfuncoes WHERE codigo = '365'), TRUE),
('2103', 'Transporte Escolar', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1001'),
  (SELECT id FROM funcoes WHERE codigo = '12'),
  (SELECT id FROM subfuncoes WHERE codigo = '361'), TRUE),
('2104', 'Alimentação Escolar – PNAE', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1001'),
  (SELECT id FROM funcoes WHERE codigo = '12'),
  (SELECT id FROM subfuncoes WHERE codigo = '306'), TRUE),
('1101', 'Construção e Ampliação de Escolas', 'PROJETO',
  (SELECT id FROM programas WHERE codigo = '1001'),
  (SELECT id FROM funcoes WHERE codigo = '12'),
  (SELECT id FROM subfuncoes WHERE codigo = '361'), TRUE),

-- Programa 1002 - Saúde
('2201', 'Manutenção da Atenção Básica – ESF', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1002'),
  (SELECT id FROM funcoes WHERE codigo = '10'),
  (SELECT id FROM subfuncoes WHERE codigo = '301'), TRUE),
('2202', 'Assistência Farmacêutica', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1002'),
  (SELECT id FROM funcoes WHERE codigo = '10'),
  (SELECT id FROM subfuncoes WHERE codigo = '303'), TRUE),
('2203', 'Vigilância Epidemiológica e Sanitária', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1002'),
  (SELECT id FROM funcoes WHERE codigo = '10'),
  (SELECT id FROM subfuncoes WHERE codigo = '305'), TRUE),
('1201', 'Construção e Reforma de UBS', 'PROJETO',
  (SELECT id FROM programas WHERE codigo = '1002'),
  (SELECT id FROM funcoes WHERE codigo = '10'),
  (SELECT id FROM subfuncoes WHERE codigo = '301'), TRUE),

-- Programa 1003 - Assistência Social
('2301', 'Manutenção do CRAS', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1003'),
  (SELECT id FROM funcoes WHERE codigo = '08'),
  (SELECT id FROM subfuncoes WHERE codigo = '244'), TRUE),
('2302', 'Manutenção do CREAS', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1003'),
  (SELECT id FROM funcoes WHERE codigo = '08'),
  (SELECT id FROM subfuncoes WHERE codigo = '244'), TRUE),
('2303', 'Benefícios Eventuais e Transferência de Renda', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1003'),
  (SELECT id FROM funcoes WHERE codigo = '08'),
  (SELECT id FROM subfuncoes WHERE codigo = '244'), TRUE),

-- Programa 1004 - Infraestrutura
('2401', 'Manutenção de Vias e Logradouros', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1004'),
  (SELECT id FROM funcoes WHERE codigo = '15'),
  (SELECT id FROM subfuncoes WHERE codigo = '451'), TRUE),
('2402', 'Iluminação Pública', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1004'),
  (SELECT id FROM funcoes WHERE codigo = '15'),
  (SELECT id FROM subfuncoes WHERE codigo = '452'), TRUE),
('2403', 'Limpeza Urbana e Coleta de Resíduos', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1004'),
  (SELECT id FROM funcoes WHERE codigo = '15'),
  (SELECT id FROM subfuncoes WHERE codigo = '452'), TRUE),
('1401', 'Pavimentação de Ruas e Avenidas', 'PROJETO',
  (SELECT id FROM programas WHERE codigo = '1004'),
  (SELECT id FROM funcoes WHERE codigo = '15'),
  (SELECT id FROM subfuncoes WHERE codigo = '451'), TRUE),

-- Programa 1005 - Agricultura
('2501', 'Apoio ao Produtor Rural', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1005'),
  (SELECT id FROM funcoes WHERE codigo = '20'),
  (SELECT id FROM subfuncoes WHERE codigo = '606'), TRUE),

-- Programa 1006 - Cultura
('2601', 'Promoção de Eventos Culturais', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1006'),
  (SELECT id FROM funcoes WHERE codigo = '13'),
  (SELECT id FROM subfuncoes WHERE codigo = '392'), TRUE),
('2602', 'Manutenção de Espaços Esportivos', 'ATIVIDADE',
  (SELECT id FROM programas WHERE codigo = '1006'),
  (SELECT id FROM funcoes WHERE codigo = '27'),
  (SELECT id FROM subfuncoes WHERE codigo = '812'), TRUE),

-- Programa 9999 - Reserva de Contingência
('9999', 'Reserva de Contingência', 'OPERACAO_ESPECIAL',
  (SELECT id FROM programas WHERE codigo = '9999'),
  (SELECT id FROM funcoes WHERE codigo = '99'),
  (SELECT id FROM subfuncoes WHERE codigo = '999'), TRUE);

-- ─────────────────────────────────────────────────────
-- PPA 2022-2025 (encerrado) e PPA 2026-2029 (vigente)
-- Seguem o ciclo padrão de 4 anos (Art. 165 da CF/88)
-- ─────────────────────────────────────────────────────

INSERT INTO ppas (descricao, ano_inicio, ano_fim, lei_numero, lei_data, status, observacoes) VALUES
('PPA 2022-2025 – Município Avança', 2022, 2025, 'Lei nº 1.850/2021', '2021-12-15', 'ENCERRADO',
 'Plano Plurianual aprovado pela Câmara Municipal em dezembro de 2021.'),
('PPA 2026-2029 – Desenvolvimento Sustentável', 2026, 2029, 'Lei nº 2.100/2025', '2025-12-18', 'VIGENTE',
 'Plano Plurianual vigente. Foco em educação, saúde e infraestrutura.');

-- ─────────────────────────────────────────────────────
-- PPA_PROGRAMAS — Vínculo dos programas ao PPA vigente
-- ─────────────────────────────────────────────────────

INSERT INTO ppa_programas (ppa_id, programa_id, orgao_id, valor_global, meta_fisica_global) VALUES
((SELECT id FROM ppas WHERE ano_inicio = 2026),
 (SELECT id FROM programas WHERE codigo = '0001'),
 (SELECT id FROM orgaos WHERE codigo = '02'), 12000000.00, NULL),

((SELECT id FROM ppas WHERE ano_inicio = 2026),
 (SELECT id FROM programas WHERE codigo = '1001'),
 (SELECT id FROM orgaos WHERE codigo = '03'), 48000000.00, 5000),

((SELECT id FROM ppas WHERE ano_inicio = 2026),
 (SELECT id FROM programas WHERE codigo = '1002'),
 (SELECT id FROM orgaos WHERE codigo = '04'), 36000000.00, 120000),

((SELECT id FROM ppas WHERE ano_inicio = 2026),
 (SELECT id FROM programas WHERE codigo = '1003'),
 (SELECT id FROM orgaos WHERE codigo = '06'), 8000000.00, 3000),

((SELECT id FROM ppas WHERE ano_inicio = 2026),
 (SELECT id FROM programas WHERE codigo = '1004'),
 (SELECT id FROM orgaos WHERE codigo = '05'), 20000000.00, 50);

-- ─────────────────────────────────────────────────────
-- LDOs — Uma por exercício dentro do PPA
-- (Art. 165 §2º da CF/88)
-- ─────────────────────────────────────────────────────

-- LDO do PPA anterior (2025 - encerrada)
INSERT INTO ldos (ppa_id, exercicio, descricao, lei_numero, lei_data, meta_fiscal_receita, meta_fiscal_despesa, meta_resultado_primario, status) VALUES
((SELECT id FROM ppas WHERE ano_inicio = 2022), 2025,
 'LDO 2025 – Diretrizes Orçamentárias', 'Lei nº 2.050/2024', '2024-07-10',
 95000000.00, 93000000.00, 2000000.00, 'ENCERRADA');

-- LDOs do PPA vigente
INSERT INTO ldos (ppa_id, exercicio, descricao, lei_numero, lei_data, meta_fiscal_receita, meta_fiscal_despesa, meta_resultado_primario, status) VALUES
((SELECT id FROM ppas WHERE ano_inicio = 2026), 2026,
 'LDO 2026 – Diretrizes Orçamentárias', 'Lei nº 2.090/2025', '2025-07-15',
 105000000.00, 102000000.00, 3000000.00, 'VIGENTE'),

((SELECT id FROM ppas WHERE ano_inicio = 2026), 2027,
 'LDO 2027 – Diretrizes Orçamentárias', NULL, NULL,
 110000000.00, 107000000.00, 3000000.00, 'ELABORACAO'),

((SELECT id FROM ppas WHERE ano_inicio = 2026), 2028,
 'LDO 2028 – Diretrizes Orçamentárias', NULL, NULL,
 115000000.00, 112000000.00, 3000000.00, 'ELABORACAO'),

((SELECT id FROM ppas WHERE ano_inicio = 2026), 2029,
 'LDO 2029 – Diretrizes Orçamentárias', NULL, NULL,
 120000000.00, 117000000.00, 3000000.00, 'ELABORACAO');

-- ─────────────────────────────────────────────────────
-- LOAs — Uma por exercício, vinculada à LDO respectiva
-- (Art. 165 §5º da CF/88)
-- ─────────────────────────────────────────────────────

-- LOA do exercício encerrado
INSERT INTO loas (ldo_id, exercicio, descricao, lei_numero, lei_data, valor_total_receita, valor_total_despesa, status) VALUES
((SELECT id FROM ldos WHERE exercicio = 2025), 2025,
 'LOA 2025 – Orçamento Anual', 'Lei nº 2.070/2024', '2024-12-20',
 95000000.00, 95000000.00, 'ENCERRADA');

-- LOA vigente
INSERT INTO loas (ldo_id, exercicio, descricao, lei_numero, lei_data, valor_total_receita, valor_total_despesa, status) VALUES
((SELECT id FROM ldos WHERE exercicio = 2026), 2026,
 'LOA 2026 – Orçamento Anual', 'Lei nº 2.110/2025', '2025-12-22',
 105000000.00, 105000000.00, 'VIGENTE');

-- LOA em elaboração
INSERT INTO loas (ldo_id, exercicio, descricao, valor_total_receita, valor_total_despesa, status) VALUES
((SELECT id FROM ldos WHERE exercicio = 2027), 2027,
 'LOA 2027 – Orçamento Anual',
 110000000.00, 110000000.00, 'ELABORACAO');

-- ─────────────────────────────────────────────────────
-- LDO_PRIORIDADES — Ações prioritárias para 2026
-- ─────────────────────────────────────────────────────

INSERT INTO ldo_prioridades (ldo_id, acao_id, unidade_id, valor_estimado, meta_fisica, justificativa) VALUES
((SELECT id FROM ldos WHERE exercicio = 2026),
 (SELECT id FROM acoes WHERE codigo = '2101'),
 (SELECT id FROM unidades WHERE codigo = '03.01'),
 12000000.00, 2500, 'Manutenção das escolas de ensino fundamental com foco na melhoria da qualidade'),

((SELECT id FROM ldos WHERE exercicio = 2026),
 (SELECT id FROM acoes WHERE codigo = '2201'),
 (SELECT id FROM unidades WHERE codigo = '04.01'),
 8500000.00, 60000, 'Manutenção da Estratégia Saúde da Família em todas as UBS'),

((SELECT id FROM ldos WHERE exercicio = 2026),
 (SELECT id FROM acoes WHERE codigo = '1401'),
 (SELECT id FROM unidades WHERE codigo = '05.01'),
 5000000.00, 15, 'Pavimentação de 15 km de vias urbanas – prioridade máxima'),

((SELECT id FROM ldos WHERE exercicio = 2026),
 (SELECT id FROM acoes WHERE codigo = '1101'),
 (SELECT id FROM unidades WHERE codigo = '03.01'),
 3500000.00, 2, 'Construção de 2 novas escolas de educação infantil'),

((SELECT id FROM ldos WHERE exercicio = 2026),
 (SELECT id FROM acoes WHERE codigo = '2301'),
 (SELECT id FROM unidades WHERE codigo = '06.01'),
 1200000.00, 1500, 'Manutenção do CRAS e ampliação do atendimento a famílias vulneráveis');

-- ─────────────────────────────────────────────────────
-- DOTAÇÕES ORÇAMENTÁRIAS — Exemplos para a LOA 2026
-- ─────────────────────────────────────────────────────

INSERT INTO dotacoes_orcamentarias (loa_id, orgao_id, unidade_id, funcao_id, subfuncao_id, programa_id, acao_id, natureza_despesa_id, fonte_recurso_id, valor_inicial, descricao) VALUES
-- Educação - Ensino Fundamental - Pessoal
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '03'),
 (SELECT id FROM unidades WHERE codigo = '03.01'),
 (SELECT id FROM funcoes WHERE codigo = '12'),
 (SELECT id FROM subfuncoes WHERE codigo = '361'),
 (SELECT id FROM programas WHERE codigo = '1001'),
 (SELECT id FROM acoes WHERE codigo = '2101'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.1.90.11'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1540'),
 8500000.00, 'Pessoal docente - Ensino Fundamental - FUNDEB 70%'),

-- Educação - Ensino Fundamental - Material de Consumo
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '03'),
 (SELECT id FROM unidades WHERE codigo = '03.01'),
 (SELECT id FROM funcoes WHERE codigo = '12'),
 (SELECT id FROM subfuncoes WHERE codigo = '361'),
 (SELECT id FROM programas WHERE codigo = '1001'),
 (SELECT id FROM acoes WHERE codigo = '2101'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.3.90.30'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1501'),
 850000.00, 'Material didático e de consumo - Ensino Fundamental'),

-- Educação - Transporte Escolar
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '03'),
 (SELECT id FROM unidades WHERE codigo = '03.03'),
 (SELECT id FROM funcoes WHERE codigo = '12'),
 (SELECT id FROM subfuncoes WHERE codigo = '361'),
 (SELECT id FROM programas WHERE codigo = '1001'),
 (SELECT id FROM acoes WHERE codigo = '2103'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.3.90.39'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1553'),
 1200000.00, 'Contratação de transporte escolar'),

-- Saúde - Atenção Básica - Pessoal ESF
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '04'),
 (SELECT id FROM unidades WHERE codigo = '04.01'),
 (SELECT id FROM funcoes WHERE codigo = '10'),
 (SELECT id FROM subfuncoes WHERE codigo = '301'),
 (SELECT id FROM programas WHERE codigo = '1002'),
 (SELECT id FROM acoes WHERE codigo = '2201'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.1.90.11'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1600'),
 6200000.00, 'Pessoal da Estratégia Saúde da Família'),

-- Saúde - Medicamentos
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '04'),
 (SELECT id FROM unidades WHERE codigo = '04.01'),
 (SELECT id FROM funcoes WHERE codigo = '10'),
 (SELECT id FROM subfuncoes WHERE codigo = '303'),
 (SELECT id FROM programas WHERE codigo = '1002'),
 (SELECT id FROM acoes WHERE codigo = '2202'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.3.90.30'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1600'),
 950000.00, 'Aquisição de medicamentos - Assistência Farmacêutica'),

-- Obras - Pavimentação
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '05'),
 (SELECT id FROM unidades WHERE codigo = '05.01'),
 (SELECT id FROM funcoes WHERE codigo = '15'),
 (SELECT id FROM subfuncoes WHERE codigo = '451'),
 (SELECT id FROM programas WHERE codigo = '1004'),
 (SELECT id FROM acoes WHERE codigo = '1401'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '4.4.90.51'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'),
 5000000.00, 'Pavimentação asfáltica de vias urbanas'),

-- Administração - Manutenção - Serviços TI
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '02'),
 (SELECT id FROM unidades WHERE codigo = '02.01'),
 (SELECT id FROM funcoes WHERE codigo = '04'),
 (SELECT id FROM subfuncoes WHERE codigo = '126'),
 (SELECT id FROM programas WHERE codigo = '0001'),
 (SELECT id FROM acoes WHERE codigo = '2004'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.3.90.40'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'),
 480000.00, 'Serviços de TI - licenças, internet e sistemas'),

-- Assistência Social - CRAS
((SELECT id FROM loas WHERE exercicio = 2026),
 (SELECT id FROM orgaos WHERE codigo = '06'),
 (SELECT id FROM unidades WHERE codigo = '06.01'),
 (SELECT id FROM funcoes WHERE codigo = '08'),
 (SELECT id FROM subfuncoes WHERE codigo = '244'),
 (SELECT id FROM programas WHERE codigo = '1003'),
 (SELECT id FROM acoes WHERE codigo = '2301'),
 (SELECT id FROM naturezas_despesa WHERE codigo = '3.3.90.39'),
 (SELECT id FROM fontes_recurso WHERE codigo = '1621'),
 750000.00, 'Manutenção do CRAS - serviços e custeio');

-- ─────────────────────────────────────────────────────
-- RECEITAS PREVISTAS — LOA 2026
-- ─────────────────────────────────────────────────────

INSERT INTO receitas_previstas (loa_id, codigo, descricao, categoria_economica, fonte_recurso_id, valor_previsto) VALUES
((SELECT id FROM loas WHERE exercicio = 2026), '1.1.1.2.01', 'IPTU – Imposto Predial e Territorial Urbano', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'), 12000000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.1.1.2.02', 'ITBI – Imposto de Transmissão de Bens Imóveis', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'), 3500000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.1.1.3.03', 'ISS – Imposto Sobre Serviços', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'), 8500000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.7.2.1.01', 'FPM – Fundo de Participação dos Municípios', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'), 35000000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.7.2.1.36', 'Transferências do FUNDEB', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1540'), 18500000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.7.1.3.01', 'Transferências do SUS – Atenção Básica', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1600'), 9800000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.7.1.3.02', 'Transferências do SUS – Média e Alta Complexidade', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1600'), 4200000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.7.1.4.01', 'Transferências do FNAS – SUAS', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1621'), 2800000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.7.2.2.01', 'ICMS – Cota Parte', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'), 8200000.00),
((SELECT id FROM loas WHERE exercicio = 2026), '1.3.1.1.01', 'Receita de Aluguéis de Imóveis', '1',
 (SELECT id FROM fontes_recurso WHERE codigo = '1500'), 350000.00);
