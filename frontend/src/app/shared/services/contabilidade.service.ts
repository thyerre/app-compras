import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
    BalanceteListItem,
    BemPatrimonialListItem, BemPatrimonialRequest, BemPatrimonialResponse,
    ConciliacaoBancariaListItem, ConciliacaoBancariaRequest, ConciliacaoBancariaResponse,
    ContaBancariaListItem, ContaBancariaRequest, ContaBancariaResponse,
    CreditoAdicionalListItem, CreditoAdicionalRequest, CreditoAdicionalResponse,
    DemonstracaoContabilListItem, DemonstracaoContabilRequest, DemonstracaoContabilResponse,
    DividaPublicaListItem, DividaPublicaRequest, DividaPublicaResponse,
    LancamentoContabilListItem, LancamentoContabilRequest, LancamentoContabilResponse,
    LiquidacaoListItem, LiquidacaoRequest, LiquidacaoResponse,
    PagamentoListItem, PagamentoRequest, PagamentoResponse,
    PlanoContasListItem, PlanoContasRequest, PlanoContasResponse,
    ReceitaOrcamentariaListItem, ReceitaOrcamentariaRequest, ReceitaOrcamentariaResponse,
    RelatorioRgfListItem, RelatorioRgfRequest, RelatorioRgfResponse,
    RelatorioRreoListItem, RelatorioRreoRequest, RelatorioRreoResponse,
    RestosAPagarListItem, RestosAPagarRequest, RestosAPagarResponse,
} from '../../core/models/contabilidade.model';
import { PageResponse } from '../../core/models/fornecedor.model';

@Injectable({ providedIn: 'root' })
export class ContabilidadeService {
  private readonly apiUrl = `${environment.apiUrl}/contabilidade`;

  constructor(private readonly http: HttpClient) {}

