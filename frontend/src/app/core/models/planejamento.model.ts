// ==========================================
// Módulo de Planejamento e Orçamento - Models
// ==========================================

// --- Domínios ---

export interface Funcao {
  id: number;
  codigo: string;
  descricao: string;
}

export interface Subfuncao {
  id: number;
  codigo: string;
  descricao: string;
  funcaoId: number;
  funcaoDescricao: string;
}

export interface NaturezaDespesa {
  id: number;
  codigo: string;
  descricao: string;
}

export interface FonteRecurso {
  id: number;
  codigo: string;
  descricao: string;
}

// --- Órgão ---

export interface OrgaoRequest {
  codigo: string;
  nome: string;
  sigla?: string;
  ativo?: boolean;
}

export interface OrgaoResponse {
  id: number;
  codigo: string;
  nome: string;
  sigla: string;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

// --- Unidade ---

export interface UnidadeRequest {
  codigo: string;
  nome: string;
  sigla?: string;
  orgaoId: number;
  ativo?: boolean;
}

export interface UnidadeResponse {
  id: number;
  codigo: string;
  nome: string;
  sigla: string;
  orgaoId: number;
  orgaoNome: string;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

// --- Programa ---

export interface ProgramaRequest {
  codigo: string;
  nome: string;
  objetivo?: string;
  publicoAlvo?: string;
  indicador?: string;
  unidadeMedida?: string;
  metaFisica?: string;
  ativo?: boolean;
}

export interface ProgramaResponse {
  id: number;
  codigo: string;
  nome: string;
  objetivo: string;
  publicoAlvo: string;
  indicador: string;
  unidadeMedida: string;
  metaFisica: string;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ProgramaListItem {
  id: number;
  codigo: string;
  nome: string;
  objetivo: string;
  ativo: boolean;
}

// --- Ação ---

export interface AcaoRequest {
  codigo: string;
  nome: string;
  tipo: string;
  descricao?: string;
  funcaoId: number;
  subfuncaoId?: number;
  programaId: number;
  ativo?: boolean;
}

export interface AcaoResponse {
  id: number;
  codigo: string;
  nome: string;
  tipo: string;
  descricao: string;
  funcaoId: number;
  funcaoDescricao: string;
  subfuncaoId: number;
  subfuncaoDescricao: string;
  programaId: number;
  programaNome: string;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface AcaoListItem {
  id: number;
  codigo: string;
  nome: string;
  tipo: string;
  funcaoDescricao: string;
  subfuncaoDescricao: string;
  programaNome: string;
  ativo: boolean;
}

// --- PPA ---

export interface PpaProgramaMeta {
  id?: number;
  descricao: string;
  unidadeMedida?: string;
  metaFisica?: number;
  metaFinanceira?: number;
  exercicio?: number;
}

export interface PpaProgramaDTO {
  id?: number;
  programaId: number;
  objetivo?: string;
  metas?: PpaProgramaMeta[];
}

export interface PpaRequest {
  exercicioInicio: number;
  exercicioFim: number;
  descricao?: string;
  status?: string;
  programas?: PpaProgramaDTO[];
}

export interface PpaProgramaResponse {
  id: number;
  programaId: number;
  programaNome: string;
  programaCodigo: string;
  objetivo: string;
  metas: PpaProgramaMeta[];
}

export interface PpaResponse {
  id: number;
  exercicioInicio: number;
  exercicioFim: number;
  descricao: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  programas: PpaProgramaResponse[];
}

export interface PpaListItem {
  id: number;
  exercicioInicio: number;
  exercicioFim: number;
  descricao: string;
  status: string;
}

// --- LDO ---

export interface LdoPrioridadeDTO {
  id?: number;
  programaId: number;
  acaoId?: number;
  descricao: string;
  metaFisica?: number;
  metaFinanceira?: number;
}

export interface LdoRequest {
  exercicio: number;
  descricao?: string;
  metaFiscal?: string;
  risco?: string;
  status?: string;
  ppaId?: number;
  prioridades?: LdoPrioridadeDTO[];
}

export interface LdoResponse {
  id: number;
  exercicio: number;
  descricao: string;
  metaFiscal: string;
  risco: string;
  status: string;
  ppaId: number;
  ppaDescricao: string;
  createdAt: string;
  updatedAt: string;
  prioridades: LdoPrioridadeResponse[];
}

export interface LdoPrioridadeResponse {
  id: number;
  programaId: number;
  programaNome: string;
  acaoId: number;
  acaoNome: string;
  descricao: string;
  metaFisica: number;
  metaFinanceira: number;
}

export interface LdoListItem {
  id: number;
  exercicio: number;
  descricao: string;
  status: string;
  ppaDescricao: string;
}

// --- LOA ---

export interface LoaRequest {
  exercicio: number;
  descricao?: string;
  valorTotal?: number;
  status?: string;
  ldoId?: number;
}

export interface LoaResponse {
  id: number;
  exercicio: number;
  descricao: string;
  valorTotal: number;
  status: string;
  ldoId: number;
  ldoDescricao: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoaListItem {
  id: number;
  exercicio: number;
  descricao: string;
  valorTotal: number;
  status: string;
}

// --- Dotação Orçamentária ---

export interface DotacaoOrcamentariaRequest {
  exercicio: number;
  codigoDotacao: string;
  orgaoId: number;
  unidadeId?: number;
  funcaoId: number;
  subfuncaoId?: number;
  programaId: number;
  acaoId?: number;
  naturezaDespesaId: number;
  fonteRecursoId: number;
  valorInicial: number;
  valorSuplementado?: number;
  valorAnulado?: number;
  valorEmpenhado?: number;
  loaId?: number;
}

export interface DotacaoOrcamentariaResponse {
  id: number;
  exercicio: number;
  codigoDotacao: string;
  orgaoNome: string;
  unidadeNome: string;
  funcaoDescricao: string;
  subfuncaoDescricao: string;
  programaNome: string;
  acaoNome: string;
  naturezaDespesaDescricao: string;
  fonteRecursoDescricao: string;
  valorInicial: number;
  valorSuplementado: number;
  valorAnulado: number;
  valorEmpenhado: number;
  saldoDisponivel: number;
  loaId: number;
  createdAt: string;
  updatedAt: string;
}

export interface DotacaoOrcamentariaListItem {
  id: number;
  exercicio: number;
  codigoDotacao: string;
  orgaoNome: string;
  programaNome: string;
  naturezaDespesaDescricao: string;
  fonteRecursoDescricao: string;
  valorInicial: number;
  saldoDisponivel: number;
}

// --- Receita Prevista ---

export interface ReceitaPrevistaRequest {
  exercicio: number;
  codigoReceita: string;
  descricao: string;
  categoria?: string;
  fonteRecursoId?: number;
  valorPrevisto: number;
  valorArrecadado?: number;
  loaId?: number;
}

export interface ReceitaPrevistaResponse {
  id: number;
  exercicio: number;
  codigoReceita: string;
  descricao: string;
  categoria: string;
  fonteRecursoId: number;
  fonteRecursoDescricao: string;
  valorPrevisto: number;
  valorArrecadado: number;
  loaId: number;
  createdAt: string;
  updatedAt: string;
}

export interface ReceitaPrevistaListItem {
  id: number;
  exercicio: number;
  codigoReceita: string;
  descricao: string;
  categoria: string;
  fonteRecursoDescricao: string;
  valorPrevisto: number;
  valorArrecadado: number;
}
