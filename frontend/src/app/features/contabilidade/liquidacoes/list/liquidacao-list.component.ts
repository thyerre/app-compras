import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { LiquidacaoListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-liquidacao-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './liquidacao-list.component.html',
  styleUrl: './liquidacao-list.component.scss',
})
export class LiquidacaoListComponent extends BaseListComponent<LiquidacaoListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroLiquidacao', label: 'Nº Liquid.', width: '130px' },
    { key: 'empenhoNumero', label: 'Empenho' },
    { key: 'fornecedorNome', label: 'Fornecedor' },
    { key: 'dataLiquidacao', label: 'Data', width: '110px' },
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
    { key: 'numeroLiquidacao', label: 'Nº Liquidação', type: 'text' },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'LIQUIDADA', label: 'Liquidada' },
        { value: 'ANULADA', label: 'Anulada' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/liquidacoes';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllLiquidacoes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: LiquidacaoListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: LiquidacaoListItem): void {
    if (confirm('Tem certeza que deseja excluir esta liquidação?')) {
      this.contabilidadeService.deleteLiquidacao(item.id).subscribe({
        next: () => {
          this.snackBar.open('Liquidação excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
