import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ConsignadoListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-consignado-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './consignado-list.component.html',
  styleUrl: './consignado-list.component.scss',
})
export class ConsignadoListComponent extends BaseListComponent<ConsignadoListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'servidorNome', label: 'Servidor' },
    { key: 'servidorMatricula', label: 'Matrícula', width: '110px' },
    { key: 'consignataria', label: 'Consignatária' },
    { key: 'contrato', label: 'Contrato', width: '120px' },
    { key: 'parcelaAtual', label: 'Parcela', width: '80px', align: 'right' },
    { key: 'parcelaTotal', label: 'Total', width: '70px', align: 'right' },
    { key: 'valorParcela', label: 'Valor Parcela', width: '120px', align: 'right' },
    { key: 'status', label: 'Status', width: '110px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'consignataria', label: 'Consignatária', type: 'text' },
    { key: 'status', label: 'Status', type: 'select', options: [
      { value: 'ATIVO', label: 'Ativo' }, { value: 'QUITADO', label: 'Quitado' },
      { value: 'SUSPENSO', label: 'Suspenso' }, { value: 'CANCELADO', label: 'Cancelado' },
    ]},
  ]);

  getRouteBase(): string { return '/rh/consignados'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllConsignados(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: ConsignadoListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: ConsignadoListItem): void {
    if (confirm('Tem certeza que deseja excluir este consignado?')) {
      this.rhService.deleteConsignado(item.id).subscribe({
        next: () => { this.snackBar.open('Consignado excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
