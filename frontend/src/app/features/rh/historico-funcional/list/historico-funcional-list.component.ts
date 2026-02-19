import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HistoricoFuncionalListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-historico-funcional-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './historico-funcional-list.component.html',
  styleUrl: './historico-funcional-list.component.scss',
})
export class HistoricoFuncionalListComponent extends BaseListComponent<HistoricoFuncionalListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'servidorNome', label: 'Servidor' },
    { key: 'servidorMatricula', label: 'Matrícula', width: '110px' },
    { key: 'tipoEvento', label: 'Tipo de Evento', width: '160px' },
    { key: 'dataEvento', label: 'Data', width: '110px' },
    { key: 'descricao', label: 'Descrição' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'tipoEvento', label: 'Tipo de Evento', type: 'text' },
  ]);

  getRouteBase(): string { return '/rh/historico-funcional'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllHistoricoFuncional(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: HistoricoFuncionalListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: HistoricoFuncionalListItem): void {
    if (confirm('Tem certeza que deseja excluir este registro?')) {
      this.rhService.deleteHistoricoFuncional(item.id).subscribe({
        next: () => { this.snackBar.open('Registro excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
