import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
    ClassificacaoFornecedor,
    Estado,
    FornecedorListItem,
    FornecedorRequest,
    FornecedorResponse,
    Municipio,
    PageResponse,
    TipoFornecedor,
} from '../../core/models/fornecedor.model';

@Injectable({ providedIn: 'root' })
export class FornecedorService {
  private readonly apiUrl = `${environment.apiUrl}/fornecedores`;
  private readonly dominioUrl = `${environment.apiUrl}/dominios`;

  constructor(private readonly http: HttpClient) {}

  // ───── CRUD ─────

  findAll(filters: Record<string, string | number | boolean | undefined>, page = 0, size = 20, sort = 'razaoSocial,asc'): Observable<PageResponse<FornecedorListItem>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, String(value));
      }
    });

    return this.http.get<PageResponse<FornecedorListItem>>(this.apiUrl, { params });
  }

  findById(id: number): Observable<FornecedorResponse> {
    return this.http.get<FornecedorResponse>(`${this.apiUrl}/${id}`);
  }

  create(data: FornecedorRequest): Observable<FornecedorResponse> {
    return this.http.post<FornecedorResponse>(this.apiUrl, data);
  }

  update(id: number, data: FornecedorRequest): Observable<FornecedorResponse> {
    return this.http.put<FornecedorResponse>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // ───── Domain Data ─────

  getEstados(): Observable<Estado[]> {
    return this.http.get<Estado[]>(`${this.dominioUrl}/estados`);
  }

  getMunicipios(estadoId: number): Observable<Municipio[]> {
    return this.http.get<Municipio[]>(`${this.dominioUrl}/municipios`, {
      params: new HttpParams().set('estadoId', estadoId.toString()),
    });
  }

  searchMunicipios(nome: string): Observable<Municipio[]> {
    return this.http.get<Municipio[]>(`${this.dominioUrl}/municipios`, {
      params: new HttpParams().set('nome', nome),
    });
  }

  getTiposFornecedor(): Observable<TipoFornecedor[]> {
    return this.http.get<TipoFornecedor[]>(`${this.dominioUrl}/tipos-fornecedor`);
  }

  getClassificacoes(): Observable<ClassificacaoFornecedor[]> {
    return this.http.get<ClassificacaoFornecedor[]>(`${this.dominioUrl}/classificacoes-fornecedor`);
  }
}
