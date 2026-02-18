export interface Estado {
  id: number;
  sigla: string;
  nome: string;
}

export interface Municipio {
  id: number;
  nome: string;
  codigoIbge: string;
  estadoId: number;
  estadoSigla: string;
}

export interface TipoFornecedor {
  id: number;
  descricao: string;
}

export interface ClassificacaoFornecedor {
  id: number;
  descricao: string;
}

export interface CertidaoFiscal {
  id?: number;
  nome: string;
  numero?: string;
  dataEmissao?: string;
  dataValidade?: string;
  arquivoNome?: string;
  arquivoPath?: string;
  observacoes?: string;
}

export interface HistoricoLicitacao {
  id?: number;
  numeroProcesso: string;
  descricao?: string;
  dataParticipacao?: string;
  resultado?: string;
  valor?: number;
  observacoes?: string;
}

export interface FornecedorRequest {
  razaoSocial: string;
  nomeFantasia?: string;
  cnpjCpf: string;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  tipoFornecedorId: number;
  classificacaoId?: number;
  cep: string;
  logradouro: string;
  numero: string;
  complemento?: string;
  bairro: string;
  municipioId: number;
  estadoId: number;
  telefone?: string;
  celular?: string;
  email?: string;
  responsavelNome?: string;
  responsavelCpf?: string;
  responsavelRg?: string;
  responsavelCargo?: string;
  bancoNome?: string;
  bancoAgencia?: string;
  bancoConta?: string;
  ativo?: boolean;
  observacoes?: string;
  certidoes?: CertidaoFiscal[];
}

export interface FornecedorResponse {
  id: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpjCpf: string;
  inscricaoEstadual: string;
  inscricaoMunicipal: string;
  tipoFornecedor: TipoFornecedor;
  classificacao: ClassificacaoFornecedor;
  cep: string;
  logradouro: string;
  numero: string;
  complemento: string;
  bairro: string;
  municipio: Municipio;
  estado: Estado;
  telefone: string;
  celular: string;
  email: string;
  responsavelNome: string;
  responsavelCpf: string;
  responsavelRg: string;
  responsavelCargo: string;
  bancoNome: string;
  bancoAgencia: string;
  bancoConta: string;
  ativo: boolean;
  observacoes: string;
  createdAt: string;
  updatedAt: string;
  certidoes: CertidaoFiscal[];
  historicoLicitacoes: HistoricoLicitacao[];
}

export interface FornecedorListItem {
  id: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpjCpf: string;
  tipoFornecedor: string;
  classificacao: string;
  municipio: string;
  estadoSigla: string;
  telefone: string;
  email: string;
  ativo: boolean;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
