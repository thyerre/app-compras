import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ReceitaOrcamentariaListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-receita-orcamentaria-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './receita-orcamentaria-list.component.html',
  styleUrl: './receita-orcamentaria-list.component.scss',
})
export class ReceitaOrcamentariaListComponent extends BaseListComponent<ReceitaOrcamentariaListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'codigoReceita', label: 'Código', width: '130px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'categoriaEconomica', label: 'Categoria', width: '130px' },
    { key: 'valorPrevistoInicial', label: 'Previsto', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'valorArrecadado', label: 'Arrecadado', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'valorRecolhido', label: 'Recolhido', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    { key: 'codigoReceita', label: 'Código Receita', type: 'text' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/receita-orcamentaria';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllReceitasOrcamentarias(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: ReceitaOrcamentariaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ReceitaOrcamentariaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta receita?')) {
      this.contabilidadeService.deleteReceitaOrcamentaria(item.id).subscribe({
        next: () => {
          this.snackBar.open('Receita excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
