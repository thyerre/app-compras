import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { RelatorioRreoListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-rreo-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './rreo-list.component.html',
  styleUrl: './rreo-list.component.scss',
})
export class RreoListComponent extends BaseListComponent<RelatorioRreoListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'bimestre', label: 'Bimestre', width: '100px' },
    {
      key: 'receitaRealizada',
      label: 'Receita Realizada',
      width: '160px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'despesaEmpenhada',
      label: 'Despesa Empenhada',
      width: '160px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    {
      key: 'receitaCorrenteLiquida',
      label: 'RCL',
      width: '140px',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'exercicio', label: 'Exercício', type: 'text' },
    {
      key: 'bimestre',
      label: 'Bimestre',
      type: 'select',
      options: [
        { value: '1', label: '1º Bimestre' },
        { value: '2', label: '2º Bimestre' },
        { value: '3', label: '3º Bimestre' },
        { value: '4', label: '4º Bimestre' },
        { value: '5', label: '5º Bimestre' },
        { value: '6', label: '6º Bimestre' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/rreo';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllRreo(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: RelatorioRreoListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: RelatorioRreoListItem): void {
    if (confirm('Tem certeza que deseja excluir este RREO?')) {
      this.contabilidadeService.deleteRreo(item.id).subscribe({
        next: () => {
          this.snackBar.open('RREO excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
