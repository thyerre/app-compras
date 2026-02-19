import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BemPatrimonialListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-bem-patrimonial-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './bem-patrimonial-list.component.html',
  styleUrl: './bem-patrimonial-list.component.scss',
})
export class BemPatrimonialListComponent extends BaseListComponent<BemPatrimonialListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'numeroPatrimonio', label: 'Nº Patrimônio', width: '140px' },
    { key: 'descricao', label: 'Descrição' },
    { key: 'tipo', label: 'Tipo', width: '120px' },
    { key: 'orgaoNome', label: 'Órgão' },
    { key: 'dataAquisicao', label: 'Aquisição', width: '110px' },
    { key: 'valorAtual', label: 'Valor Atual', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'estadoConservacao', label: 'Conservação', width: '120px' },
    { key: 'situacao', label: 'Situação', width: '100px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'descricao', label: 'Descrição', type: 'text' },
    { key: 'tipo', label: 'Tipo', type: 'select', options: [
      { value: 'MOVEL', label: 'Móvel' },
      { value: 'IMOVEL', label: 'Imóvel' },
      { value: 'INTANGIVEL', label: 'Intangível' },
    ]},
    { key: 'situacao', label: 'Situação', type: 'select', options: [
      { value: 'ATIVO', label: 'Ativo' },
      { value: 'BAIXADO', label: 'Baixado' },
      { value: 'CEDIDO', label: 'Cedido' },
    ]},
  ]);

  getRouteBase(): string {
    return '/contabilidade/bens-patrimoniais';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllBensPatrimoniais(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: BemPatrimonialListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: BemPatrimonialListItem): void {
    if (confirm('Tem certeza que deseja excluir este bem?')) {
      this.contabilidadeService.deleteBemPatrimonial(item.id).subscribe({
        next: () => {
          this.snackBar.open('Bem excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
