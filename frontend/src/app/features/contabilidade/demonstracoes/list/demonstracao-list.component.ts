import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { DemonstracaoContabilListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-demonstracao-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './demonstracao-list.component.html',
  styleUrl: './demonstracao-list.component.scss',
})
export class DemonstracaoListComponent extends BaseListComponent<DemonstracaoContabilListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'tipo', label: 'Tipo', width: '80px' },
    { key: 'periodoInicio', label: 'Início', width: '110px' },
    { key: 'periodoFim', label: 'Fim', width: '110px' },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'tipo',
      label: 'Tipo',
      type: 'select',
      options: [
        { value: 'BO', label: 'Balanço Orçamentário' },
        { value: 'BF', label: 'Balanço Financeiro' },
        { value: 'BP', label: 'Balanço Patrimonial' },
        { value: 'DVP', label: 'Dem. Variações Patrimoniais' },
        { value: 'DFC', label: 'Dem. Fluxos de Caixa' },
        { value: 'DMPL', label: 'Dem. Mutações PL' },
      ],
    },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'RASCUNHO', label: 'Rascunho' },
        { value: 'GERADO', label: 'Gerado' },
        { value: 'REVISADO', label: 'Revisado' },
        { value: 'PUBLICADO', label: 'Publicado' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/demonstracoes';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllDemonstracoes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: DemonstracaoContabilListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: DemonstracaoContabilListItem): void {
    if (confirm('Tem certeza que deseja excluir esta demonstração?')) {
      this.contabilidadeService.deleteDemonstracao(item.id).subscribe({
        next: () => {
          this.snackBar.open('Demonstração excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
