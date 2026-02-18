import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { LoaListItem } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-loa-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './loa-list.component.html',
  styleUrl: './loa-list.component.scss',
})
export class LoaListComponent extends BaseListComponent<LoaListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'descricao', label: 'Descrição' },
    {
      key: 'valorTotal',
      label: 'Valor Total',
      width: '150px',
      align: 'right',
      format: (value: number) => value != null ? value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) : '',
    },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'ELABORACAO', label: 'Elaboração' },
        { value: 'VIGENTE', label: 'Vigente' },
        { value: 'ENCERRADO', label: 'Encerrado' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/loa';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllLoas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: LoaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: LoaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta LOA?')) {
      this.planejamentoService.deleteLoa(item.id).subscribe({
        next: () => {
          this.snackBar.open('LOA excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
