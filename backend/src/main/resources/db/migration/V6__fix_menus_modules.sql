-- =====================================================
-- V6 — Complementos de menus
-- 1. Desativa menus da V1 que não possuem rotas implementadas
-- 2. Adiciona submenu "Dotações" ao módulo Planejamento
-- =====================================================

-- ─────────────────────────────────────────────────────
-- 1. Desativar menus da V1 que apontam para rotas não implementadas
-- ─────────────────────────────────────────────────────

-- Relatórios (/relatorios) — não implementado
UPDATE menus SET ativo = FALSE WHERE label = 'Relatórios' AND rota = '/relatorios' AND parent_id IS NULL;

-- Usuários e Perfis dentro de Administração — não implementados
UPDATE menus SET ativo = FALSE WHERE rota IN ('/admin/usuarios', '/admin/perfis');

-- Administração (menu pai vazio após desativar filhos) — desativar
UPDATE menus SET ativo = FALSE WHERE label = 'Administração' AND parent_id IS NULL;

-- ─────────────────────────────────────────────────────
-- 2. Adicionar menu "Dotações" ao módulo Planejamento
-- ─────────────────────────────────────────────────────

INSERT INTO menus (label, icone, rota, ordem, parent_id, ativo)
VALUES (
    'Dotações',
    'account_balance_wallet',
    '/dotacoes',
    9,
    (SELECT id FROM menus WHERE label = 'Planejamento' AND parent_id IS NULL),
    TRUE
);

-- Vincular "Dotações" à role ADMIN
INSERT INTO menu_roles (menu_id, role_id)
SELECT m.id, r.id
FROM menus m, roles r
WHERE m.label = 'Dotações' AND m.rota = '/dotacoes'
  AND r.nome = 'ADMIN';