  // ========================
  // Plano de Contas
  // ========================
  findAllPlanoContas(filters: Record<string, any>, page: number, size: number, sort = 'codigo,asc'): Observable<PageResponse<PlanoContasListItem>> {
    return this.http.get<PageResponse<PlanoContasListItem>>(`${this.apiUrl}/plano-contas`, { params: this.buildParams(filters, page, size, sort) });
  }
  findPlanoContasById(id: number): Observable<PlanoContasResponse> {
    return this.http.get<PlanoContasResponse>(`${this.apiUrl}/plano-contas/${id}`);
  }
  createPlanoContas(data: PlanoContasRequest): Observable<PlanoContasResponse> {
    return this.http.post<PlanoContasResponse>(`${this.apiUrl}/plano-contas`, data);
  }
  updatePlanoContas(id: number, data: PlanoContasRequest): Observable<PlanoContasResponse> {
    return this.http.put<PlanoContasResponse>(`${this.apiUrl}/plano-contas/${id}`, data);
  }
  deletePlanoContas(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/plano-contas/${id}`);
  }

  // ========================
  // Lançamentos Contábeis
  // ========================
  findAllLancamentos(filters: Record<string, any>, page: number, size: number, sort = 'dataLancamento,desc'): Observable<PageResponse<LancamentoContabilListItem>> {
    return this.http.get<PageResponse<LancamentoContabilListItem>>(`${this.apiUrl}/lancamentos`, { params: this.buildParams(filters, page, size, sort) });
  }
  findLancamentoById(id: number): Observable<LancamentoContabilResponse> {
    return this.http.get<LancamentoContabilResponse>(`${this.apiUrl}/lancamentos/${id}`);
  }
  createLancamento(data: LancamentoContabilRequest): Observable<LancamentoContabilResponse> {
    return this.http.post<LancamentoContabilResponse>(`${this.apiUrl}/lancamentos`, data);
  }
  updateLancamento(id: number, data: LancamentoContabilRequest): Observable<LancamentoContabilResponse> {
    return this.http.put<LancamentoContabilResponse>(`${this.apiUrl}/lancamentos/${id}`, data);
  }
  deleteLancamento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/lancamentos/${id}`);
  }

  // ========================
  // Liquidações
  // ========================
  findAllLiquidacoes(filters: Record<string, any>, page: number, size: number, sort = 'dataLiquidacao,desc'): Observable<PageResponse<LiquidacaoListItem>> {
    return this.http.get<PageResponse<LiquidacaoListItem>>(`${this.apiUrl}/liquidacoes`, { params: this.buildParams(filters, page, size, sort) });
  }
  findLiquidacaoById(id: number): Observable<LiquidacaoResponse> {
    return this.http.get<LiquidacaoResponse>(`${this.apiUrl}/liquidacoes/${id}`);
  }
  createLiquidacao(data: LiquidacaoRequest): Observable<LiquidacaoResponse> {
    return this.http.post<LiquidacaoResponse>(`${this.apiUrl}/liquidacoes`, data);
  }
  updateLiquidacao(id: number, data: LiquidacaoRequest): Observable<LiquidacaoResponse> {
    return this.http.put<LiquidacaoResponse>(`${this.apiUrl}/liquidacoes/${id}`, data);
  }
  deleteLiquidacao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/liquidacoes/${id}`);
  }

  // ========================
  // Pagamentos
  // ========================
  findAllPagamentos(filters: Record<string, any>, page: number, size: number, sort = 'dataPagamento,desc'): Observable<PageResponse<PagamentoListItem>> {
    return this.http.get<PageResponse<PagamentoListItem>>(`${this.apiUrl}/pagamentos`, { params: this.buildParams(filters, page, size, sort) });
  }
  findPagamentoById(id: number): Observable<PagamentoResponse> {
    return this.http.get<PagamentoResponse>(`${this.apiUrl}/pagamentos/${id}`);
  }
  createPagamento(data: PagamentoRequest): Observable<PagamentoResponse> {
    return this.http.post<PagamentoResponse>(`${this.apiUrl}/pagamentos`, data);
  }
  updatePagamento(id: number, data: PagamentoRequest): Observable<PagamentoResponse> {
    return this.http.put<PagamentoResponse>(`${this.apiUrl}/pagamentos/${id}`, data);
  }
  deletePagamento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/pagamentos/${id}`);
  }

  // ========================
  // Contas Bancárias
  // ========================
  findAllContasBancarias(filters: Record<string, any>, page: number, size: number, sort = 'descricao,asc'): Observable<PageResponse<ContaBancariaListItem>> {
    return this.http.get<PageResponse<ContaBancariaListItem>>(`${this.apiUrl}/contas-bancarias`, { params: this.buildParams(filters, page, size, sort) });
  }
  findContaBancariaById(id: number): Observable<ContaBancariaResponse> {
    return this.http.get<ContaBancariaResponse>(`${this.apiUrl}/contas-bancarias/${id}`);
  }
  createContaBancaria(data: ContaBancariaRequest): Observable<ContaBancariaResponse> {
    return this.http.post<ContaBancariaResponse>(`${this.apiUrl}/contas-bancarias`, data);
  }
  updateContaBancaria(id: number, data: ContaBancariaRequest): Observable<ContaBancariaResponse> {
    return this.http.put<ContaBancariaResponse>(`${this.apiUrl}/contas-bancarias/${id}`, data);
  }
  deleteContaBancaria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/contas-bancarias/${id}`);
  }

  // ========================
  // Receita Orçamentária
  // ========================
  findAllReceitasOrcamentarias(filters: Record<string, any>, page: number, size: number, sort = 'codigoReceita,asc'): Observable<PageResponse<ReceitaOrcamentariaListItem>> {
    return this.http.get<PageResponse<ReceitaOrcamentariaListItem>>(`${this.apiUrl}/receita-orcamentaria`, { params: this.buildParams(filters, page, size, sort) });
  }
  findReceitaOrcamentariaById(id: number): Observable<ReceitaOrcamentariaResponse> {
    return this.http.get<ReceitaOrcamentariaResponse>(`${this.apiUrl}/receita-orcamentaria/${id}`);
  }
  createReceitaOrcamentaria(data: ReceitaOrcamentariaRequest): Observable<ReceitaOrcamentariaResponse> {
    return this.http.post<ReceitaOrcamentariaResponse>(`${this.apiUrl}/receita-orcamentaria`, data);
  }
  updateReceitaOrcamentaria(id: number, data: ReceitaOrcamentariaRequest): Observable<ReceitaOrcamentariaResponse> {
    return this.http.put<ReceitaOrcamentariaResponse>(`${this.apiUrl}/receita-orcamentaria/${id}`, data);
  }
  deleteReceitaOrcamentaria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/receita-orcamentaria/${id}`);
  }

  // ========================
  // Créditos Adicionais
  // ========================
  findAllCreditosAdicionais(filters: Record<string, any>, page: number, size: number, sort = 'dataDecreto,desc'): Observable<PageResponse<CreditoAdicionalListItem>> {
    return this.http.get<PageResponse<CreditoAdicionalListItem>>(`${this.apiUrl}/creditos-adicionais`, { params: this.buildParams(filters, page, size, sort) });
  }
  findCreditoAdicionalById(id: number): Observable<CreditoAdicionalResponse> {
    return this.http.get<CreditoAdicionalResponse>(`${this.apiUrl}/creditos-adicionais/${id}`);
  }
  createCreditoAdicional(data: CreditoAdicionalRequest): Observable<CreditoAdicionalResponse> {
    return this.http.post<CreditoAdicionalResponse>(`${this.apiUrl}/creditos-adicionais`, data);
  }
  updateCreditoAdicional(id: number, data: CreditoAdicionalRequest): Observable<CreditoAdicionalResponse> {
    return this.http.put<CreditoAdicionalResponse>(`${this.apiUrl}/creditos-adicionais/${id}`, data);
  }
  deleteCreditoAdicional(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/creditos-adicionais/${id}`);
  }

  // ========================
  // Restos a Pagar
  // ========================
  findAllRestosAPagar(filters: Record<string, any>, page: number, size: number, sort = 'dataInscricao,desc'): Observable<PageResponse<RestosAPagarListItem>> {
    return this.http.get<PageResponse<RestosAPagarListItem>>(`${this.apiUrl}/restos-a-pagar`, { params: this.buildParams(filters, page, size, sort) });
  }
  findRestosAPagarById(id: number): Observable<RestosAPagarResponse> {
    return this.http.get<RestosAPagarResponse>(`${this.apiUrl}/restos-a-pagar/${id}`);
  }
  createRestosAPagar(data: RestosAPagarRequest): Observable<RestosAPagarResponse> {
    return this.http.post<RestosAPagarResponse>(`${this.apiUrl}/restos-a-pagar`, data);
  }
  updateRestosAPagar(id: number, data: RestosAPagarRequest): Observable<RestosAPagarResponse> {
    return this.http.put<RestosAPagarResponse>(`${this.apiUrl}/restos-a-pagar/${id}`, data);
  }
  deleteRestosAPagar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/restos-a-pagar/${id}`);
  }

  // ========================
  // Bens Patrimoniais
  // ========================
  findAllBensPatrimoniais(filters: Record<string, any>, page: number, size: number, sort = 'dataAquisicao,desc'): Observable<PageResponse<BemPatrimonialListItem>> {
    return this.http.get<PageResponse<BemPatrimonialListItem>>(`${this.apiUrl}/bens-patrimoniais`, { params: this.buildParams(filters, page, size, sort) });
  }
  findBemPatrimonialById(id: number): Observable<BemPatrimonialResponse> {
    return this.http.get<BemPatrimonialResponse>(`${this.apiUrl}/bens-patrimoniais/${id}`);
  }
  createBemPatrimonial(data: BemPatrimonialRequest): Observable<BemPatrimonialResponse> {
    return this.http.post<BemPatrimonialResponse>(`${this.apiUrl}/bens-patrimoniais`, data);
  }
  updateBemPatrimonial(id: number, data: BemPatrimonialRequest): Observable<BemPatrimonialResponse> {
    return this.http.put<BemPatrimonialResponse>(`${this.apiUrl}/bens-patrimoniais/${id}`, data);
  }
  deleteBemPatrimonial(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/bens-patrimoniais/${id}`);
  }

  // ========================
  // Dívida Pública
  // ========================
  findAllDividaPublica(filters: Record<string, any>, page: number, size: number, sort = 'dataContratacao,desc'): Observable<PageResponse<DividaPublicaListItem>> {
    return this.http.get<PageResponse<DividaPublicaListItem>>(`${this.apiUrl}/divida-publica`, { params: this.buildParams(filters, page, size, sort) });
  }
  findDividaPublicaById(id: number): Observable<DividaPublicaResponse> {
    return this.http.get<DividaPublicaResponse>(`${this.apiUrl}/divida-publica/${id}`);
  }
  createDividaPublica(data: DividaPublicaRequest): Observable<DividaPublicaResponse> {
    return this.http.post<DividaPublicaResponse>(`${this.apiUrl}/divida-publica`, data);
  }
  updateDividaPublica(id: number, data: DividaPublicaRequest): Observable<DividaPublicaResponse> {
    return this.http.put<DividaPublicaResponse>(`${this.apiUrl}/divida-publica/${id}`, data);
  }
  deleteDividaPublica(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/divida-publica/${id}`);
  }

  // ========================
  // Conciliação Bancária
  // ========================
  findAllConciliacoes(filters: Record<string, any>, page: number, size: number, sort = 'anoReferencia,desc'): Observable<PageResponse<ConciliacaoBancariaListItem>> {
    return this.http.get<PageResponse<ConciliacaoBancariaListItem>>(`${this.apiUrl}/conciliacao-bancaria`, { params: this.buildParams(filters, page, size, sort) });
  }
  findConciliacaoById(id: number): Observable<ConciliacaoBancariaResponse> {
    return this.http.get<ConciliacaoBancariaResponse>(`${this.apiUrl}/conciliacao-bancaria/${id}`);
  }
  createConciliacao(data: ConciliacaoBancariaRequest): Observable<ConciliacaoBancariaResponse> {
    return this.http.post<ConciliacaoBancariaResponse>(`${this.apiUrl}/conciliacao-bancaria`, data);
  }
  updateConciliacao(id: number, data: ConciliacaoBancariaRequest): Observable<ConciliacaoBancariaResponse> {
    return this.http.put<ConciliacaoBancariaResponse>(`${this.apiUrl}/conciliacao-bancaria/${id}`, data);
  }
  deleteConciliacao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/conciliacao-bancaria/${id}`);
  }

  // ========================
  // Balancetes (read-only)
  // ========================
  findAllBalancetes(filters: Record<string, any>, page: number, size: number, sort = 'exercicio,desc'): Observable<PageResponse<BalanceteListItem>> {
    return this.http.get<PageResponse<BalanceteListItem>>(`${this.apiUrl}/balancetes`, { params: this.buildParams(filters, page, size, sort) });
  }

  // ========================
  // Demonstrações Contábeis
  // ========================
  findAllDemonstracoes(filters: Record<string, any>, page: number, size: number, sort = 'exercicio,desc'): Observable<PageResponse<DemonstracaoContabilListItem>> {
    return this.http.get<PageResponse<DemonstracaoContabilListItem>>(`${this.apiUrl}/demonstracoes`, { params: this.buildParams(filters, page, size, sort) });
  }
  findDemonstracaoById(id: number): Observable<DemonstracaoContabilResponse> {
    return this.http.get<DemonstracaoContabilResponse>(`${this.apiUrl}/demonstracoes/${id}`);
  }
  createDemonstracao(data: DemonstracaoContabilRequest): Observable<DemonstracaoContabilResponse> {
    return this.http.post<DemonstracaoContabilResponse>(`${this.apiUrl}/demonstracoes`, data);
  }
  updateDemonstracao(id: number, data: DemonstracaoContabilRequest): Observable<DemonstracaoContabilResponse> {
    return this.http.put<DemonstracaoContabilResponse>(`${this.apiUrl}/demonstracoes/${id}`, data);
  }
  deleteDemonstracao(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/demonstracoes/${id}`);
  }

  // ========================
  // Relatórios RREO
  // ========================
  findAllRreo(filters: Record<string, any>, page: number, size: number, sort = 'exercicio,desc'): Observable<PageResponse<RelatorioRreoListItem>> {
    return this.http.get<PageResponse<RelatorioRreoListItem>>(`${this.apiUrl}/rreo`, { params: this.buildParams(filters, page, size, sort) });
  }
  findRreoById(id: number): Observable<RelatorioRreoResponse> {
    return this.http.get<RelatorioRreoResponse>(`${this.apiUrl}/rreo/${id}`);
  }
  createRreo(data: RelatorioRreoRequest): Observable<RelatorioRreoResponse> {
    return this.http.post<RelatorioRreoResponse>(`${this.apiUrl}/rreo`, data);
  }
  updateRreo(id: number, data: RelatorioRreoRequest): Observable<RelatorioRreoResponse> {
    return this.http.put<RelatorioRreoResponse>(`${this.apiUrl}/rreo/${id}`, data);
  }
  deleteRreo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/rreo/${id}`);
  }

  // ========================
  // Relatórios RGF
  // ========================
  findAllRgf(filters: Record<string, any>, page: number, size: number, sort = 'exercicio,desc'): Observable<PageResponse<RelatorioRgfListItem>> {
    return this.http.get<PageResponse<RelatorioRgfListItem>>(`${this.apiUrl}/rgf`, { params: this.buildParams(filters, page, size, sort) });
  }
  findRgfById(id: number): Observable<RelatorioRgfResponse> {
    return this.http.get<RelatorioRgfResponse>(`${this.apiUrl}/rgf/${id}`);
  }
  createRgf(data: RelatorioRgfRequest): Observable<RelatorioRgfResponse> {
    return this.http.post<RelatorioRgfResponse>(`${this.apiUrl}/rgf`, data);
  }
  updateRgf(id: number, data: RelatorioRgfRequest): Observable<RelatorioRgfResponse> {
    return this.http.put<RelatorioRgfResponse>(`${this.apiUrl}/rgf/${id}`, data);
  }
  deleteRgf(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/rgf/${id}`);
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
