// ── Plano de Contas (PCASP) ─────────────────────────────
export interface PlanoContasListItem {
  id: number;
  codigo: string;
  descricao: string;
  classe: number;
  nivel: number;
  tipo: string;
  natureza: string;
}

export interface PlanoContasRequest {
  codigo: string;
  descricao: string;
  classe: number;
  nivel: number;
  tipo: string;
  natureza?: string;
  escrituracao?: string;
  parentId?: number;
}

export interface PlanoContasResponse extends PlanoContasRequest {
  id: number;
  parentCodigo?: string;
  parentDescricao?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Conta Bancária ──────────────────────────────────────
export interface ContaBancariaListItem {
  id: number;
  bancoCodigo: string;
  bancoNome: string;
  agencia: string;
  numeroConta: string;
  descricao: string;
  tipo: string;
  saldoAtual: number;
  ativo: boolean;
}

export interface ContaBancariaRequest {
  bancoCodigo: string;
  bancoNome: string;
  agencia: string;
  numeroConta: string;
  digito?: string;
  descricao: string;
  tipo?: string;
  fonteRecursoId?: number;
  saldoAtual?: number;
  ativo?: boolean;
}

export interface ContaBancariaResponse extends ContaBancariaRequest {
  id: number;
  fonteRecursoCodigo?: string;
  fonteRecursoDescricao?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Liquidação ──────────────────────────────────────────
export interface LiquidacaoListItem {
  id: number;
  numeroLiquidacao: string;
  empenhoNumero: string;
  fornecedorNome: string;
  dataLiquidacao: string;
  valor: number;
  status: string;
}

export interface LiquidacaoRequest {
  empenhoId: number;
  numeroLiquidacao: string;
  dataLiquidacao: string;
  valor: number;
  tipoDocumentoFiscal?: string;
  numeroDocumentoFiscal?: string;
  dataDocumentoFiscal?: string;
  descricao?: string;
  status?: string;
  observacoes?: string;
}

export interface LiquidacaoResponse extends LiquidacaoRequest {
  id: number;
  empenhoNumero: string;
  fornecedorNome: string;
  createdAt: string;
  updatedAt: string;
}

// ── Pagamento ───────────────────────────────────────────
export interface PagamentoListItem {
  id: number;
  numeroPagamento: string;
  liquidacaoNumero: string;
  fornecedorNome: string;
  dataPagamento: string;
  valor: number;
  formaPagamento: string;
  status: string;
}

export interface PagamentoRequest {
  liquidacaoId: number;
  contaBancariaId: number;
  numeroPagamento: string;
  dataPagamento: string;
  valor: number;
  formaPagamento?: string;
  numeroDocumento?: string;
  descricao?: string;
  status?: string;
  observacoes?: string;
}

export interface PagamentoResponse extends PagamentoRequest {
  id: number;
  liquidacaoNumero: string;
  empenhoNumero: string;
  fornecedorNome: string;
  contaBancariaDescricao: string;
  createdAt: string;
  updatedAt: string;
}

// ── Receita Orçamentária ────────────────────────────────
export interface ReceitaOrcamentariaListItem {
  id: number;
  exercicio: number;
  codigoReceita: string;
  descricao: string;
  categoriaEconomica: string;
  valorPrevistoInicial: number;
  valorArrecadado: number;
  valorRecolhido: number;
}

export interface ReceitaOrcamentariaRequest {
  loaId: number;
  exercicio: number;
  codigoReceita: string;
  descricao: string;
  categoriaEconomica: string;
  origem?: string;
  especie?: string;
  fonteRecursoId?: number;
  valorPrevistoInicial?: number;
  valorPrevistoAtualizado?: number;
  valorLancado?: number;
  valorArrecadado?: number;
  valorRecolhido?: number;
}

export interface ReceitaOrcamentariaResponse extends ReceitaOrcamentariaRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}

// ── Crédito Adicional ───────────────────────────────────
export interface CreditoAdicionalListItem {
  id: number;
  exercicio: number;
  tipo: string;
  numeroDecreto: string;
  numeroLei: string;
  valor: number;
  status: string;
}

export interface CreditoAdicionalRequest {
  dotacaoId: number;
  exercicio: number;
  tipo: string;
  numeroDecreto?: string;
  dataDecreto?: string;
  numeroLei?: string;
  dataLei?: string;
  valor: number;
  fonteAnulacao?: string;
  justificativa?: string;
  status?: string;
}

export interface CreditoAdicionalResponse {
  id: number;
  exercicio: number;
  tipo: string;
  numeroDecreto?: string;
  dataDecreto?: string;
  numeroLei?: string;
  dataLei?: string;
  dotacao?: any;
  dotacaoId?: number;
  valor: number;
  fonteAnulacao?: string;
  justificativa?: string;
  status?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Restos a Pagar ──────────────────────────────────────
export interface RestosAPagarListItem {
  id: number;
  exercicioOrigem: number;
  numeroEmpenho: string;
  fornecedorNome: string;
  tipo: string;
  valorInscrito: number;
  valorPago: number;
  dataInscricao: string;
  status: string;
}

export interface RestosAPagarRequest {
  empenhoId: number;
  exercicioOrigem: number;
  tipo: string;
  dataInscricao: string;
  valorInscrito: number;
  valorCancelado?: number;
  valorPago?: number;
  valorALiquidar?: number;
  status?: string;
  observacoes?: string;
}

export interface RestosAPagarResponse extends RestosAPagarRequest {
  id: number;
  empenhoNumero: string;
  fornecedorNome: string;
  createdAt: string;
  updatedAt: string;
}

// ── Lançamento Contábil ─────────────────────────────────
export interface LancamentoContabilListItem {
  id: number;
  exercicio: number;
  numeroLancamento: string;
  dataLancamento: string;
  tipo: string;
  origem: string;
  historico: string;
  totalDebito: number;
  totalCredito: number;
}

export interface LancamentoItemDTO {
  planoContaId: number;
  tipo: string;
  valor: number;
  historico?: string;
}

export interface LancamentoContabilRequest {
  exercicio: number;
  numeroLancamento: string;
  dataLancamento: string;
  tipo: string;
  origem?: string;
  historico: string;
  empenhoId?: number;
  liquidacaoId?: number;
  pagamentoId?: number;
  itens: LancamentoItemDTO[];
}

export interface LancamentoContabilResponse extends LancamentoContabilRequest {
  id: number;
  totalDebito: number;
  totalCredito: number;
  createdAt: string;
  updatedAt: string;
}

// ── Bem Patrimonial ─────────────────────────────────────
export interface BemPatrimonialListItem {
  id: number;
  numeroPatrimonio: string;
  descricao: string;
  tipo: string;
  orgaoNome: string;
  dataAquisicao: string;
  valorAtual: number;
  estadoConservacao: string;
  situacao: string;
}

export interface BemPatrimonialRequest {
  numeroPatrimonio: string;
  descricao: string;
  tipo: string;
  orgaoId: number;
  unidadeId?: number;
  fornecedorId?: number;
  empenhoId?: number;
  planoContaId?: number;
  dataAquisicao: string;
  valorOriginal: number;
  valorAtual?: number;
  vidaUtilAnos?: number;
  valorResidual?: number;
  depreciacaoAcumulada?: number;
  metodoDepreciacao?: string;
  estadoConservacao?: string;
  localizacao?: string;
  responsavel?: string;
  situacao?: string;
  dataBaixa?: string;
  motivoBaixa?: string;
  observacoes?: string;
}

export interface BemPatrimonialResponse extends BemPatrimonialRequest {
  id: number;
  orgaoNome: string;
  unidadeNome: string;
  createdAt: string;
  updatedAt: string;
}

// ── Dívida Pública ──────────────────────────────────────
export interface DividaPublicaListItem {
  id: number;
  tipo: string;
  credor: string;
  numeroContrato: string;
  dataContratacao: string;
  valorOriginal: number;
  saldoDevedor: number;
  status: string;
}

export interface DividaPublicaRequest {
  tipo: string;
  credor: string;
  cnpjCredor?: string;
  numeroContrato?: string;
  dataContratacao: string;
  dataVencimento?: string;
  valorOriginal: number;
  saldoDevedor?: number;
  taxaJuros?: number;
  indexador?: string;
  finalidade?: string;
  leiAutorizativa?: string;
  status?: string;
  observacoes?: string;
}

export interface DividaPublicaResponse extends DividaPublicaRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}

// ── Conciliação Bancária ────────────────────────────────
export interface ConciliacaoBancariaListItem {
  id: number;
  contaBancariaDescricao: string;
  mesReferencia: number;
  anoReferencia: number;
  saldoExtrato: number;
  saldoContabil: number;
  diferenca: number;
  status: string;
}

export interface ConciliacaoBancariaRequest {
  contaBancariaId: number;
  mesReferencia: number;
  anoReferencia: number;
  saldoExtrato: number;
  saldoContabil: number;
  observacoes?: string;
  status?: string;
}

export interface ConciliacaoBancariaResponse extends ConciliacaoBancariaRequest {
  id: number;
  contaBancariaDescricao: string;
  diferenca: number;
  createdAt: string;
  updatedAt: string;
}

// ── Balancete ───────────────────────────────────────────
export interface BalanceteListItem {
  id: number;
  exercicio: number;
  mes: number;
  planoContaCodigo: string;
  planoContaDescricao: string;
  saldoAnterior: number;
  totalDebitos: number;
  totalCreditos: number;
  saldoAtual: number;
}

// ── Demonstração Contábil ───────────────────────────────
export interface DemonstracaoContabilListItem {
  id: number;
  exercicio: number;
  tipo: string;
  periodoInicio: string;
  periodoFim: string;
  status: string;
}

export interface DemonstracaoContabilRequest {
  exercicio: number;
  tipo: string;
  periodoInicio: string;
  periodoFim: string;
  responsavelNome?: string;
  responsavelCargo?: string;
  contadorNome?: string;
  contadorCrc?: string;
  status?: string;
  observacoes?: string;
}

export interface DemonstracaoContabilResponse extends DemonstracaoContabilRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}

