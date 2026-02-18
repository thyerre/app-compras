import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { DotacaoOrcamentariaListItem, OrgaoResponse } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-dotacao-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './dotacao-list.component.html',
  styleUrl: './dotacao-list.component.scss',
})
export class DotacaoListComponent extends BaseListComponent<DotacaoOrcamentariaListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly orgaos = signal<OrgaoResponse[]>([]);
  readonly filters = signal<Record<string, any>>({});
  readonly filterFields = signal<FilterField[]>([]);

  readonly columns: TableColumn[] = [
    { key: 'codigoDotacao', label: 'Código', width: '160px' },
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'orgaoNome', label: 'Órgão' },
    { key: 'programaNome', label: 'Programa' },
    { key: 'naturezaDespesaDescricao', label: 'Natureza Despesa' },
    {
      key: 'valorInicial',
      label: 'Valor Inicial',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'saldoDisponivel',
      label: 'Saldo Disponível',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
  ];

  override ngOnInit(): void {
    this.loadDomainData();
    super.ngOnInit();
  }

  getRouteBase(): string {
    return '/dotacoes';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllDotacoes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => {
        this.items.set(page.content);
        this.totalElements.set(page.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void {
    this.filters.set({});
  }

  onFilterSearch(values: Record<string, any>): void {
    this.filters.set(values);
    this.onSearch();
  }

  onFilterClear(): void {
    this.onClearFilters();
  }

  onEditItem(item: DotacaoOrcamentariaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: DotacaoOrcamentariaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta dotação?')) {
      this.planejamentoService.deleteDotacao(item.id).subscribe({
        next: () => {
          this.snackBar.open('Dotação excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }

  private loadDomainData(): void {
    this.planejamentoService.getOrgaosSimples().subscribe(data => {
      this.orgaos.set(data);
      this.buildFilterFields();
    });
  }

  private buildFilterFields(): void {
    this.filterFields.set([
      { key: 'exercicio', label: 'Exercício', type: 'text' },
      { key: 'codigoDotacao', label: 'Código', type: 'text' },
      {
        key: 'orgaoId',
        label: 'Órgão',
        type: 'select',
        options: this.orgaos().map(o => ({ value: o.id, label: o.nome })),
      },
    ]);
  }
}
