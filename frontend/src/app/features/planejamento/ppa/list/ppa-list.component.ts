import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { PpaListItem } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-ppa-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './ppa-list.component.html',
  styleUrl: './ppa-list.component.scss',
})
export class PpaListComponent extends BaseListComponent<PpaListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicioInicio', label: 'Exercício Início', width: '120px' },
    { key: 'exercicioFim', label: 'Exercício Fim', width: '120px' },
    { key: 'descricao', label: 'Descrição' },
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
    return '/ppa';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllPpas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: PpaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: PpaListItem): void {
    if (confirm('Tem certeza que deseja excluir este PPA?')) {
      this.planejamentoService.deletePpa(item.id).subscribe({
        next: () => {
          this.snackBar.open('PPA excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