// ── Relatório RREO ──────────────────────────────────────
export interface RelatorioRreoListItem {
  id: number;
  exercicio: number;
  bimestre: number;
  receitaRealizada: number;
  despesaEmpenhada: number;
  receitaCorrenteLiquida: number;
  status: string;
}

export interface RelatorioRreoRequest {
  exercicio: number;
  bimestre: number;
  receitaPrevista?: number;
  receitaRealizada?: number;
  despesaFixada?: number;
  despesaEmpenhada?: number;
  despesaLiquidada?: number;
  despesaPaga?: number;
  receitaCorrenteLiquida?: number;
  superavitDeficit?: number;
  responsavelNome?: string;
  responsavelCargo?: string;
  contadorNome?: string;
  contadorCrc?: string;
  status?: string;
  observacoes?: string;
}

export interface RelatorioRreoResponse extends RelatorioRreoRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}

// ── Relatório RGF ───────────────────────────────────────
export interface RelatorioRgfListItem {
  id: number;
  exercicio: number;
  quadrimestre: number;
  receitaCorrenteLiquida: number;
  despesaPessoalTotal: number;
  percentualPessoalTotal: number;
  status: string;
}

export interface RelatorioRgfRequest {
  exercicio: number;
  quadrimestre: number;
  receitaCorrenteLiquida?: number;
  despesaPessoalLegislativo?: number;
  despesaPessoalExecutivo?: number;
  despesaPessoalTotal?: number;
  percentualPessoalLegislativo?: number;
  percentualPessoalExecutivo?: number;
  percentualPessoalTotal?: number;
  limitePrudencial?: number;
  limiteMaximo?: number;
  limiteAlerta?: number;
  dividaConsolidadaLiquida?: number;
  operacoesCreditoInternas?: number;
  operacoesCreditoExternas?: number;
  responsavelNome?: string;
  responsavelCargo?: string;
  contadorNome?: string;
  contadorCrc?: string;
  status?: string;
  observacoes?: string;
}

export interface RelatorioRgfResponse extends RelatorioRgfRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}
