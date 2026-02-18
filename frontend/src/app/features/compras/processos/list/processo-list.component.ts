import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ModalidadeLicitacao, ProcessoCompraListItem, StatusProcesso } from '../../../../core/models/compras.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ComprasService } from '../../../../shared/services/compras.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-processo-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './processo-list.component.html',
  styleUrl: './processo-list.component.scss',
})
export class ProcessoListComponent extends BaseListComponent<ProcessoCompraListItem> {
  private readonly comprasService = inject(ComprasService);
  private readonly snackBar = inject(MatSnackBar);

  readonly modalidades = signal<ModalidadeLicitacao[]>([]);
  readonly statusList = signal<StatusProcesso[]>([]);
  readonly filters = signal<Record<string, any>>({});
  readonly filterFields = signal<FilterField[]>([]);

  readonly columns: TableColumn[] = [
    { key: 'numeroProcesso', label: 'Nº Processo', width: '140px' },
    { key: 'exercicio', label: 'Exercício', width: '100px' },
    { key: 'objeto', label: 'Objeto' },
    { key: 'modalidadeNome', label: 'Modalidade', width: '160px' },
    { key: 'orgaoNome', label: 'Órgão' },
    {
      key: 'valorEstimado',
      label: 'Valor Estimado',
      width: '140px',
      align: 'right',
      format: (value: number) => value?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '—',
    },
    { key: 'statusNome', label: 'Status', width: '140px' },
    { key: 'dataAbertura', label: 'Abertura', width: '110px' },
  ];

  override ngOnInit(): void {
    this.loadDomainData();
    super.ngOnInit();
  }

  getRouteBase(): string {
    return '/processos';
  }

  loadData(): void {
    this.loading.set(true);
    this.comprasService.findAllProcessos(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onViewItem(item: ProcessoCompraListItem): void {
    this.router.navigate([this.getRouteBase(), item.id, 'detalhe']);
  }

  onEditItem(item: ProcessoCompraListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ProcessoCompraListItem): void {
    if (confirm('Tem certeza que deseja excluir este processo?')) {
      this.comprasService.deleteProcesso(item.id).subscribe({
        next: () => {
          this.snackBar.open('Processo excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }

  private loadDomainData(): void {
    this.comprasService.getModalidades().subscribe(data => {
      this.modalidades.set(data);
      this.buildFilterFields();
    });
    this.comprasService.getStatusProcesso().subscribe(data => {
      this.statusList.set(data);
      this.buildFilterFields();
    });
  }

  private buildFilterFields(): void {
    this.filterFields.set([
      { key: 'numeroProcesso', label: 'Nº Processo', type: 'text' },
      { key: 'exercicio', label: 'Exercício', type: 'text' },
      {
        key: 'modalidadeId',
        label: 'Modalidade',
        type: 'select',
        options: this.modalidades().map(m => ({ value: m.id, label: m.nome })),
      },
      {
        key: 'statusId',
        label: 'Status',
        type: 'select',
        options: this.statusList().map(s => ({ value: s.id, label: s.nome })),
      },
    ]);
  }
}
