import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../../core/models/fornecedor.model';
import {
    CargoListItem, CargoRequest, CargoResponse,
    ConsignadoListItem, ConsignadoRequest, ConsignadoResponse,
    DependenteListItem, DependenteRequest, DependenteResponse,
    EventoFolhaListItem, EventoFolhaRequest, EventoFolhaResponse,
    FeriasListItem, FeriasRequest, FeriasResponse,
    FolhaPagamentoListItem, FolhaPagamentoRequest, FolhaPagamentoResponse,
    HistoricoFuncionalListItem, HistoricoFuncionalRequest, HistoricoFuncionalResponse,
    LicencaListItem, LicencaRequest, LicencaResponse,
    NivelSalarialListItem, NivelSalarialRequest, NivelSalarialResponse,
    RoleItem,
    ServidorListItem, ServidorRequest, ServidorResponse,
    TipoVinculo,
} from '../../core/models/rh.model';

@Injectable({ providedIn: 'root' })
export class RhService {
  private readonly apiUrl = `${environment.apiUrl}/rh`;

  constructor(private readonly http: HttpClient) {}

  // ========================
  // Cargos
  // ========================
  findAllCargos(filters: Record<string, any>, page: number, size: number, sort = 'descricao,asc'): Observable<PageResponse<CargoListItem>> {
    return this.http.get<PageResponse<CargoListItem>>(`${this.apiUrl}/cargos`, { params: this.buildParams(filters, page, size, sort) });
  }
  findCargoById(id: number): Observable<CargoResponse> {
    return this.http.get<CargoResponse>(`${this.apiUrl}/cargos/${id}`);
  }
  createCargo(data: CargoRequest): Observable<CargoResponse> {
    return this.http.post<CargoResponse>(`${this.apiUrl}/cargos`, data);
  }
  updateCargo(id: number, data: CargoRequest): Observable<CargoResponse> {
    return this.http.put<CargoResponse>(`${this.apiUrl}/cargos/${id}`, data);
  }
  deleteCargo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cargos/${id}`);
  }

  // ========================
  // Servidores
  // ========================
  findAllServidores(filters: Record<string, any>, page: number, size: number, sort = 'nome,asc'): Observable<PageResponse<ServidorListItem>> {
    return this.http.get<PageResponse<ServidorListItem>>(`${this.apiUrl}/servidores`, { params: this.buildParams(filters, page, size, sort) });
  }
  findServidorById(id: number): Observable<ServidorResponse> {
    return this.http.get<ServidorResponse>(`${this.apiUrl}/servidores/${id}`);
  }
  createServidor(data: ServidorRequest): Observable<ServidorResponse> {
    return this.http.post<ServidorResponse>(`${this.apiUrl}/servidores`, data);
  }
  updateServidor(id: number, data: ServidorRequest): Observable<ServidorResponse> {
    return this.http.put<ServidorResponse>(`${this.apiUrl}/servidores/${id}`, data);
  }
  deleteServidor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/servidores/${id}`);
  }
  findRoles(): Observable<RoleItem[]> {
    return this.http.get<RoleItem[]>(`${this.apiUrl}/servidores/roles`);
  }

  // ========================
  // Folhas de Pagamento
  // ========================
  findAllFolhas(filters: Record<string, any>, page: number, size: number, sort = 'competencia,desc'): Observable<PageResponse<FolhaPagamentoListItem>> {
    return this.http.get<PageResponse<FolhaPagamentoListItem>>(`${this.apiUrl}/folhas-pagamento`, { params: this.buildParams(filters, page, size, sort) });
  }
  findFolhaById(id: number): Observable<FolhaPagamentoResponse> {
    return this.http.get<FolhaPagamentoResponse>(`${this.apiUrl}/folhas-pagamento/${id}`);
  }
  createFolha(data: FolhaPagamentoRequest): Observable<FolhaPagamentoResponse> {
    return this.http.post<FolhaPagamentoResponse>(`${this.apiUrl}/folhas-pagamento`, data);
  }
  updateFolha(id: number, data: FolhaPagamentoRequest): Observable<FolhaPagamentoResponse> {
    return this.http.put<FolhaPagamentoResponse>(`${this.apiUrl}/folhas-pagamento/${id}`, data);
  }
  deleteFolha(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/folhas-pagamento/${id}`);
  }

  // ========================
  // Domínios
  // ========================
  findTiposVinculo(): Observable<TipoVinculo[]> {
    return this.http.get<TipoVinculo[]>(`${environment.apiUrl}/arrecadacao/dominios/tipos-vinculo`);
  }
  findCargosLookup(): Observable<CargoListItem[]> {
    return this.http.get<CargoListItem[]>(`${environment.apiUrl}/arrecadacao/dominios/cargos`);
  }

  // ========================
  // Níveis Salariais
  // ========================
  findAllNiveisSalariais(filters: Record<string, any>, page: number, size: number, sort = 'nivel,asc'): Observable<PageResponse<NivelSalarialListItem>> {
    return this.http.get<PageResponse<NivelSalarialListItem>>(`${this.apiUrl}/niveis-salariais`, { params: this.buildParams(filters, page, size, sort) });
  }
  findNivelSalarialById(id: number): Observable<NivelSalarialResponse> {
    return this.http.get<NivelSalarialResponse>(`${this.apiUrl}/niveis-salariais/${id}`);
  }
  createNivelSalarial(data: NivelSalarialRequest): Observable<NivelSalarialResponse> {
    return this.http.post<NivelSalarialResponse>(`${this.apiUrl}/niveis-salariais`, data);
  }
  updateNivelSalarial(id: number, data: NivelSalarialRequest): Observable<NivelSalarialResponse> {
    return this.http.put<NivelSalarialResponse>(`${this.apiUrl}/niveis-salariais/${id}`, data);
  }
  deleteNivelSalarial(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/niveis-salariais/${id}`);
  }

  // ========================
  // Dependentes
  // ========================
  findAllDependentes(filters: Record<string, any>, page: number, size: number, sort = 'nome,asc'): Observable<PageResponse<DependenteListItem>> {
    return this.http.get<PageResponse<DependenteListItem>>(`${this.apiUrl}/dependentes`, { params: this.buildParams(filters, page, size, sort) });
  }
  findDependenteById(id: number): Observable<DependenteResponse> {
    return this.http.get<DependenteResponse>(`${this.apiUrl}/dependentes/${id}`);
  }
  createDependente(data: DependenteRequest): Observable<DependenteResponse> {
    return this.http.post<DependenteResponse>(`${this.apiUrl}/dependentes`, data);
  }
  updateDependente(id: number, data: DependenteRequest): Observable<DependenteResponse> {
    return this.http.put<DependenteResponse>(`${this.apiUrl}/dependentes/${id}`, data);
  }
  deleteDependente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/dependentes/${id}`);
  }

  // ========================
  // Eventos de Folha
  // ========================
  findAllEventosFolha(filters: Record<string, any>, page: number, size: number, sort = 'codigo,asc'): Observable<PageResponse<EventoFolhaListItem>> {
    return this.http.get<PageResponse<EventoFolhaListItem>>(`${this.apiUrl}/eventos-folha`, { params: this.buildParams(filters, page, size, sort) });
  }
  findEventoFolhaById(id: number): Observable<EventoFolhaResponse> {
    return this.http.get<EventoFolhaResponse>(`${this.apiUrl}/eventos-folha/${id}`);
  }
  createEventoFolha(data: EventoFolhaRequest): Observable<EventoFolhaResponse> {
    return this.http.post<EventoFolhaResponse>(`${this.apiUrl}/eventos-folha`, data);
  }
  updateEventoFolha(id: number, data: EventoFolhaRequest): Observable<EventoFolhaResponse> {
    return this.http.put<EventoFolhaResponse>(`${this.apiUrl}/eventos-folha/${id}`, data);
  }
  deleteEventoFolha(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/eventos-folha/${id}`);
  }

  // ========================
  // Férias
  // ========================
  findAllFerias(filters: Record<string, any>, page: number, size: number, sort = 'periodoAquisitivoInicio,desc'): Observable<PageResponse<FeriasListItem>> {
    return this.http.get<PageResponse<FeriasListItem>>(`${this.apiUrl}/ferias`, { params: this.buildParams(filters, page, size, sort) });
  }
  findFeriasById(id: number): Observable<FeriasResponse> {
    return this.http.get<FeriasResponse>(`${this.apiUrl}/ferias/${id}`);
  }
  createFerias(data: FeriasRequest): Observable<FeriasResponse> {
    return this.http.post<FeriasResponse>(`${this.apiUrl}/ferias`, data);
  }
  updateFerias(id: number, data: FeriasRequest): Observable<FeriasResponse> {
    return this.http.put<FeriasResponse>(`${this.apiUrl}/ferias/${id}`, data);
  }
  deleteFerias(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ferias/${id}`);
  }

  // ========================
  // Licenças
  // ========================
  findAllLicencas(filters: Record<string, any>, page: number, size: number, sort = 'dataInicio,desc'): Observable<PageResponse<LicencaListItem>> {
    return this.http.get<PageResponse<LicencaListItem>>(`${this.apiUrl}/licencas`, { params: this.buildParams(filters, page, size, sort) });
  }
  findLicencaById(id: number): Observable<LicencaResponse> {
    return this.http.get<LicencaResponse>(`${this.apiUrl}/licencas/${id}`);
  }
  createLicenca(data: LicencaRequest): Observable<LicencaResponse> {
    return this.http.post<LicencaResponse>(`${this.apiUrl}/licencas`, data);
  }
  updateLicenca(id: number, data: LicencaRequest): Observable<LicencaResponse> {
    return this.http.put<LicencaResponse>(`${this.apiUrl}/licencas/${id}`, data);
  }
  deleteLicenca(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/licencas/${id}`);
  }

  // ========================
  // Histórico Funcional
  // ========================
  findAllHistoricoFuncional(filters: Record<string, any>, page: number, size: number, sort = 'dataEvento,desc'): Observable<PageResponse<HistoricoFuncionalListItem>> {
    return this.http.get<PageResponse<HistoricoFuncionalListItem>>(`${this.apiUrl}/historico-funcional`, { params: this.buildParams(filters, page, size, sort) });
  }
  findHistoricoFuncionalById(id: number): Observable<HistoricoFuncionalResponse> {
    return this.http.get<HistoricoFuncionalResponse>(`${this.apiUrl}/historico-funcional/${id}`);
  }
  createHistoricoFuncional(data: HistoricoFuncionalRequest): Observable<HistoricoFuncionalResponse> {
    return this.http.post<HistoricoFuncionalResponse>(`${this.apiUrl}/historico-funcional`, data);
  }
  updateHistoricoFuncional(id: number, data: HistoricoFuncionalRequest): Observable<HistoricoFuncionalResponse> {
    return this.http.put<HistoricoFuncionalResponse>(`${this.apiUrl}/historico-funcional/${id}`, data);
  }
  deleteHistoricoFuncional(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/historico-funcional/${id}`);
  }

  // ========================
  // Consignados
  // ========================
  findAllConsignados(filters: Record<string, any>, page: number, size: number, sort = 'dataInicio,desc'): Observable<PageResponse<ConsignadoListItem>> {
    return this.http.get<PageResponse<ConsignadoListItem>>(`${this.apiUrl}/consignados`, { params: this.buildParams(filters, page, size, sort) });
  }
  findConsignadoById(id: number): Observable<ConsignadoResponse> {
    return this.http.get<ConsignadoResponse>(`${this.apiUrl}/consignados/${id}`);
  }
  createConsignado(data: ConsignadoRequest): Observable<ConsignadoResponse> {
    return this.http.post<ConsignadoResponse>(`${this.apiUrl}/consignados`, data);
  }
  updateConsignado(id: number, data: ConsignadoRequest): Observable<ConsignadoResponse> {
    return this.http.put<ConsignadoResponse>(`${this.apiUrl}/consignados/${id}`, data);
  }
  deleteConsignado(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/consignados/${id}`);
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
