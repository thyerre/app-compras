-- =====================================================
-- V14 — Mover menu Fornecedores para dentro de Compras
--
-- Fornecedores (id=6) era um menu de nível raiz.
-- Agora será filho de Compras (id=2), aparecendo
-- como último item do submenu.
-- =====================================================

-- Tornar Fornecedores filho de Compras (id=2), ordem 4 (após Contratos)
UPDATE menus
SET parent_id = (SELECT id FROM menus WHERE label = 'Compras' AND parent_id IS NULL),
    ordem = 4,
    rota = '/fornecedores'
WHERE id = 6;
