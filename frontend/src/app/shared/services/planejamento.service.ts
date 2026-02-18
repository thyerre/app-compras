import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../../core/models/fornecedor.model';
import {
    AcaoListItem,
    AcaoRequest,
    AcaoResponse,
    DotacaoOrcamentariaListItem,
    DotacaoOrcamentariaRequest,
    DotacaoOrcamentariaResponse,
    FonteRecurso,
    Funcao,
    LdoListItem,
    LdoRequest,
    LdoResponse,
    LoaListItem,
    LoaRequest,
    LoaResponse,
    NaturezaDespesa,
    OrgaoRequest,
    OrgaoResponse,
    PpaListItem,
    PpaRequest,
    PpaResponse,
    ProgramaListItem,
    ProgramaRequest,
    ProgramaResponse,
    ReceitaPrevistaListItem,
    ReceitaPrevistaRequest,
    ReceitaPrevistaResponse,
    Subfuncao,
    UnidadeRequest,
    UnidadeResponse,
} from '../../core/models/planejamento.model';

@Injectable({ providedIn: 'root' })
export class PlanejamentoService {
  private readonly apiUrl = `${environment.apiUrl}/planejamento`;
  private readonly dominioUrl = `${this.apiUrl}/dominios`;

  constructor(private readonly http: HttpClient) {}

  // ========================
  // Domínios
  // ========================

  getFuncoes(): Observable<Funcao[]> {
    return this.http.get<Funcao[]>(`${this.dominioUrl}/funcoes`);
  }

  getSubfuncoes(funcaoId?: number): Observable<Subfuncao[]> {
    let params = new HttpParams();
    if (funcaoId) {
      params = params.set('funcaoId', funcaoId.toString());
    }
    return this.http.get<Subfuncao[]>(`${this.dominioUrl}/subfuncoes`, { params });
  }

  getNaturezasDespesa(): Observable<NaturezaDespesa[]> {
    return this.http.get<NaturezaDespesa[]>(`${this.dominioUrl}/naturezas-despesa`);
  }

  getFontesRecurso(): Observable<FonteRecurso[]> {
    return this.http.get<FonteRecurso[]>(`${this.dominioUrl}/fontes-recurso`);
  }

  getOrgaosSimples(): Observable<OrgaoResponse[]> {
    return this.http.get<OrgaoResponse[]>(`${this.dominioUrl}/orgaos`);
  }

  getUnidadesSimples(orgaoId?: number): Observable<UnidadeResponse[]> {
    let params = new HttpParams();
    if (orgaoId) {
      params = params.set('orgaoId', orgaoId.toString());
    }
    return this.http.get<UnidadeResponse[]>(`${this.dominioUrl}/unidades`, { params });
  }

  getProgramasSimples(): Observable<ProgramaListItem[]> {
    return this.http.get<ProgramaListItem[]>(`${this.dominioUrl}/programas`);
  }

  getLoasSimples(): Observable<{ id: number; exercicio: number; descricao: string; status: string }[]> {
    return this.http.get<{ id: number; exercicio: number; descricao: string; status: string }[]>(`${this.dominioUrl}/loas`);
  }

  // ========================
  // Órgãos
  // ========================

