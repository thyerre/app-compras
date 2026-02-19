import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { LancamentoContabilListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-lancamento-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './lancamento-list.component.html',
  styleUrl: './lancamento-list.component.scss',
})
export class LancamentoListComponent extends BaseListComponent<LancamentoContabilListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroLancamento', label: 'Nº Lanç.', width: '130px' },
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'dataLancamento', label: 'Data', width: '110px' },
    { key: 'tipo', label: 'Tipo', width: '100px' },
    { key: 'origem', label: 'Origem', width: '100px' },
    { key: 'historico', label: 'Histórico' },
    {
      key: 'totalDebito',
      label: 'Débito',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'totalCredito',
      label: 'Crédito',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'tipo',
      label: 'Tipo',
      type: 'select',
      options: [
        { value: 'NORMAL', label: 'Normal' },
        { value: 'ESTORNO', label: 'Estorno' },
        { value: 'ENCERRAMENTO', label: 'Encerramento' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/lancamentos';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllLancamentos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: LancamentoContabilListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: LancamentoContabilListItem): void {
    if (confirm('Tem certeza que deseja excluir este lançamento?')) {
      this.contabilidadeService.deleteLancamento(item.id).subscribe({
        next: () => {
          this.snackBar.open('Lançamento excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
