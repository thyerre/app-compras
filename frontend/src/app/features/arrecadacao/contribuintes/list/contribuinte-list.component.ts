import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ContribuinteListItem } from '../../../../core/models/arrecadacao.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-contribuinte-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './contribuinte-list.component.html',
  styleUrl: './contribuinte-list.component.scss',
})
export class ContribuinteListComponent extends BaseListComponent<ContribuinteListItem> {
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);

  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'cpfCnpj', label: 'CPF/CNPJ', width: '160px' },
    { key: 'nomeRazaoSocial', label: 'Nome / Razão Social' },
    { key: 'tipoPessoa', label: 'Tipo', width: '80px' },
    { key: 'inscricaoMunicipal', label: 'Insc. Municipal', width: '140px' },
    { key: 'telefone', label: 'Telefone', width: '140px' },
    { key: 'email', label: 'E-mail' },
    { key: 'municipioNome', label: 'Município', width: '150px' },
    { key: 'ativo', label: 'Ativo', width: '80px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'nomeRazaoSocial', label: 'Nome / Razão Social', type: 'text' },
    { key: 'cpfCnpj', label: 'CPF/CNPJ', type: 'text' },
    {
      key: 'tipoPessoa',
      label: 'Tipo Pessoa',
      type: 'select',
      options: [
        { value: 'PF', label: 'Pessoa Física' },
        { value: 'PJ', label: 'Pessoa Jurídica' },
      ],
    },
  ]);

  getRouteBase(): string {
    return '/arrecadacao/contribuintes';
  }

  loadData(): void {
    this.loading.set(true);
    this.arrecadacaoService.findAllContribuintes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
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

  onEditItem(item: ContribuinteListItem): void {
    this.onEdit(item.id);
  }

  onDeleteItem(item: ContribuinteListItem): void {
    if (confirm('Tem certeza que deseja excluir este contribuinte?')) {
      this.arrecadacaoService.deleteContribuinte(item.id).subscribe({
        next: () => {
          this.snackBar.open('Contribuinte excluído com sucesso!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
          this.loadData();
        },
      });
    }
  }
}
