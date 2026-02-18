import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Estado, FornecedorListItem, TipoFornecedor } from '../../../core/models/fornecedor.model';
import { BaseListComponent } from '../../../shared/bases/base-list.component';
import { FornecedorService } from '../../../shared/services/fornecedor.service';
import { ButtonComponent } from '../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-fornecedor-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatDialogModule,
    MatSnackBarModule,
    ButtonComponent,
    FilterComponent,
    DataTableComponent,
  ],
  templateUrl: './fornecedor-list.component.html',
  styleUrl: './fornecedor-list.component.scss',
})
export class FornecedorListComponent extends BaseListComponent<FornecedorListItem> {
  private readonly fornecedorService = inject(FornecedorService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly dialog = inject(MatDialog);

  readonly tipos = signal<TipoFornecedor[]>([]);
  readonly estados = signal<Estado[]>([]);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'cnpjCpf', label: 'CNPJ/CPF', width: '150px' },
    { key: 'razaoSocial', label: 'Razão Social' },
    { key: 'nomeFantasia', label: 'Nome Fantasia' },
    { key: 'tipoFornecedor', label: 'Tipo', width: '140px' },
    { key: 'municipio', label: 'Município', width: '150px' },
    { key: 'estadoSigla', label: 'UF', width: '60px', align: 'center' },
    { key: 'telefone', label: 'Telefone', width: '140px' },
    { key: 'email', label: 'E-mail' },
    {
      key: 'ativo',
      label: 'Status',
      width: '80px',
      align: 'center',
      format: (value: boolean) => value ? 'Ativo' : 'Inativo',
    },
  ];

  readonly filterFields = signal<FilterField[]>([]);

  override ngOnInit(): void {
    this.loadDomainData();
    super.ngOnInit();
  }

  getRouteBase(): string {
    return '/fornecedores';
  }

  loadData(): void {
    this.loading.set(true);
    this.fornecedorService
      .findAll(this.filters(), this.pageIndex(), this.pageSize())
      .subscribe({
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

  onViewItem(item: FornecedorListItem): void {
    this.router.navigate([this.getRouteBase(), item.id, 'detalhe']);
  }

  onEditItem(item: FornecedorListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: FornecedorListItem): void {
    if (confirm('Tem certeza que deseja excluir este fornecedor?')) {
      this.fornecedorService.delete(item.id).subscribe({
        next: () => {
          this.snackBar.open(
            'Fornecedor excluído com sucesso!',
            'Fechar',
            { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
          );
          this.loadData();
        },
      });
    }
  }

  private loadDomainData(): void {
    this.fornecedorService.getTiposFornecedor().subscribe(tipos => {
      this.tipos.set(tipos);
      this.buildFilterFields();
    });
    this.fornecedorService.getEstados().subscribe(estados => {
      this.estados.set(estados);
      this.buildFilterFields();
    });
  }

  private buildFilterFields(): void {
    this.filterFields.set([
      { key: 'razaoSocial', label: 'Razão Social', type: 'text' },
      { key: 'cnpjCpf', label: 'CNPJ/CPF', type: 'text' },
      {
        key: 'tipoFornecedorId',
        label: 'Tipo',
        type: 'select',
        options: this.tipos().map(t => ({ value: t.id, label: t.descricao })),
      },
      {
        key: 'estadoId',
        label: 'Estado',
        type: 'select',
        options: this.estados().map(e => ({ value: e.id, label: `${e.sigla} - ${e.nome}` })),
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
