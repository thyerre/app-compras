import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { EventoFolhaListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-evento-folha-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './evento-folha-list.component.html',
  styleUrl: './evento-folha-list.component.scss',
})
export class EventoFolhaListComponent extends BaseListComponent<EventoFolhaListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'codigo', label: 'Código', width: '100px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'tipo', label: 'Tipo', width: '120px' },
    { key: 'percentual', label: 'Percentual', width: '110px', align: 'right' },
    { key: 'valorFixo', label: 'Valor Fixo', width: '120px', align: 'right' },
    { key: 'ativo', label: 'Ativo', width: '80px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'descricao', label: 'Descrição', type: 'text' },
    { key: 'tipo', label: 'Tipo', type: 'select', options: [
      { value: 'PROVENTO', label: 'Provento' }, { value: 'DESCONTO', label: 'Desconto' },
    ]},
  ]);

  getRouteBase(): string { return '/rh/eventos-folha'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllEventosFolha(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: EventoFolhaListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: EventoFolhaListItem): void {
    if (confirm('Tem certeza que deseja excluir este evento de folha?')) {
      this.rhService.deleteEventoFolha(item.id).subscribe({
        next: () => { this.snackBar.open('Evento de folha excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
