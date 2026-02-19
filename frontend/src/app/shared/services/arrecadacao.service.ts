import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
    BaixaArrecadacaoListItem, BaixaArrecadacaoRequest, BaixaArrecadacaoResponse,
    ContribuinteListItem, ContribuinteRequest, ContribuinteResponse,
    GuiaArrecadacaoListItem, GuiaArrecadacaoRequest, GuiaArrecadacaoResponse,
    LancamentoTributarioListItem, LancamentoTributarioRequest, LancamentoTributarioResponse,
    Tributo,
} from '../../core/models/arrecadacao.model';
import { PageResponse } from '../../core/models/fornecedor.model';

@Injectable({ providedIn: 'root' })
export class ArrecadacaoService {
  private readonly apiUrl = `${environment.apiUrl}/arrecadacao`;

  constructor(private readonly http: HttpClient) {}

  // ========================
  // Contribuintes
  // ========================
  findAllContribuintes(filters: Record<string, any>, page: number, size: number, sort = 'nomeRazaoSocial,asc'): Observable<PageResponse<ContribuinteListItem>> {
    return this.http.get<PageResponse<ContribuinteListItem>>(`${this.apiUrl}/contribuintes`, { params: this.buildParams(filters, page, size, sort) });
  }
  findContribuinteById(id: number): Observable<ContribuinteResponse> {
    return this.http.get<ContribuinteResponse>(`${this.apiUrl}/contribuintes/${id}`);
  }
  createContribuinte(data: ContribuinteRequest): Observable<ContribuinteResponse> {
    return this.http.post<ContribuinteResponse>(`${this.apiUrl}/contribuintes`, data);
  }
  updateContribuinte(id: number, data: ContribuinteRequest): Observable<ContribuinteResponse> {
    return this.http.put<ContribuinteResponse>(`${this.apiUrl}/contribuintes/${id}`, data);
  }
  deleteContribuinte(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/contribuintes/${id}`);
  }

  // ========================
  // Lançamentos Tributários
  // ========================
  findAllLancamentos(filters: Record<string, any>, page: number, size: number, sort = 'dataLancamento,desc'): Observable<PageResponse<LancamentoTributarioListItem>> {
    return this.http.get<PageResponse<LancamentoTributarioListItem>>(`${this.apiUrl}/lancamentos-tributarios`, { params: this.buildParams(filters, page, size, sort) });
  }
  findLancamentoById(id: number): Observable<LancamentoTributarioResponse> {
    return this.http.get<LancamentoTributarioResponse>(`${this.apiUrl}/lancamentos-tributarios/${id}`);
  }
  createLancamento(data: LancamentoTributarioRequest): Observable<LancamentoTributarioResponse> {
    return this.http.post<LancamentoTributarioResponse>(`${this.apiUrl}/lancamentos-tributarios`, data);
  }
  updateLancamento(id: number, data: LancamentoTributarioRequest): Observable<LancamentoTributarioResponse> {
    return this.http.put<LancamentoTributarioResponse>(`${this.apiUrl}/lancamentos-tributarios/${id}`, data);
  }
  deleteLancamento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/lancamentos-tributarios/${id}`);
  }

  // ========================
  // Guias de Arrecadação
  // ========================
  findAllGuias(filters: Record<string, any>, page: number, size: number, sort = 'dataEmissao,desc'): Observable<PageResponse<GuiaArrecadacaoListItem>> {
    return this.http.get<PageResponse<GuiaArrecadacaoListItem>>(`${this.apiUrl}/guias`, { params: this.buildParams(filters, page, size, sort) });
  }
  findGuiaById(id: number): Observable<GuiaArrecadacaoResponse> {
    return this.http.get<GuiaArrecadacaoResponse>(`${this.apiUrl}/guias/${id}`);
  }
  createGuia(data: GuiaArrecadacaoRequest): Observable<GuiaArrecadacaoResponse> {
    return this.http.post<GuiaArrecadacaoResponse>(`${this.apiUrl}/guias`, data);
  }
  updateGuia(id: number, data: GuiaArrecadacaoRequest): Observable<GuiaArrecadacaoResponse> {
    return this.http.put<GuiaArrecadacaoResponse>(`${this.apiUrl}/guias/${id}`, data);
  }
  deleteGuia(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/guias/${id}`);
  }

  // ========================
  // Baixas de Arrecadação
  // ========================
  findAllBaixas(filters: Record<string, any>, page: number, size: number, sort = 'dataPagamento,desc'): Observable<PageResponse<BaixaArrecadacaoListItem>> {
    return this.http.get<PageResponse<BaixaArrecadacaoListItem>>(`${this.apiUrl}/baixas`, { params: this.buildParams(filters, page, size, sort) });
  }
  findBaixaById(id: number): Observable<BaixaArrecadacaoResponse> {
    return this.http.get<BaixaArrecadacaoResponse>(`${this.apiUrl}/baixas/${id}`);
  }
  createBaixa(data: BaixaArrecadacaoRequest): Observable<BaixaArrecadacaoResponse> {
    return this.http.post<BaixaArrecadacaoResponse>(`${this.apiUrl}/baixas`, data);
  }
  updateBaixa(id: number, data: BaixaArrecadacaoRequest): Observable<BaixaArrecadacaoResponse> {
    return this.http.put<BaixaArrecadacaoResponse>(`${this.apiUrl}/baixas/${id}`, data);
  }
  deleteBaixa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/baixas/${id}`);
  }

  // ========================
  // Domínios
  // ========================
  findTributos(): Observable<Tributo[]> {
    return this.http.get<Tributo[]>(`${environment.apiUrl}/arrecadacao/dominios/tributos`);
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
