import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DependenteListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-dependente-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './dependente-list.component.html',
  styleUrl: './dependente-list.component.scss',
})
export class DependenteListComponent extends BaseListComponent<DependenteListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'servidorNome', label: 'Servidor' },
    { key: 'servidorMatricula', label: 'Matrícula', width: '110px' },
    { key: 'nome', label: 'Nome' },
    { key: 'cpf', label: 'CPF', width: '140px' },
    { key: 'dataNascimento', label: 'Nascimento', width: '120px' },
    { key: 'parentesco', label: 'Parentesco', width: '120px' },
    { key: 'ativo', label: 'Ativo', width: '80px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'nome', label: 'Nome', type: 'text' },
    { key: 'parentesco', label: 'Parentesco', type: 'text' },
  ]);

  getRouteBase(): string { return '/rh/dependentes'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllDependentes(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: DependenteListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: DependenteListItem): void {
    if (confirm('Tem certeza que deseja excluir este dependente?')) {
      this.rhService.deleteDependente(item.id).subscribe({
        next: () => { this.snackBar.open('Dependente excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
