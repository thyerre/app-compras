import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { EmpenhoListItem } from '../../../../core/models/compras.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ComprasService } from '../../../../shared/services/compras.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-empenho-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './empenho-list.component.html',
  styleUrl: './empenho-list.component.scss',
})
export class EmpenhoListComponent extends BaseListComponent<EmpenhoListItem> {
  private readonly comprasService = inject(ComprasService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroEmpenho', label: 'Nº Empenho', width: '140px' },
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'processoNumero', label: 'Processo' },
    { key: 'fornecedorNome', label: 'Fornecedor' },
    { key: 'tipoEmpenho', label: 'Tipo', width: '120px' },
    { key: 'dataEmpenho', label: 'Data', width: '110px' },
    {
      key: 'valor',
      label: 'Valor',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'numeroEmpenho', label: 'Nº Empenho', type: 'text' },
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'EMITIDO', label: 'Emitido' },
        { value: 'LIQUIDADO', label: 'Liquidado' },
        { value: 'PAGO', label: 'Pago' },
        { value: 'ANULADO', label: 'Anulado' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/empenhos';
  }

  loadData(): void {
    this.loading.set(true);
    this.comprasService.findAllEmpenhos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: EmpenhoListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: EmpenhoListItem): void {
    if (confirm('Tem certeza que deseja excluir este empenho?')) {
      this.comprasService.deleteEmpenho(item.id).subscribe({
        next: () => {
          this.snackBar.open('Empenho excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
