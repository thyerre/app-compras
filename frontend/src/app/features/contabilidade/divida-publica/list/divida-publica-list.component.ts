import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DividaPublicaListItem } from '../../../../core/models/contabilidade.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-divida-publica-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './divida-publica-list.component.html',
  styleUrl: './divida-publica-list.component.scss',
})
export class DividaPublicaListComponent extends BaseListComponent<DividaPublicaListItem> {
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'tipo', label: 'Tipo', width: '120px' },
    { key: 'credor', label: 'Credor' },
    { key: 'numeroContrato', label: 'Contrato', width: '130px' },
    { key: 'dataContratacao', label: 'Contratação', width: '110px' },
    { key: 'valorOriginal', label: 'Valor Original', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'saldoDevedor', label: 'Saldo Devedor', width: '140px', format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—' },
    { key: 'status', label: 'Status', width: '120px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'tipo', label: 'Tipo', type: 'select', options: [
      { value: 'FUNDADA', label: 'Fundada' },
      { value: 'FLUTUANTE', label: 'Flutuante' },
    ]},
    { key: 'status', label: 'Status', type: 'select', options: [
      { value: 'ATIVA', label: 'Ativa' },
      { value: 'QUITADA', label: 'Quitada' },
      { value: 'RENEGOCIADA', label: 'Renegociada' },
    ]},
  ]);

  getRouteBase(): string {
    return '/contabilidade/divida-publica';
  }

  loadData(): void {
    this.loading.set(true);
    this.contabilidadeService.findAllDividaPublica(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: DividaPublicaListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: DividaPublicaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta dívida?')) {
      this.contabilidadeService.deleteDividaPublica(item.id).subscribe({
        next: () => {
          this.snackBar.open('Dívida excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
