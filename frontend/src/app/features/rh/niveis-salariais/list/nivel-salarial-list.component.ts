import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { NivelSalarialListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-nivel-salarial-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './nivel-salarial-list.component.html',
  styleUrl: './nivel-salarial-list.component.scss',
})
export class NivelSalarialListComponent extends BaseListComponent<NivelSalarialListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'cargoDescricao', label: 'Cargo' },
    { key: 'nivel', label: 'Nível', width: '100px' },
    { key: 'classe', label: 'Classe', width: '100px' },
    { key: 'referencia', label: 'Referência', width: '110px' },
    { key: 'valorBase', label: 'Valor Base', width: '130px', align: 'right' },
    { key: 'vigenciaInicio', label: 'Vigência Início', width: '140px' },
    { key: 'vigenciaFim', label: 'Vigência Fim', width: '130px' },
    { key: 'ativo', label: 'Ativo', width: '80px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'nivel', label: 'Nível', type: 'text' },
  ]);

  getRouteBase(): string { return '/rh/niveis-salariais'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllNiveisSalariais(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: NivelSalarialListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: NivelSalarialListItem): void {
    if (confirm('Tem certeza que deseja excluir este nível salarial?')) {
      this.rhService.deleteNivelSalarial(item.id).subscribe({
        next: () => { this.snackBar.open('Nível salarial excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
