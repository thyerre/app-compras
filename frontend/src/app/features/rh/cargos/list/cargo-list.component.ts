import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CargoListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-cargo-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './cargo-list.component.html',
  styleUrl: './cargo-list.component.scss',
})
export class CargoListComponent extends BaseListComponent<CargoListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'codigo', label: 'Código', width: '100px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'tipo', label: 'Tipo', width: '140px' },
    { key: 'vagas', label: 'Vagas', width: '80px', align: 'right' },
    { key: 'vagasOcupadas', label: 'Ocupadas', width: '90px', align: 'right' },
    { key: 'cargaHorariaSemanal', label: 'CH Semanal', width: '110px', align: 'right' },
    { key: 'ativo', label: 'Ativo', width: '80px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'descricao', label: 'Descrição', type: 'text' },
    { key: 'tipo', label: 'Tipo', type: 'select', options: [
      { value: 'EFETIVO', label: 'Efetivo' }, { value: 'COMISSIONADO', label: 'Comissionado' },
      { value: 'TEMPORARIO', label: 'Temporário' }, { value: 'ELETIVO', label: 'Eletivo' },
    ]},
  ]);

  getRouteBase(): string { return '/rh/cargos'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllCargos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: CargoListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: CargoListItem): void {
    if (confirm('Tem certeza que deseja excluir este cargo?')) {
      this.rhService.deleteCargo(item.id).subscribe({
        next: () => { this.snackBar.open('Cargo excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
