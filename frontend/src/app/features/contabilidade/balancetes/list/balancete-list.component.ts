import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BalanceteListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-balancete-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, FilterComponent, DataTableComponent],
  templateUrl: './balancete-list.component.html',
  styleUrl: './balancete-list.component.scss',
})
export class BalanceteListComponent extends BaseListComponent<BalanceteListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'mes', label: 'Mês', width: '80px' },
    { key: 'planoContaCodigo', label: 'Conta', width: '150px' },
    { key: 'planoContaDescricao', label: 'Descrição' },
    {
      key: 'saldoAnterior',
      label: 'Saldo Anterior',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'totalDebitos',
      label: 'Débitos',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'totalCreditos',
      label: 'Créditos',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'saldoAtual',
      label: 'Saldo Atual',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    { key: 'mes', label: 'Mês', type: 'text' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/balancetes';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllBalancetes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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
}
