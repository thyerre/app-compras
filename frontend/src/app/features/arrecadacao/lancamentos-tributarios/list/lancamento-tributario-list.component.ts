import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LancamentoTributarioListItem } from '../../../../core/models/arrecadacao.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-lancamento-tributario-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './lancamento-tributario-list.component.html',
  styleUrl: './lancamento-tributario-list.component.scss',
})
export class LancamentoTributarioListComponent extends BaseListComponent<LancamentoTributarioListItem> {
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroLancamento', label: 'Nº Lanç.', width: '120px' },
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'contribuinteNome', label: 'Contribuinte' },
    { key: 'tributoCodigo', label: 'Tributo', width: '100px' },
    { key: 'dataLancamento', label: 'Data Lanç.', width: '120px' },
    { key: 'dataVencimento', label: 'Vencimento', width: '120px' },
    {
      key: 'valorTotal',
      label: 'Valor Total',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'status', label: 'Status', width: '100px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'contribuinteNome', label: 'Contribuinte', type: 'text' },
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'ABERTO', label: 'Aberto' },
        { value: 'PAGO', label: 'Pago' },
        { value: 'CANCELADO', label: 'Cancelado' },
        { value: 'INSCRITO_DIVIDA', label: 'Inscrito em Dívida' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/arrecadacao/lancamentos-tributarios';
  }

  loadData(): void {
    this.loading.set(true);
    this.arrecadacaoService.findAllLancamentos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => {
        this.items.set(page.content);
        this.totalElements.set(page.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }

  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: LancamentoTributarioListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: LancamentoTributarioListItem): void {
    if (confirm('Tem certeza que deseja excluir este lançamento tributário?')) {
      this.arrecadacaoService.deleteLancamento(item.id).subscribe({
        next: () => {
          this.snackBar.open('Lançamento excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
