import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { LdoListItem } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-ldo-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './ldo-list.component.html',
  styleUrl: './ldo-list.component.scss',
})
export class LdoListComponent extends BaseListComponent<LdoListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'status', label: 'Status', width: '120px' },
    { key: 'ppaDescricao', label: 'PPA' },
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
    return '/ldo';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllLdos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: LdoListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: LdoListItem): void {
    if (confirm('Tem certeza que deseja excluir esta LDO?')) {
      this.planejamentoService.deleteLdo(item.id).subscribe({
        next: () => {
          this.snackBar.open('LDO excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
