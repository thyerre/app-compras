import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { AcaoListItem, Funcao } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-acao-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './acao-list.component.html',
  styleUrl: './acao-list.component.scss',
})
export class AcaoListComponent extends BaseListComponent<AcaoListItem> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly funcoes = signal<Funcao[]>([]);
  readonly filters = signal<Record<string, any>>({});
  readonly filterFields = signal<FilterField[]>([]);

  readonly columns: TableColumn[] = [
    { key: 'codigo', label: 'Código', width: '100px' },
    { key: 'nome', label: 'Nome' },
    { key: 'tipo', label: 'Tipo', width: '120px' },
    { key: 'funcaoDescricao', label: 'Função' },
    { key: 'programaNome', label: 'Programa' },
    {
      key: 'ativo',
      label: 'Status',
      width: '80px',
      align: 'center',
      format: (value: boolean) => (value ? 'Ativo' : 'Inativo'),
    },
  ];

  override ngOnInit(): void {
    this.loadDomainData();
    super.ngOnInit();
  }

  getRouteBase(): string {
    return '/acoes';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllAcoes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: AcaoListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: AcaoListItem): void {
    if (confirm('Tem certeza que deseja excluir esta ação?')) {
      this.planejamentoService.deleteAcao(item.id).subscribe({
        next: () => {
          this.snackBar.open('Ação excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }

  private loadDomainData(): void {
    this.planejamentoService.getFuncoes().subscribe(data => {
      this.funcoes.set(data);
      this.buildFilterFields();
    });
  }

  private buildFilterFields(): void {
    this.filterFields.set([
      { key: 'nome', label: 'Nome', type: 'text' },
      {
        key: 'tipo',
        label: 'Tipo',
        type: 'select',
        options: [
          { value: 'PROJETO', label: 'Projeto' },
          { value: 'ATIVIDADE', label: 'Atividade' },
          { value: 'OPERACAO_ESPECIAL', label: 'Operação Especial' },
        ],
      },
      {
        key: 'funcaoId',
        label: 'Função',
        type: 'select',
        options: this.funcoes().map(f => ({ value: f.id, label: `${f.codigo} - ${f.descricao}` })),
      },
    ]);
  }
}
