import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ConciliacaoBancariaListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-conciliacao-bancaria-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './conciliacao-bancaria-list.component.html',
  styleUrl: './conciliacao-bancaria-list.component.scss',
})
export class ConciliacaoBancariaListComponent extends BaseListComponent<ConciliacaoBancariaListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'contaBancariaDescricao', label: 'Conta Bancária' },
    { key: 'mesReferencia', label: 'Mês', width: '80px' },
    { key: 'anoReferencia', label: 'Ano', width: '80px' },
    {
      key: 'saldoExtrato',
      label: 'Saldo Extrato',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'saldoContabil',
      label: 'Saldo Contábil',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'diferenca',
      label: 'Diferença',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'anoReferencia', label: 'Ano', type: 'text' },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'PENDENTE', label: 'Pendente' },
        { value: 'CONCILIADO', label: 'Conciliado' },
        { value: 'DIVERGENTE', label: 'Divergente' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/conciliacao-bancaria';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllConciliacoes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: ConciliacaoBancariaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ConciliacaoBancariaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta conciliação?')) {
      this.contabilidadeService.deleteConciliacao(item.id).subscribe({
        next: () => {
          this.snackBar.open('Conciliação excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
