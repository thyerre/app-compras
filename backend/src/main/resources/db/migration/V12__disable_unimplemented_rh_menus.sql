-- =====================================================
-- V12 — Desativar submenus ainda não implementados
--
-- Os seguintes submenus existem no banco (V10 e V11)
-- mas ainda não possuem telas no frontend.
-- Ao clicar neles o usuário era redirecionado para o login
-- (rota inexistente). Serão reativados quando os
-- componentes forem criados.
-- =====================================================

-- ── Recursos Humanos ────────────────────────────────────
UPDATE menus SET ativo = FALSE
WHERE parent_id = (SELECT id FROM menus WHERE label = 'Recursos Humanos' AND parent_id IS NULL)
  AND rota IN (
    '/rh/niveis-salariais',
    '/rh/dependentes',
    '/rh/eventos-folha',
    '/rh/ferias',
    '/rh/licencas',
    '/rh/historico-funcional',
    '/rh/consignados'
  );

-- ── Arrecadação ─────────────────────────────────────────
UPDATE menus SET ativo = FALSE
WHERE parent_id = (SELECT id FROM menus WHERE label = 'Arrecadação' AND parent_id IS NULL)
  AND rota IN (
    '/arrecadacao/imoveis',
    '/arrecadacao/atividades-economicas',
    '/arrecadacao/tributos',
    '/arrecadacao/divida-ativa',
    '/arrecadacao/parcelamentos',
    '/arrecadacao/agentes-arrecadadores',
    '/arrecadacao/certidoes'
  );
