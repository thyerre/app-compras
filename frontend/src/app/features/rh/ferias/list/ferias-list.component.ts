import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FeriasListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-ferias-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './ferias-list.component.html',
  styleUrl: './ferias-list.component.scss',
})
export class FeriasListComponent extends BaseListComponent<FeriasListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'servidorNome', label: 'Servidor' },
    { key: 'servidorMatricula', label: 'Matrícula', width: '110px' },
    { key: 'periodoAquisitivoInicio', label: 'Aquisitivo Início', width: '140px' },
    { key: 'periodoAquisitivoFim', label: 'Aquisitivo Fim', width: '130px' },
    { key: 'periodoGozoInicio', label: 'Gozo Início', width: '120px' },
    { key: 'periodoGozoFim', label: 'Gozo Fim', width: '120px' },
    { key: 'diasGozo', label: 'Dias', width: '70px', align: 'right' },
    { key: 'status', label: 'Status', width: '110px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'status', label: 'Status', type: 'select', options: [
      { value: 'PENDENTE', label: 'Pendente' }, { value: 'APROVADA', label: 'Aprovada' },
      { value: 'EM_GOZO', label: 'Em Gozo' }, { value: 'CONCLUIDA', label: 'Concluída' },
      { value: 'CANCELADA', label: 'Cancelada' },
    ]},
  ]);

  getRouteBase(): string { return '/rh/ferias'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllFerias(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: FeriasListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: FeriasListItem): void {
    if (confirm('Tem certeza que deseja excluir estas férias?')) {
      this.rhService.deleteFerias(item.id).subscribe({
        next: () => { this.snackBar.open('Férias excluídas!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
