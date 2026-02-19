import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { PlanoContasListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-plano-contas-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './plano-contas-list.component.html',
  styleUrl: './plano-contas-list.component.scss',
})
export class PlanoContasListComponent extends BaseListComponent<PlanoContasListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'codigo', label: 'Código', width: '150px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'classe', label: 'Classe', width: '80px' },
    { key: 'nivel', label: 'Nível', width: '80px' },
    { key: 'tipo', label: 'Tipo', width: '80px' },
    { key: 'natureza', label: 'Natureza', width: '100px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'codigo', label: 'Código', type: 'text' },
    { key: 'descricao', label: 'Descrição', type: 'text' },
    {
      key: 'tipo',
      label: 'Tipo',
      type: 'select',
      options: [
        { value: 'S', label: 'Sintética' },
        { value: 'A', label: 'Analítica' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/plano-contas';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllPlanoContas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: PlanoContasListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: PlanoContasListItem): void {
    if (confirm('Tem certeza que deseja excluir esta conta?')) {
      this.contabilidadeService.deletePlanoContas(item.id).subscribe({
        next: () => {
          this.snackBar.open('Conta excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
