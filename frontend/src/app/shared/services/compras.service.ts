import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
    EmpenhoListItem,
    EmpenhoRequest,
    EmpenhoResponse,
    ModalidadeLicitacao,
    ProcessoCompraListItem,
    ProcessoCompraRequest,
    ProcessoCompraResponse,
    StatusProcesso,
    TipoJulgamento
} from '../../core/models/compras.model';
import { PageResponse } from '../../core/models/fornecedor.model';

@Injectable({ providedIn: 'root' })
export class ComprasService {
  private readonly apiUrl = `${environment.apiUrl}/compras`;
  private readonly dominioUrl = `${this.apiUrl}/dominios`;

  constructor(private readonly http: HttpClient) {}

  // ========================
  // Domínios
  // ========================

  getModalidades(): Observable<ModalidadeLicitacao[]> {
    return this.http.get<ModalidadeLicitacao[]>(`${this.dominioUrl}/modalidades`);
  }

  getTiposJulgamento(): Observable<TipoJulgamento[]> {
    return this.http.get<TipoJulgamento[]>(`${this.dominioUrl}/tipos-julgamento`);
  }

  getStatusProcesso(): Observable<StatusProcesso[]> {
    return this.http.get<StatusProcesso[]>(`${this.dominioUrl}/status-processo`);
  }

  // ========================
  // Processos de Compra
  // ========================

  findAllProcessos(filters: Record<string, any>, page = 0, size = 20, sort = 'numeroProcesso,desc'): Observable<PageResponse<ProcessoCompraListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<ProcessoCompraListItem>>(`${this.apiUrl}/processos`, { params });
  }

  findProcessoById(id: number): Observable<ProcessoCompraResponse> {
    return this.http.get<ProcessoCompraResponse>(`${this.apiUrl}/processos/${id}`);
  }

  createProcesso(data: ProcessoCompraRequest): Observable<ProcessoCompraResponse> {
    return this.http.post<ProcessoCompraResponse>(`${this.apiUrl}/processos`, data);
  }

  updateProcesso(id: number, data: ProcessoCompraRequest): Observable<ProcessoCompraResponse> {
    return this.http.put<ProcessoCompraResponse>(`${this.apiUrl}/processos/${id}`, data);
  }

  deleteProcesso(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/processos/${id}`);
  }

  // ========================
  // Empenhos
  // ========================

  findAllEmpenhos(filters: Record<string, any>, page = 0, size = 20, sort = 'dataEmpenho,desc'): Observable<PageResponse<EmpenhoListItem>> {
    let params = this.buildParams(filters, page, size, sort);
    return this.http.get<PageResponse<EmpenhoListItem>>(`${this.apiUrl}/empenhos`, { params });
  }

  findEmpenhoById(id: number): Observable<EmpenhoResponse> {
    return this.http.get<EmpenhoResponse>(`${this.apiUrl}/empenhos/${id}`);
  }

  createEmpenho(data: EmpenhoRequest): Observable<EmpenhoResponse> {
    return this.http.post<EmpenhoResponse>(`${this.apiUrl}/empenhos`, data);
  }

  updateEmpenho(id: number, data: EmpenhoRequest): Observable<EmpenhoResponse> {
    return this.http.put<EmpenhoResponse>(`${this.apiUrl}/empenhos/${id}`, data);
  }

  deleteEmpenho(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/empenhos/${id}`);
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
