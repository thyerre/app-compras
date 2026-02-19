import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RestosAPagarListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-restos-a-pagar-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './restos-a-pagar-list.component.html',
  styleUrl: './restos-a-pagar-list.component.scss',
})
export class RestosAPagarListComponent extends BaseListComponent<RestosAPagarListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicioOrigem', label: 'Exercício', width: '100px' },
    { key: 'numeroEmpenho', label: 'Empenho', width: '130px' },
    { key: 'fornecedorNome', label: 'Fornecedor' },
    { key: 'tipo', label: 'Tipo', width: '160px' },
    { key: 'valorInscrito', label: 'Inscrito', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'valorPago', label: 'Pago', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'dataInscricao', label: 'Inscrição', width: '110px' },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicioOrigem', label: 'Exercício Origem', type: 'text' },
    { key: 'tipo', label: 'Tipo', type: 'select', options: [
      { value: 'PROCESSADO', label: 'Processado' },
      { value: 'NAO_PROCESSADO', label: 'Não Processado' },
    ]},
    { key: 'status', label: 'Status', type: 'select', options: [
      { value: 'INSCRITO', label: 'Inscrito' },
      { value: 'LIQUIDADO', label: 'Liquidado' },
      { value: 'PAGO', label: 'Pago' },
      { value: 'CANCELADO', label: 'Cancelado' },
    ]},
  ]);

  getRouteBase(): string {
    return '/contabilidade/restos-a-pagar';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllRestosAPagar(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: RestosAPagarListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: RestosAPagarListItem): void {
    if (confirm('Tem certeza que deseja excluir este RP?')) {
      this.contabilidadeService.deleteRestosAPagar(item.id).subscribe({
        next: () => {
          this.snackBar.open('RP excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