  findAllOrgaos(filters: Record<string, any>, page = 0, size = 20, sort = 'nome,asc'): Observable<PageResponse<OrgaoResponse>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<OrgaoResponse>>(`${this.apiUrl}/orgaos`, { params });
  }

  findOrgaoById(id: number): Observable<OrgaoResponse> {
    return this.http.get<OrgaoResponse>(`${this.apiUrl}/orgaos/${id}`);
  }

  createOrgao(data: OrgaoRequest): Observable<OrgaoResponse> {
    return this.http.post<OrgaoResponse>(`${this.apiUrl}/orgaos`, data);
  }

  updateOrgao(id: number, data: OrgaoRequest): Observable<OrgaoResponse> {
    return this.http.put<OrgaoResponse>(`${this.apiUrl}/orgaos/${id}`, data);
  }

  deleteOrgao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/orgaos/${id}`);
  }

  // ========================
  // Unidades
  // ========================

  findAllUnidades(filters: Record<string, any>, page = 0, size = 20, sort = 'nome,asc'): Observable<PageResponse<UnidadeResponse>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<UnidadeResponse>>(`${this.apiUrl}/unidades`, { params });
  }

  findUnidadeById(id: number): Observable<UnidadeResponse> {
    return this.http.get<UnidadeResponse>(`${this.apiUrl}/unidades/${id}`);
  }

  createUnidade(data: UnidadeRequest): Observable<UnidadeResponse> {
    return this.http.post<UnidadeResponse>(`${this.apiUrl}/unidades`, data);
  }

  updateUnidade(id: number, data: UnidadeRequest): Observable<UnidadeResponse> {
    return this.http.put<UnidadeResponse>(`${this.apiUrl}/unidades/${id}`, data);
  }

  deleteUnidade(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/unidades/${id}`);
  }

  // ========================
  // Programas
  // ========================

  findAllProgramas(filters: Record<string, any>, page = 0, size = 20, sort = 'codigo,asc'): Observable<PageResponse<ProgramaListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<ProgramaListItem>>(`${this.apiUrl}/programas`, { params });
  }

  findProgramaById(id: number): Observable<ProgramaResponse> {
    return this.http.get<ProgramaResponse>(`${this.apiUrl}/programas/${id}`);
  }

  createPrograma(data: ProgramaRequest): Observable<ProgramaResponse> {
    return this.http.post<ProgramaResponse>(`${this.apiUrl}/programas`, data);
  }

  updatePrograma(id: number, data: ProgramaRequest): Observable<ProgramaResponse> {
    return this.http.put<ProgramaResponse>(`${this.apiUrl}/programas/${id}`, data);
  }

  deletePrograma(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/programas/${id}`);
  }

  // ========================
  // Ações
  // ========================

  findAllAcoes(filters: Record<string, any>, page = 0, size = 20, sort = 'codigo,asc'): Observable<PageResponse<AcaoListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<AcaoListItem>>(`${this.apiUrl}/acoes`, { params });
  }

  findAcaoById(id: number): Observable<AcaoResponse> {
    return this.http.get<AcaoResponse>(`${this.apiUrl}/acoes/${id}`);
  }

  createAcao(data: AcaoRequest): Observable<AcaoResponse> {
    return this.http.post<AcaoResponse>(`${this.apiUrl}/acoes`, data);
  }

  updateAcao(id: number, data: AcaoRequest): Observable<AcaoResponse> {
    return this.http.put<AcaoResponse>(`${this.apiUrl}/acoes/${id}`, data);
  }

  deleteAcao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/acoes/${id}`);
  }

  // ========================
  // PPA
  // ========================

  findAllPpas(filters: Record<string, any>, page = 0, size = 20, sort = 'anoInicio,desc'): Observable<PageResponse<PpaListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<PpaListItem>>(`${this.apiUrl}/ppas`, { params });
  }

  findPpaById(id: number): Observable<PpaResponse> {
    return this.http.get<PpaResponse>(`${this.apiUrl}/ppas/${id}`);
  }

  createPpa(data: PpaRequest): Observable<PpaResponse> {
    return this.http.post<PpaResponse>(`${this.apiUrl}/ppas`, data);
  }

  updatePpa(id: number, data: PpaRequest): Observable<PpaResponse> {
    return this.http.put<PpaResponse>(`${this.apiUrl}/ppas/${id}`, data);
  }

  deletePpa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ppas/${id}`);
  }

  // ========================
  // LDO
  // ========================

  findAllLdos(filters: Record<string, any>, page = 0, size = 20, sort = 'exercicio,desc'): Observable<PageResponse<LdoListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<LdoListItem>>(`${this.apiUrl}/ldos`, { params });
  }

  findLdoById(id: number): Observable<LdoResponse> {
    return this.http.get<LdoResponse>(`${this.apiUrl}/ldos/${id}`);
  }

  createLdo(data: LdoRequest): Observable<LdoResponse> {
    return this.http.post<LdoResponse>(`${this.apiUrl}/ldos`, data);
  }

  updateLdo(id: number, data: LdoRequest): Observable<LdoResponse> {
    return this.http.put<LdoResponse>(`${this.apiUrl}/ldos/${id}`, data);
  }

  deleteLdo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ldos/${id}`);
  }

  // ========================
  // LOA
  // ========================

  findAllLoas(filters: Record<string, any>, page = 0, size = 20, sort = 'exercicio,desc'): Observable<PageResponse<LoaListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<LoaListItem>>(`${this.apiUrl}/loas`, { params });
  }

  findLoaById(id: number): Observable<LoaResponse> {
    return this.http.get<LoaResponse>(`${this.apiUrl}/loas/${id}`);
  }

  createLoa(data: LoaRequest): Observable<LoaResponse> {
    return this.http.post<LoaResponse>(`${this.apiUrl}/loas`, data);
  }

  updateLoa(id: number, data: LoaRequest): Observable<LoaResponse> {
    return this.http.put<LoaResponse>(`${this.apiUrl}/loas/${id}`, data);
  }

  deleteLoa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/loas/${id}`);
  }

  // ========================
  // Dotações Orçamentárias
  // ========================

  findAllDotacoes(filters: Record<string, any>, page = 0, size = 20, sort = 'id,desc'): Observable<PageResponse<DotacaoOrcamentariaListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<DotacaoOrcamentariaListItem>>(`${this.apiUrl}/dotacoes`, { params });
  }

  findDotacaoById(id: number): Observable<DotacaoOrcamentariaResponse> {
    return this.http.get<DotacaoOrcamentariaResponse>(`${this.apiUrl}/dotacoes/${id}`);
  }

  createDotacao(data: DotacaoOrcamentariaRequest): Observable<DotacaoOrcamentariaResponse> {
    return this.http.post<DotacaoOrcamentariaResponse>(`${this.apiUrl}/dotacoes`, data);
  }

  updateDotacao(id: number, data: DotacaoOrcamentariaRequest): Observable<DotacaoOrcamentariaResponse> {
    return this.http.put<DotacaoOrcamentariaResponse>(`${this.apiUrl}/dotacoes/${id}`, data);
  }

  deleteDotacao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/dotacoes/${id}`);
  }

  // ========================
  // Receitas Previstas
  // ========================

  findAllReceitas(filters: Record<string, any>, page = 0, size = 20, sort = 'codigo,asc'): Observable<PageResponse<ReceitaPrevistaListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<ReceitaPrevistaListItem>>(`${this.apiUrl}/receitas`, { params });
  }

  findReceitaById(id: number): Observable<ReceitaPrevistaResponse> {
    return this.http.get<ReceitaPrevistaResponse>(`${this.apiUrl}/receitas/${id}`);
  }

  createReceita(data: ReceitaPrevistaRequest): Observable<ReceitaPrevistaResponse> {
    return this.http.post<ReceitaPrevistaResponse>(`${this.apiUrl}/receitas`, data);
  }

  updateReceita(id: number, data: ReceitaPrevistaRequest): Observable<ReceitaPrevistaResponse> {
    return this.http.put<ReceitaPrevistaResponse>(`${this.apiUrl}/receitas/${id}`, data);
  }

  deleteReceita(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/receitas/${id}`);
  }

  // ========================
  // Helpers
  // ========================

  private buildParams(filters: Record<string, any>, page: number, size: number, sort: string): HttpParams {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, String(value));
      }
    });

    return params;
  }
}
