import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { RelatorioRgfListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-rgf-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './rgf-list.component.html',
  styleUrl: './rgf-list.component.scss',
})
export class RgfListComponent extends BaseListComponent<RelatorioRgfListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'quadrimestre', label: 'Quadrimestre', width: '120px' },
    {
      key: 'receitaCorrenteLiquida',
      label: 'RCL',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'despesaPessoalTotal',
      label: 'Desp. Pessoal',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'percentualPessoalTotal',
      label: '% Pessoal',
      width: '100px',
      format: (value: number) => value != null ? value.toFixed(2) + '%' : '—',
    },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'quadrimestre',
      label: 'Quadrimestre',
      type: 'select',
      options: [
        { value: '1', label: '1º Quadrimestre' },
        { value: '2', label: '2º Quadrimestre' },
        { value: '3', label: '3º Quadrimestre' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/rgf';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllRgf(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: RelatorioRgfListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: RelatorioRgfListItem): void {
    if (confirm('Tem certeza que deseja excluir este RGF?')) {
      this.contabilidadeService.deleteRgf(item.id).subscribe({
        next: () => {
          this.snackBar.open('RGF excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
