-- =====================================================
-- V13 — Reativar submenus de RH agora implementados
--
-- Os 7 submenus foram desativados na V12 pois ainda
-- não existiam componentes no frontend.
-- Agora que estão implementados, reativamos.
-- =====================================================

UPDATE menus SET ativo = TRUE
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
