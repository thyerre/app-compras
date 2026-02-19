import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { PagamentoListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-pagamento-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './pagamento-list.component.html',
  styleUrl: './pagamento-list.component.scss',
})
export class PagamentoListComponent extends BaseListComponent<PagamentoListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroPagamento', label: 'Nº Pgto.', width: '130px' },
    { key: 'liquidacaoNumero', label: 'Liquidação' },
    { key: 'fornecedorNome', label: 'Fornecedor' },
    { key: 'dataPagamento', label: 'Data', width: '110px' },
    {
      key: 'valor',
      label: 'Valor',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'formaPagamento', label: 'Forma', width: '120px' },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'numeroPagamento', label: 'Nº Pagamento', type: 'text' },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'PAGO', label: 'Pago' },
        { value: 'ANULADO', label: 'Anulado' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/pagamentos';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllPagamentos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: PagamentoListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: PagamentoListItem): void {
    if (confirm('Tem certeza que deseja excluir este pagamento?')) {
      this.contabilidadeService.deletePagamento(item.id).subscribe({
        next: () => {
          this.snackBar.open('Pagamento excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
