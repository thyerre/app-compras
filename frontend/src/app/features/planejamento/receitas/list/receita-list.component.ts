import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ReceitaPrevistaListItem } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-receita-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './receita-list.component.html',
  styleUrl: './receita-list.component.scss',
})
export class ReceitaListComponent extends BaseListComponent<ReceitaPrevistaListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'codigoReceita', label: 'Código', width: '120px' },
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'categoria', label: 'Categoria', width: '140px' },
    { key: 'fonteRecursoDescricao', label: 'Fonte Recurso' },
    {
      key: 'valorPrevisto',
      label: 'Valor Previsto',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'valorArrecadado',
      label: 'Arrecadado',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    { key: 'descricao', label: 'Descrição', type: 'text' },
  ]);

  getRouteBase(): string {
    return '/receitas';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllReceitas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: ReceitaPrevistaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ReceitaPrevistaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta receita?')) {
      this.planejamentoService.deleteReceita(item.id).subscribe({
        next: () => {
          this.snackBar.open('Receita excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
