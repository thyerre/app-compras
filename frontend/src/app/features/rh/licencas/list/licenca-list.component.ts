import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LicencaListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-licenca-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './licenca-list.component.html',
  styleUrl: './licenca-list.component.scss',
})
export class LicencaListComponent extends BaseListComponent<LicencaListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'servidorNome', label: 'Servidor' },
    { key: 'servidorMatricula', label: 'Matrícula', width: '110px' },
    { key: 'tipoLicenca', label: 'Tipo', width: '160px' },
    { key: 'dataInicio', label: 'Início', width: '110px' },
    { key: 'dataFim', label: 'Fim', width: '110px' },
    { key: 'quantidadeDias', label: 'Dias', width: '70px', align: 'right' },
    { key: 'remunerada', label: 'Remunerada', width: '110px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'tipoLicenca', label: 'Tipo de Licença', type: 'text' },
  ]);

  getRouteBase(): string { return '/rh/licencas'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllLicencas(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: LicencaListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: LicencaListItem): void {
    if (confirm('Tem certeza que deseja excluir esta licença?')) {
      this.rhService.deleteLicenca(item.id).subscribe({
        next: () => { this.snackBar.open('Licença excluída!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
