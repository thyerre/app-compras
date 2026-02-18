import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./shared/ui/layout/layout.component').then(m => m.LayoutComponent),
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
      },
      {
        path: 'fornecedores',
        loadComponent: () =>
          import('./features/fornecedores/list/fornecedor-list.component').then(m => m.FornecedorListComponent),
      },
      {
        path: 'fornecedores/:id/detalhe',
        loadComponent: () =>
          import('./features/fornecedores/detail/fornecedor-detail.component').then(m => m.FornecedorDetailComponent),
      },
      {
        path: 'fornecedores/:id',
        loadComponent: () =>
          import('./features/fornecedores/form/fornecedor-form.component').then(m => m.FornecedorFormComponent),
      },

      // ── Planejamento e Orçamento ──────────────────────────────────

      // Órgãos
      {
        path: 'orgaos',
        loadComponent: () =>
          import('./features/planejamento/orgaos/list/orgao-list.component').then(m => m.OrgaoListComponent),
      },
      {
        path: 'orgaos/:id',
        loadComponent: () =>
          import('./features/planejamento/orgaos/form/orgao-form.component').then(m => m.OrgaoFormComponent),
      },

      // Unidades
      {
        path: 'unidades',
        loadComponent: () =>
          import('./features/planejamento/unidades/list/unidade-list.component').then(m => m.UnidadeListComponent),
      },
      {
        path: 'unidades/:id',
        loadComponent: () =>
          import('./features/planejamento/unidades/form/unidade-form.component').then(m => m.UnidadeFormComponent),
      },

      // Programas
      {
        path: 'programas',
        loadComponent: () =>
          import('./features/planejamento/programas/list/programa-list.component').then(m => m.ProgramaListComponent),
      },
      {
        path: 'programas/:id',
        loadComponent: () =>
          import('./features/planejamento/programas/form/programa-form.component').then(m => m.ProgramaFormComponent),
      },

      // Ações
      {
        path: 'acoes',
        loadComponent: () =>
          import('./features/planejamento/acoes/list/acao-list.component').then(m => m.AcaoListComponent),
      },
      {
        path: 'acoes/:id',
        loadComponent: () =>
          import('./features/planejamento/acoes/form/acao-form.component').then(m => m.AcaoFormComponent),
      },

      // PPA
      {
        path: 'ppa',
        loadComponent: () =>
          import('./features/planejamento/ppa/list/ppa-list.component').then(m => m.PpaListComponent),
      },
      {
        path: 'ppa/:id',
        loadComponent: () =>
          import('./features/planejamento/ppa/form/ppa-form.component').then(m => m.PpaFormComponent),
      },

      // LDO
      {
        path: 'ldo',
        loadComponent: () =>
          import('./features/planejamento/ldo/list/ldo-list.component').then(m => m.LdoListComponent),
      },
      {
        path: 'ldo/:id',
        loadComponent: () =>
          import('./features/planejamento/ldo/form/ldo-form.component').then(m => m.LdoFormComponent),
      },

      // LOA
      {
        path: 'loa',
        loadComponent: () =>
          import('./features/planejamento/loa/list/loa-list.component').then(m => m.LoaListComponent),
      },
      {
        path: 'loa/:id',
        loadComponent: () =>
          import('./features/planejamento/loa/form/loa-form.component').then(m => m.LoaFormComponent),
      },

      // Dotações Orçamentárias
      {
        path: 'dotacoes',
        loadComponent: () =>
          import('./features/planejamento/dotacoes/list/dotacao-list.component').then(m => m.DotacaoListComponent),
      },
      {
        path: 'dotacoes/:id',
        loadComponent: () =>
          import('./features/planejamento/dotacoes/form/dotacao-form.component').then(m => m.DotacaoFormComponent),
      },

      // Receitas Previstas
      {
        path: 'receitas',
        loadComponent: () =>
          import('./features/planejamento/receitas/list/receita-list.component').then(m => m.ReceitaListComponent),
      },
      {
        path: 'receitas/:id',
        loadComponent: () =>
          import('./features/planejamento/receitas/form/receita-form.component').then(m => m.ReceitaFormComponent),
      },

      // ── Compras e Licitações ──────────────────────────────────────

      // Processos de Compra
      {
        path: 'processos',
        loadComponent: () =>
          import('./features/compras/processos/list/processo-list.component').then(m => m.ProcessoListComponent),
      },
      {
        path: 'processos/:id/detalhe',
        loadComponent: () =>
          import('./features/compras/processos/detail/processo-detail.component').then(m => m.ProcessoDetailComponent),
      },
      {
        path: 'processos/:id',
        loadComponent: () =>
          import('./features/compras/processos/form/processo-form.component').then(m => m.ProcessoFormComponent),
      },

      // Empenhos
      {
        path: 'empenhos',
        loadComponent: () =>
          import('./features/compras/empenhos/list/empenho-list.component').then(m => m.EmpenhoListComponent),
      },
      {
        path: 'empenhos/:id',
        loadComponent: () =>
          import('./features/compras/empenhos/form/empenho-form.component').then(m => m.EmpenhoFormComponent),
      },

      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];
