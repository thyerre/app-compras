import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BaixaArrecadacaoListItem } from '../../../../core/models/arrecadacao.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-baixa-arrecadacao-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './baixa-arrecadacao-list.component.html',
  styleUrl: './baixa-arrecadacao-list.component.scss',
})
export class BaixaArrecadacaoListComponent extends BaseListComponent<BaixaArrecadacaoListItem> {
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'guiaNumero', label: 'Nº Guia', width: '120px' },
    { key: 'contribuinteNome', label: 'Contribuinte' },
    { key: 'dataPagamento', label: 'Data Pagamento', width: '120px' },
    { key: 'valorPago', label: 'Valor Pago', width: '140px', align: 'right',
      format: (v: number) => v?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'tipoBaixa', label: 'Tipo', width: '120px' },
    { key: 'agenteDescricao', label: 'Agente Arrecadador' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'contribuinteNome', label: 'Contribuinte', type: 'text' },
    { key: 'tipoBaixa', label: 'Tipo Baixa', type: 'select', options: [
      { value: 'NORMAL', label: 'Normal' }, { value: 'ESTORNO', label: 'Estorno' },
      { value: 'DACAO', label: 'Dação' }, { value: 'COMPENSACAO', label: 'Compensação' },
    ]},
  ]);

  getRouteBase(): string { return '/arrecadacao/baixas'; }

  loadData(): void {
    this.loading.set(true);
    this.arrecadacaoService.findAllBaixas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: BaixaArrecadacaoListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: BaixaArrecadacaoListItem): void {
    if (confirm('Tem certeza que deseja excluir esta baixa?')) {
      this.arrecadacaoService.deleteBaixa(item.id).subscribe({
        next: () => { this.snackBar.open('Baixa excluída!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
