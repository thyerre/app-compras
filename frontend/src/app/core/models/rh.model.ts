// ── Cargo ────────────────────────────────────────────────
export interface CargoListItem {
  id: number;
  codigo: string;
  descricao: string;
  tipo: string;
  vagas: number;
  vagasOcupadas: number;
  cargaHorariaSemanal: number;
  ativo: boolean;
}

export interface CargoRequest {
  codigo: string;
  descricao: string;
  tipo: string;
  escolaridadeMinima?: string;
  cbo?: string;
  vagas?: number;
  vagasOcupadas?: number;
  cargaHorariaSemanal?: number;
  ativo?: boolean;
}

export interface CargoResponse {
  id: number;
  codigo: string;
  descricao: string;
  tipo: string;
  cbo?: string;
  vagas: number;
  vagasOcupadas: number;
  escolaridadeMinima?: string;
  cargaHorariaSemanal?: number;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

// ── Tipo de Vínculo ─────────────────────────────────────
export interface TipoVinculo {
  id: number;
  codigo: string;
  descricao: string;
  regime: string;
  ativo: boolean;
}

// ── Servidor ────────────────────────────────────────────
export interface ServidorListItem {
  id: number;
  matricula: string;
  nome: string;
  cpf: string;
  cargoDescricao: string;
  tipoVinculoDescricao: string;
  dataAdmissao: string;
  situacao: string;
}

export interface ServidorRequest {
  matricula: string;
  nome: string;
  cpf: string;
  rg?: string;
  rgOrgaoEmissor?: string;
  dataNascimento?: string;
  sexo?: string;
  estadoCivil?: string;
  naturalidade?: string;
  nacionalidade?: string;
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
  pisPasep?: string;
  ctpsNumero?: string;
  ctpsSerie?: string;
  cnhNumero?: string;
  cnhCategoria?: string;
  tituloEleitor?: string;
  zonaEleitoral?: string;
  secaoEleitoral?: string;
  grauInstrucao?: string;
  cargoId: number;
  nivelSalarialId?: number;
  tipoVinculoId: number;
  orgaoId?: number;
  unidadeId?: number;
  dataAdmissao: string;
  dataPosse?: string;
  dataExercicio?: string;
  dataDemissao?: string;
  motivoDemissao?: string;
  bancoCodigo?: string;
  bancoNome?: string;
  agencia?: string;
  conta?: string;
  tipoConta?: string;
  regimePrevidencia?: string;
  numeroPrevidencia?: string;
  situacao?: string;
  ativo?: boolean;
  observacoes?: string;
  senhaUsuario?: string;
}

export interface ServidorResponse extends ServidorRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}

// ── Folha de Pagamento ──────────────────────────────────
export interface FolhaPagamentoListItem {
  id: number;
  competencia: string;
  tipo: string;
  totalProventos: number;
  totalDescontos: number;
  totalLiquido: number;
  quantidadeServidores: number;
  status: string;
}

export interface FolhaPagamentoRequest {
  competencia: string;
  tipo: string;
  dataPagamento?: string;
  status?: string;
  observacoes?: string;
}

