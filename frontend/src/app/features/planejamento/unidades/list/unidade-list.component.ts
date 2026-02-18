import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { OrgaoResponse, UnidadeResponse } from '../../../../core/models/planejamento.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-unidade-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './unidade-list.component.html',
  styleUrl: './unidade-list.component.scss',
})
export class UnidadeListComponent extends BaseListComponent<UnidadeResponse> {
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly orgaos = signal<OrgaoResponse[]>([]);
  readonly filters = signal<Record<string, any>>({});
  readonly filterFields = signal<FilterField[]>([]);

  readonly columns: TableColumn[] = [
    { key: 'codigo', label: 'Código', width: '120px' },
    { key: 'nome', label: 'Nome' },
    { key: 'sigla', label: 'Sigla', width: '100px' },
    { key: 'orgaoNome', label: 'Órgão' },
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
    return '/unidades';
  }

  loadData(): void {
    this.loading.set(true);
    this.planejamentoService.findAllUnidades(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: UnidadeResponse): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: UnidadeResponse): void {
    if (confirm('Tem certeza que deseja excluir esta unidade?')) {
      this.planejamentoService.deleteUnidade(item.id).subscribe({
        next: () => {
          this.snackBar.open('Unidade excluída com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }

  private loadDomainData(): void {
    this.planejamentoService.getOrgaosSimples().subscribe(orgaos => {
      this.orgaos.set(orgaos);
      this.buildFilterFields();
    });
  }

  private buildFilterFields(): void {
    this.filterFields.set([
      { key: 'nome', label: 'Nome', type: 'text' },
      { key: 'codigo', label: 'Código', type: 'text' },
      {
        key: 'orgaoId',
        label: 'Órgão',
        type: 'select',
        options: this.orgaos().map(o => ({ value: o.id, label: o.nome })),
      },
      {
        key: 'ativo',
        label: 'Status',
        type: 'select',
        options: [
          { value: true, label: 'Ativo' },
          { value: false, label: 'Inativo' },
        ],
      },
    ]);
  }
}
