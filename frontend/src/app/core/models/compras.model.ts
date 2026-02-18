// ==========================================
// Módulo de Compras e Licitações - Models
// ==========================================

// --- Domínios ---

export interface ModalidadeLicitacao {
  id: number;
  nome: string;
  fundamentoLegal: string;
}

export interface TipoJulgamento {
  id: number;
  descricao: string;
}

export interface StatusProcesso {
  id: number;
  nome: string;
  descricao: string;
  cor: string;
  ordem: number;
}

// --- Processo de Compra ---

export interface ProcessoItemDTO {
  id?: number;
  numeroItem: number;
  descricao: string;
  unidade?: string;
  quantidade?: number;
  valorUnitarioEstimado?: number;
  valorTotalEstimado?: number;
}

export interface ProcessoCompraRequest {
  numeroProcesso: string;
  exercicio: number;
  objeto: string;
  justificativa?: string;
  modalidadeId: number;
  tipoJulgamentoId?: number;
  statusId: number;
  orgaoId: number;
  unidadeId?: number;
  dotacaoOrcamentariaId?: number;
  valorEstimado?: number;
  valorHomologado?: number;
  dataAbertura?: string;
  dataEncerramento?: string;
  dataHomologacao?: string;
  dataPublicacao?: string;
  numeroEdital?: string;
  anoEdital?: number;
  observacoes?: string;
  itens?: ProcessoItemDTO[];
}

export interface ProcessoCompraResponse {
  id: number;
  numeroProcesso: string;
  exercicio: number;
  objeto: string;
  justificativa: string;
  modalidadeId: number;
  modalidadeNome: string;
  tipoJulgamentoId: number;
  tipoJulgamentoDescricao: string;
  statusId: number;
  statusNome: string;
  statusCor: string;
  orgaoNome: string;
  unidadeNome: string;
  dotacaoOrcamentariaId: number;
  dotacaoCodigo: string;
  valorEstimado: number;
  valorHomologado: number;
  dataAbertura: string;
  dataEncerramento: string;
  dataHomologacao: string;
  dataPublicacao: string;
  numeroEdital: string;
  anoEdital: number;
  observacoes: string;
  createdAt: string;
  updatedAt: string;
  itens: ProcessoItemDTO[];
}

export interface ProcessoCompraListItem {
  id: number;
  numeroProcesso: string;
  exercicio: number;
  objeto: string;
  modalidadeNome: string;
  statusNome: string;
  statusCor: string;
  orgaoNome: string;
  valorEstimado: number;
  dataAbertura: string;
}

// --- Participantes, Documentos, Propostas, Histórico ---

export interface ProcessoParticipanteDTO {
  id: number;
  processoId: number;
  fornecedorId: number;
  fornecedorNome: string;
  fornecedorCnpjCpf: string;
  habilitado: boolean;
  dataHabilitacao: string;
  observacoes: string;
}

export interface ProcessoDocumentoDTO {
  id: number;
  processoId: number;
  tipoDocumento: string;
  descricao: string;
  arquivoNome: string;
  arquivoPath: string;
  dataUpload: string;
}

export interface ProcessoPropostaDTO {
  id: number;
  processoId: number;
  participanteId: number;
  fornecedorNome: string;
  itemId: number;
  valorUnitario: number;
  valorTotal: number;
  marca: string;
  classificacao: number;
  vencedor: boolean;
}

export interface ProcessoHistoricoDTO {
  id: number;
  processoId: number;
  statusAnterior: string;
  statusNovo: string;
  dataAlteracao: string;
  usuario: string;
  observacoes: string;
}

// --- Empenho ---

export interface EmpenhoRequest {
  numeroEmpenho: string;
  exercicio: number;
  processoCompraId?: number;
  fornecedorId: number;
  dotacaoOrcamentariaId?: number;
  tipoEmpenho: string;
  dataEmpenho: string;
  descricao: string;
  valor: number;
  valorAnulado?: number;
  valorLiquidado?: number;
  valorPago?: number;
  status?: string;
  observacoes?: string;
}

export interface EmpenhoResponse {
  id: number;
  numeroEmpenho: string;
  exercicio: number;
  processoCompraId: number;
  processoNumero: string;
  fornecedorId: number;
  fornecedorNome: string;
  fornecedorCnpjCpf: string;
  dotacaoOrcamentariaId: number;
  dotacaoCodigo: string;
  tipoEmpenho: string;
  dataEmpenho: string;
  descricao: string;
  valor: number;
  valorAnulado: number;
  valorLiquidado: number;
  valorPago: number;
  status: string;
  observacoes: string;
  createdAt: string;
  updatedAt: string;
}

export interface EmpenhoListItem {
  id: number;
  numeroEmpenho: string;
  exercicio: number;
  processoNumero: string;
  fornecedorNome: string;
  tipoEmpenho: string;
  dataEmpenho: string;
  valor: number;
  status: string;
}
