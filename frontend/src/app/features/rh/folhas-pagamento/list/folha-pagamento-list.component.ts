import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FolhaPagamentoListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-folha-pagamento-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './folha-pagamento-list.component.html',
  styleUrl: './folha-pagamento-list.component.scss',
})
export class FolhaPagamentoListComponent extends BaseListComponent<FolhaPagamentoListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'competencia', label: 'Competência', width: '120px' },
    { key: 'tipo', label: 'Tipo', width: '140px' },
    { key: 'totalProventos', label: 'Total Proventos', width: '140px', align: 'right' },
    { key: 'totalDescontos', label: 'Total Descontos', width: '140px', align: 'right' },
    { key: 'totalLiquido', label: 'Total Líquido', width: '140px', align: 'right' },
    { key: 'quantidadeServidores', label: 'Servidores', width: '110px', align: 'right' },
    { key: 'status', label: 'Status', width: '110px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'competencia', label: 'Competência', type: 'text' },
    {
      key: 'tipo', label: 'Tipo', type: 'select', options: [
        { value: 'NORMAL', label: 'Normal' }, { value: 'COMPLEMENTAR', label: 'Complementar' },
        { value: 'DECIMO_TERCEIRO', label: '13º Salário' }, { value: 'FERIAS', label: 'Férias' },
        { value: 'RESCISAO', label: 'Rescisão' },
      ],
    },
    {
      key: 'status', label: 'Status', type: 'select', options: [
        { value: 'ABERTA', label: 'Aberta' }, { value: 'CALCULADA', label: 'Calculada' },
        { value: 'CONFERIDA', label: 'Conferida' }, { value: 'FECHADA', label: 'Fechada' },
        { value: 'PAGA', label: 'Paga' },
      ],
    },
  ]);

  getRouteBase(): string { return '/rh/folhas-pagamento'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllFolhas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: FolhaPagamentoListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: FolhaPagamentoListItem): void {
    if (confirm('Tem certeza que deseja excluir esta folha de pagamento?')) {
      this.rhService.deleteFolha(item.id).subscribe({
        next: () => { this.snackBar.open('Folha excluída!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
