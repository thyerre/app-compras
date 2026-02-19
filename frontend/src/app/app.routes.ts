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

      // ── Contabilidade Pública ─────────────────────────────────────

      // Plano de Contas
      { path: 'contabilidade/plano-contas', loadComponent: () => import('./features/contabilidade/plano-contas/list/plano-contas-list.component').then(m => m.PlanoContasListComponent) },
      { path: 'contabilidade/plano-contas/:id', loadComponent: () => import('./features/contabilidade/plano-contas/form/plano-contas-form.component').then(m => m.PlanoContasFormComponent) },
      // Lançamentos Contábeis
      { path: 'contabilidade/lancamentos', loadComponent: () => import('./features/contabilidade/lancamentos/list/lancamento-list.component').then(m => m.LancamentoListComponent) },
      { path: 'contabilidade/lancamentos/:id', loadComponent: () => import('./features/contabilidade/lancamentos/form/lancamento-form.component').then(m => m.LancamentoFormComponent) },
      // Liquidações
      { path: 'contabilidade/liquidacoes', loadComponent: () => import('./features/contabilidade/liquidacoes/list/liquidacao-list.component').then(m => m.LiquidacaoListComponent) },
      { path: 'contabilidade/liquidacoes/:id', loadComponent: () => import('./features/contabilidade/liquidacoes/form/liquidacao-form.component').then(m => m.LiquidacaoFormComponent) },
      // Pagamentos
      { path: 'contabilidade/pagamentos', loadComponent: () => import('./features/contabilidade/pagamentos/list/pagamento-list.component').then(m => m.PagamentoListComponent) },
      { path: 'contabilidade/pagamentos/:id', loadComponent: () => import('./features/contabilidade/pagamentos/form/pagamento-form.component').then(m => m.PagamentoFormComponent) },
      // Contas Bancárias
      { path: 'contabilidade/contas-bancarias', loadComponent: () => import('./features/contabilidade/contas-bancarias/list/conta-bancaria-list.component').then(m => m.ContaBancariaListComponent) },
      { path: 'contabilidade/contas-bancarias/:id', loadComponent: () => import('./features/contabilidade/contas-bancarias/form/conta-bancaria-form.component').then(m => m.ContaBancariaFormComponent) },
      // Receita Orçamentária
      { path: 'contabilidade/receita-orcamentaria', loadComponent: () => import('./features/contabilidade/receita-orcamentaria/list/receita-orcamentaria-list.component').then(m => m.ReceitaOrcamentariaListComponent) },
      { path: 'contabilidade/receita-orcamentaria/:id', loadComponent: () => import('./features/contabilidade/receita-orcamentaria/form/receita-orcamentaria-form.component').then(m => m.ReceitaOrcamentariaFormComponent) },
      // Créditos Adicionais
      { path: 'contabilidade/creditos-adicionais', loadComponent: () => import('./features/contabilidade/creditos-adicionais/list/credito-adicional-list.component').then(m => m.CreditoAdicionalListComponent) },
      { path: 'contabilidade/creditos-adicionais/:id', loadComponent: () => import('./features/contabilidade/creditos-adicionais/form/credito-adicional-form.component').then(m => m.CreditoAdicionalFormComponent) },
      // Restos a Pagar
      { path: 'contabilidade/restos-a-pagar', loadComponent: () => import('./features/contabilidade/restos-a-pagar/list/restos-a-pagar-list.component').then(m => m.RestosAPagarListComponent) },
      { path: 'contabilidade/restos-a-pagar/:id', loadComponent: () => import('./features/contabilidade/restos-a-pagar/form/restos-a-pagar-form.component').then(m => m.RestosAPagarFormComponent) },
      // Bens Patrimoniais
      { path: 'contabilidade/bens-patrimoniais', loadComponent: () => import('./features/contabilidade/bens-patrimoniais/list/bem-patrimonial-list.component').then(m => m.BemPatrimonialListComponent) },
      { path: 'contabilidade/bens-patrimoniais/:id', loadComponent: () => import('./features/contabilidade/bens-patrimoniais/form/bem-patrimonial-form.component').then(m => m.BemPatrimonialFormComponent) },
      // Dívida Pública
      { path: 'contabilidade/divida-publica', loadComponent: () => import('./features/contabilidade/divida-publica/list/divida-publica-list.component').then(m => m.DividaPublicaListComponent) },
      { path: 'contabilidade/divida-publica/:id', loadComponent: () => import('./features/contabilidade/divida-publica/form/divida-publica-form.component').then(m => m.DividaPublicaFormComponent) },
      // Conciliação Bancária
      { path: 'contabilidade/conciliacao-bancaria', loadComponent: () => import('./features/contabilidade/conciliacao-bancaria/list/conciliacao-bancaria-list.component').then(m => m.ConciliacaoBancariaListComponent) },
      { path: 'contabilidade/conciliacao-bancaria/:id', loadComponent: () => import('./features/contabilidade/conciliacao-bancaria/form/conciliacao-bancaria-form.component').then(m => m.ConciliacaoBancariaFormComponent) },
      // Balancetes (somente listagem)
      { path: 'contabilidade/balancetes', loadComponent: () => import('./features/contabilidade/balancetes/list/balancete-list.component').then(m => m.BalanceteListComponent) },
      // Demonstrações Contábeis
      { path: 'contabilidade/demonstracoes', loadComponent: () => import('./features/contabilidade/demonstracoes/list/demonstracao-list.component').then(m => m.DemonstracaoListComponent) },
      { path: 'contabilidade/demonstracoes/:id', loadComponent: () => import('./features/contabilidade/demonstracoes/form/demonstracao-form.component').then(m => m.DemonstracaoFormComponent) },
      // RREO
      { path: 'contabilidade/rreo', loadComponent: () => import('./features/contabilidade/rreo/list/rreo-list.component').then(m => m.RreoListComponent) },
      { path: 'contabilidade/rreo/:id', loadComponent: () => import('./features/contabilidade/rreo/form/rreo-form.component').then(m => m.RreoFormComponent) },
      // RGF
      { path: 'contabilidade/rgf', loadComponent: () => import('./features/contabilidade/rgf/list/rgf-list.component').then(m => m.RgfListComponent) },
      { path: 'contabilidade/rgf/:id', loadComponent: () => import('./features/contabilidade/rgf/form/rgf-form.component').then(m => m.RgfFormComponent) },

      // ── Arrecadação ───────────────────────────────────────────────

      // Contribuintes
      { path: 'arrecadacao/contribuintes', loadComponent: () => import('./features/arrecadacao/contribuintes/list/contribuinte-list.component').then(m => m.ContribuinteListComponent) },
      { path: 'arrecadacao/contribuintes/:id', loadComponent: () => import('./features/arrecadacao/contribuintes/form/contribuinte-form.component').then(m => m.ContribuinteFormComponent) },
      // Lançamentos Tributários
      { path: 'arrecadacao/lancamentos-tributarios', loadComponent: () => import('./features/arrecadacao/lancamentos-tributarios/list/lancamento-tributario-list.component').then(m => m.LancamentoTributarioListComponent) },
      { path: 'arrecadacao/lancamentos-tributarios/:id', loadComponent: () => import('./features/arrecadacao/lancamentos-tributarios/form/lancamento-tributario-form.component').then(m => m.LancamentoTributarioFormComponent) },
      // Guias de Arrecadação
      { path: 'arrecadacao/guias', loadComponent: () => import('./features/arrecadacao/guias/list/guia-arrecadacao-list.component').then(m => m.GuiaArrecadacaoListComponent) },
      { path: 'arrecadacao/guias/:id', loadComponent: () => import('./features/arrecadacao/guias/form/guia-arrecadacao-form.component').then(m => m.GuiaArrecadacaoFormComponent) },
      // Baixas de Arrecadação
      { path: 'arrecadacao/baixas', loadComponent: () => import('./features/arrecadacao/baixas/list/baixa-arrecadacao-list.component').then(m => m.BaixaArrecadacaoListComponent) },
      { path: 'arrecadacao/baixas/:id', loadComponent: () => import('./features/arrecadacao/baixas/form/baixa-arrecadacao-form.component').then(m => m.BaixaArrecadacaoFormComponent) },

      // ── Recursos Humanos ──────────────────────────────────────────

      // Cargos
      { path: 'rh/cargos', loadComponent: () => import('./features/rh/cargos/list/cargo-list.component').then(m => m.CargoListComponent) },
      { path: 'rh/cargos/:id', loadComponent: () => import('./features/rh/cargos/form/cargo-form.component').then(m => m.CargoFormComponent) },
      // Servidores
      { path: 'rh/servidores', loadComponent: () => import('./features/rh/servidores/list/servidor-list.component').then(m => m.ServidorListComponent) },
      { path: 'rh/servidores/:id', loadComponent: () => import('./features/rh/servidores/form/servidor-form.component').then(m => m.ServidorFormComponent) },
      // Folhas de Pagamento
      { path: 'rh/folhas-pagamento', loadComponent: () => import('./features/rh/folhas-pagamento/list/folha-pagamento-list.component').then(m => m.FolhaPagamentoListComponent) },
      { path: 'rh/folhas-pagamento/:id', loadComponent: () => import('./features/rh/folhas-pagamento/form/folha-pagamento-form.component').then(m => m.FolhaPagamentoFormComponent) },

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
