// ── Contribuinte ─────────────────────────────────────────
export interface ContribuinteListItem {
  id: number;
  tipoPessoa: string;
  cpfCnpj: string;
  nomeRazaoSocial: string;
  nomeFantasia: string;
  inscricaoMunicipal: string;
  telefone: string;
  email: string;
  municipioNome: string;
  estadoSigla: string;
  ativo: boolean;
}

export interface ContribuinteRequest {
  tipoPessoa: string;
  cpfCnpj: string;
  nomeRazaoSocial: string;
  nomeFantasia?: string;
  inscricaoMunicipal?: string;
  email?: string;
  telefone?: string;
  celular?: string;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  municipioId?: number;
  estadoId?: number;
  ativo?: boolean;
  observacoes?: string;
}

export interface ContribuinteResponse extends ContribuinteRequest {
  id: number;
  municipio?: { id: number; nome: string; codigoIbge: string; estadoId: number; estadoSigla: string };
  estado?: { id: number; sigla: string; nome: string };
  createdAt: string;
  updatedAt: string;
}

// ── Tributo ─────────────────────────────────────────────
export interface Tributo {
  id: number;
  codigo: string;
  descricao: string;
  tipo: string;
  ativo: boolean;
}

// ── Lançamento Tributário ───────────────────────────────
export interface LancamentoTributarioListItem {
  id: number;
  numeroLancamento: string;
  exercicio: number;
  contribuinteNome: string;
  contribuinteCpfCnpj: string;
  tributoCodigo: string;
  tributoDescricao: string;
  dataLancamento: string;
  dataVencimento: string;
  valorTotal: number;
  status: string;
}

export interface LancamentoTributarioRequest {
  numeroLancamento: string;
  exercicio: number;
  contribuinteId: number;
  tributoId: number;
  imovelId?: number;
  atividadeEconomicaId?: number;
  dataLancamento: string;
  dataVencimento: string;
  baseCalculo?: number;
  aliquota?: number;
  valorPrincipal: number;
  valorDesconto?: number;
  valorJuros?: number;
  valorMulta?: number;
  valorTotal: number;
  status?: string;
  historico?: string;
}

export interface LancamentoTributarioResponse extends LancamentoTributarioRequest {
  id: number;
  contribuinteNome: string;
  contribuinteCpfCnpj: string;
  tributoCodigo: string;
  tributoDescricao: string;
  imovelInscricao?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Guia de Arrecadação (DAM) ───────────────────────────
export interface GuiaArrecadacaoListItem {
  id: number;
  numeroGuia: string;
  contribuinteNome: string;
  contribuinteCpfCnpj: string;
  dataEmissao: string;
  dataVencimento: string;
  valorTotal: number;
  status: string;
}

export interface GuiaArrecadacaoRequest {
  numeroGuia: string;
  lancamentoTributarioId: number;
  contribuinteId: number;
  dataEmissao: string;
  dataVencimento: string;
  valorPrincipal: number;
  valorDesconto?: number;
  valorJuros?: number;
  valorMulta?: number;
  valorTotal: number;
  codigoBarras?: string;
  linhaDigitavel?: string;
  status?: string;
}

export interface GuiaArrecadacaoResponse {
  id: number;
  numeroGuia: string;
  lancamentoTributarioId: number;
  lancamentoNumero?: string;
  contribuinteId: number;
  contribuinteNome: string;
  dataEmissao: string;
  dataVencimento: string;
  valorPrincipal: number;
  valorDesconto?: number;
  valorJuros?: number;
  valorMulta?: number;
  valorTotal: number;
  codigoBarras?: string;
  linhaDigitavel?: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

// ── Baixa de Arrecadação ────────────────────────────────
export interface BaixaArrecadacaoListItem {
  id: number;
  guiaNumero: string;
  contribuinteNome: string;
  dataPagamento: string;
  valorPago: number;
  tipoBaixa: string;
  agenteDescricao: string;
}

export interface BaixaArrecadacaoRequest {
  guiaArrecadacaoId: number;
  agenteArrecadadorId?: number;
  dataPagamento: string;
  dataCredito?: string;
  valorPago: number;
  valorJuros?: number;
  valorMulta?: number;
  valorDesconto?: number;
  tipoBaixa?: string;
  autenticacao?: string;
  receitaOrcamentariaId?: number;
  observacoes?: string;
}

export interface BaixaArrecadacaoResponse {
  id: number;
  guiaArrecadacaoId: number;
  guiaNumero: string;
  agenteArrecadadorId?: number;
  agenteDescricao: string;
  dataPagamento: string;
  dataCredito?: string;
  valorPago: number;
  valorJuros?: number;
  valorMulta?: number;
  valorDesconto?: number;
  tipoBaixa: string;
  autenticacao?: string;
  receitaOrcamentariaId?: number;
  observacoes?: string;
  createdAt: string;
}
