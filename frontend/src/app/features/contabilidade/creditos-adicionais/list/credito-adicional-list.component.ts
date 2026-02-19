import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CreditoAdicionalListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-credito-adicional-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './credito-adicional-list.component.html',
  styleUrl: './credito-adicional-list.component.scss',
})
export class CreditoAdicionalListComponent extends BaseListComponent<CreditoAdicionalListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'tipo', label: 'Tipo', width: '140px' },
    { key: 'numeroDecreto', label: 'Decreto', width: '130px' },
    { key: 'numeroLei', label: 'Lei', width: '130px' },
    { key: 'valor', label: 'Valor', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    { key: 'tipo', label: 'Tipo', type: 'select', options: [
      { value: 'SUPLEMENTAR', label: 'Suplementar' },
      { value: 'ESPECIAL', label: 'Especial' },
      { value: 'EXTRAORDINARIO', label: 'Extraordinário' },
    ]},
  ]);

  getRouteBase(): string {
    return '/contabilidade/creditos-adicionais';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllCreditosAdicionais(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: CreditoAdicionalListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: CreditoAdicionalListItem): void {
    if (confirm('Tem certeza que deseja excluir este crédito?')) {
      this.contabilidadeService.deleteCreditoAdicional(item.id).subscribe({
        next: () => {
          this.snackBar.open('Crédito excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
