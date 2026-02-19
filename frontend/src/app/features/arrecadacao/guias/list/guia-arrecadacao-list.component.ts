import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { GuiaArrecadacaoListItem } from '../../../../core/models/arrecadacao.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-guia-arrecadacao-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './guia-arrecadacao-list.component.html',
  styleUrl: './guia-arrecadacao-list.component.scss',
})
export class GuiaArrecadacaoListComponent extends BaseListComponent<GuiaArrecadacaoListItem> {
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroGuia', label: 'Nº Guia', width: '120px' },
    { key: 'contribuinteNome', label: 'Contribuinte' },
    { key: 'contribuinteCpfCnpj', label: 'CPF/CNPJ', width: '160px' },
    { key: 'dataEmissao', label: 'Emissão', width: '120px' },
    { key: 'dataVencimento', label: 'Vencimento', width: '120px' },
    { key: 'valorTotal', label: 'Valor', width: '140px', align: 'right',
      format: (v: number) => v?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'status', label: 'Status', width: '100px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'contribuinteNome', label: 'Contribuinte', type: 'text' },
    { key: 'status', label: 'Status', type: 'select', options: [
      { value: 'EMITIDA', label: 'Emitida' }, { value: 'PAGA', label: 'Paga' },
      { value: 'VENCIDA', label: 'Vencida' }, { value: 'CANCELADA', label: 'Cancelada' },
    ]},
  ]);

  getRouteBase(): string { return '/arrecadacao/guias'; }

  loadData(): void {
    this.loading.set(true);
    this.arrecadacaoService.findAllGuias(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: GuiaArrecadacaoListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: GuiaArrecadacaoListItem): void {
    if (confirm('Tem certeza que deseja excluir esta guia?')) {
      this.arrecadacaoService.deleteGuia(item.id).subscribe({
        next: () => { this.snackBar.open('Guia excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
