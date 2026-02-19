import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ContaBancariaListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-conta-bancaria-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './conta-bancaria-list.component.html',
  styleUrl: './conta-bancaria-list.component.scss',
})
export class ContaBancariaListComponent extends BaseListComponent<ContaBancariaListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'bancoCodigo', label: 'Cód. Banco', width: '100px' },
    { key: 'bancoNome', label: 'Banco' },
    { key: 'agencia', label: 'Agência', width: '100px' },
    { key: 'numeroConta', label: 'Conta', width: '120px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'tipo', label: 'Tipo', width: '120px' },
    {
      key: 'saldoAtual',
      label: 'Saldo',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'ativo', label: 'Ativo', width: '80px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'descricao', label: 'Descrição', type: 'text' },
    {
      key: 'tipo',
      label: 'Tipo',
      type: 'select',
      options: [
        { value: 'MOVIMENTO', label: 'Movimento' },
        { value: 'VINCULADA', label: 'Vinculada' },
        { value: 'APLICACAO', label: 'Aplicação' },
        { value: 'ESPECIAL', label: 'Especial' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/contabilidade/contas-bancarias';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllContasBancarias(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: ContaBancariaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ContaBancariaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta conta bancária?')) {
      this.contabilidadeService.deleteContaBancaria(item.id).subscribe({
        next: () => {
          this.snackBar.open('Conta bancária excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
