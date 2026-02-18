import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ProgramaListItem } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-programa-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './programa-list.component.html',
  styleUrl: './programa-list.component.scss',
})
export class ProgramaListComponent extends BaseListComponent<ProgramaListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'codigo', label: 'Código', width: '120px' },
    { key: 'nome', label: 'Nome' },
    { key: 'objetivo', label: 'Objetivo' },
    {
      key: 'ativo',
      label: 'Status',
      width: '80px',
      align: 'center',
      format: (value: boolean) => (value ? 'Ativo' : 'Inativo'),
    },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'nome', label: 'Nome', type: 'text' },
    { key: 'codigo', label: 'Código', type: 'text' },
    {
      key: 'ativo',
      label: 'Status',
      type: 'select',
      options: [
        { value: true, label: 'Ativo' },
        { value: false, label: 'Inativo' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/programas';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllProgramas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: ProgramaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ProgramaListItem): void {
    if (confirm('Tem certeza que deseja excluir este programa?')) {
      this.planejamentoService.deletePrograma(item.id).subscribe({
        next: () => {
          this.snackBar.open('Programa excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