export interface FolhaPagamentoResponse {
  id: number;
  competencia: string;
  tipo: string;
  dataPagamento?: string;
  totalProventos: number;
  totalDescontos: number;
  totalLiquido: number;
  quantidadeServidores: number;
  status: string;
  observacoes?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Nível Salarial ──────────────────────────────────────
export interface NivelSalarialListItem {
  id: number;
  cargoDescricao: string;
  nivel: string;
  classe?: string;
  referencia?: string;
  valorBase: number;
  vigenciaInicio: string;
  vigenciaFim?: string;
  ativo: boolean;
}

export interface NivelSalarialRequest {
  cargoId: number;
  nivel: string;
  classe?: string;
  referencia?: string;
  valorBase: number;
  vigenciaInicio: string;
  vigenciaFim?: string;
  ativo?: boolean;
}

export interface NivelSalarialResponse {
  id: number;
  cargoId: number;
  cargoDescricao: string;
  nivel: string;
  classe?: string;
  referencia?: string;
  valorBase: number;
  vigenciaInicio: string;
  vigenciaFim?: string;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

// ── Dependente ──────────────────────────────────────────
export interface DependenteListItem {
  id: number;
  servidorNome: string;
  servidorMatricula: string;
  nome: string;
  cpf?: string;
  dataNascimento: string;
  parentesco: string;
  ativo: boolean;
}

export interface DependenteRequest {
  servidorId: number;
  nome: string;
  cpf?: string;
  dataNascimento: string;
  parentesco: string;
  sexo?: string;
  dependenteIr?: boolean;
  dependenteSalarioFamilia?: boolean;
  dependentePlanoSaude?: boolean;
  pensaoAlimenticia?: boolean;
  percentualPensao?: number;
  dataInicioDependencia?: string;
  dataFimDependencia?: string;
  ativo?: boolean;
}

export interface DependenteResponse {
  id: number;
  servidorId: number;
  servidorNome: string;
  nome: string;
  cpf?: string;
  dataNascimento: string;
  parentesco: string;
  sexo?: string;
  dependenteIr: boolean;
  dependenteSalarioFamilia: boolean;
  dependentePlanoSaude: boolean;
  pensaoAlimenticia: boolean;
  percentualPensao?: number;
  dataInicioDependencia?: string;
  dataFimDependencia?: string;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

// ── Evento de Folha ─────────────────────────────────────
export interface EventoFolhaListItem {
  id: number;
  codigo: string;
  descricao: string;
  tipo: string;
  percentual?: number;
  valorFixo?: number;
  ativo: boolean;
}

export interface EventoFolhaRequest {
  codigo: string;
  descricao: string;
  tipo: string;
  incidenciaInss?: boolean;
  incidenciaIrrf?: boolean;
  incidenciaFgts?: boolean;
  automatico?: boolean;
  formula?: string;
  percentual?: number;
  valorFixo?: number;
  tipoCalculo?: string;
  aplicaMensal?: boolean;
  aplicaFerias?: boolean;
  aplica13?: boolean;
  aplicaRescisao?: boolean;
  ativo?: boolean;
}

export interface EventoFolhaResponse {
  id: number;
  codigo: string;
  descricao: string;
  tipo: string;
  incidenciaInss: boolean;
  incidenciaIrrf: boolean;
  incidenciaFgts: boolean;
  automatico: boolean;
  formula?: string;
  percentual?: number;
  valorFixo?: number;
  tipoCalculo?: string;
  aplicaMensal: boolean;
  aplicaFerias: boolean;
  aplica13: boolean;
  aplicaRescisao: boolean;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

// ── Férias ──────────────────────────────────────────────
export interface FeriasListItem {
  id: number;
  servidorNome: string;
  servidorMatricula: string;
  periodoAquisitivoInicio: string;
  periodoAquisitivoFim: string;
  periodoGozoInicio?: string;
  periodoGozoFim?: string;
  diasGozo?: number;
  status: string;
}

export interface FeriasRequest {
  servidorId: number;
  periodoAquisitivoInicio: string;
  periodoAquisitivoFim: string;
  periodoGozoInicio?: string;
  periodoGozoFim?: string;
  diasGozo?: number;
  diasAbono?: number;
  parcelas?: number;
  valorFerias?: number;
  valorAbono?: number;
  valor13Ferias?: number;
  status?: string;
  observacoes?: string;
}

export interface FeriasResponse {
  id: number;
  servidorId: number;
  servidorNome: string;
  periodoAquisitivoInicio: string;
  periodoAquisitivoFim: string;
  periodoGozoInicio?: string;
  periodoGozoFim?: string;
  diasGozo?: number;
  diasAbono?: number;
  parcelas?: number;
  valorFerias?: number;
  valorAbono?: number;
  valor13Ferias?: number;
  status: string;
  observacoes?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Licença ─────────────────────────────────────────────
export interface LicencaListItem {
  id: number;
  servidorNome: string;
  servidorMatricula: string;
  tipoLicenca: string;
  dataInicio: string;
  dataFim?: string;
  quantidadeDias?: number;
  remunerada: boolean;
}

export interface LicencaRequest {
  servidorId: number;
  tipoLicenca: string;
  dataInicio: string;
  dataFim?: string;
  quantidadeDias?: number;
  remunerada?: boolean;
  cid?: string;
  medicoNome?: string;
  medicoCrm?: string;
  numeroDocumento?: string;
  observacoes?: string;
}

export interface LicencaResponse {
  id: number;
  servidorId: number;
  servidorNome: string;
  tipoLicenca: string;
  dataInicio: string;
  dataFim?: string;
  quantidadeDias?: number;
  remunerada: boolean;
  cid?: string;
  medicoNome?: string;
  medicoCrm?: string;
  numeroDocumento?: string;
  observacoes?: string;
  createdAt: string;
  updatedAt: string;
}

// ── Histórico Funcional ─────────────────────────────────
export interface HistoricoFuncionalListItem {
  id: number;
  servidorNome: string;
  servidorMatricula: string;
  tipoEvento: string;
  dataEvento: string;
  descricao: string;
}

export interface HistoricoFuncionalRequest {
  servidorId: number;
  tipoEvento: string;
  dataEvento: string;
  numeroAto?: string;
  descricao: string;
  cargoAnteriorId?: number;
  cargoNovoId?: number;
  salarioAnterior?: number;
  salarioNovo?: number;
  orgaoAnteriorId?: number;
  orgaoNovoId?: number;
  unidadeAnteriorId?: number;
  unidadeNovoId?: number;
  observacoes?: string;
}

export interface HistoricoFuncionalResponse {
  id: number;
  servidorId: number;
  servidorNome: string;
  tipoEvento: string;
  dataEvento: string;
  numeroAto?: string;
  descricao: string;
  cargoAnteriorId?: number;
  cargoAnteriorDescricao?: string;
  cargoNovoId?: number;
  cargoNovoDescricao?: string;
  salarioAnterior?: number;
  salarioNovo?: number;
  orgaoAnteriorId?: number;
  orgaoAnteriorNome?: string;
  orgaoNovoId?: number;
  orgaoNovoNome?: string;
  unidadeAnteriorId?: number;
  unidadeAnteriorNome?: string;
  unidadeNovoId?: number;
  unidadeNovoNome?: string;
  observacoes?: string;
  createdAt: string;
}

// ── Consignado ──────────────────────────────────────────
export interface ConsignadoListItem {
  id: number;
  servidorNome: string;
  servidorMatricula: string;
  consignataria: string;
  contrato?: string;
  parcelaAtual: number;
  parcelaTotal: number;
  valorParcela: number;
  status: string;
}

export interface ConsignadoRequest {
  servidorId: number;
  eventoFolhaId?: number;
  consignataria: string;
  contrato?: string;
  parcelaAtual?: number;
  parcelaTotal: number;
  valorParcela: number;
  valorTotal: number;
  dataInicio: string;
  dataFim?: string;
  status?: string;
  observacoes?: string;
}

export interface ConsignadoResponse {
  id: number;
  servidorId: number;
  servidorNome: string;
  eventoFolhaId?: number;
  eventoFolhaDescricao?: string;
  consignataria: string;
  contrato?: string;
  parcelaAtual: number;
  parcelaTotal: number;
  valorParcela: number;
  valorTotal: number;
  dataInicio: string;
  dataFim?: string;
  status: string;
  observacoes?: string;
  createdAt: string;
  updatedAt: string;
}

