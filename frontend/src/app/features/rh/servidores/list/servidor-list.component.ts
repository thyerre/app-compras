import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ServidorListItem } from '../../../../core/models/rh.model';
import { BaseListComponent } from '../../../../shared/bases/base-list.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DataTableComponent } from '../../../../shared/ui/data-table/data-table.component';
import { TableColumn } from '../../../../shared/ui/data-table/data-table.model';
import { FilterComponent } from '../../../../shared/ui/filter/filter.component';
import { FilterField } from '../../../../shared/ui/filter/filter.model';

@Component({
  selector: 'app-servidor-list',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatSnackBarModule, ButtonComponent, FilterComponent, DataTableComponent],
  templateUrl: './servidor-list.component.html',
  styleUrl: './servidor-list.component.scss',
})
export class ServidorListComponent extends BaseListComponent<ServidorListItem> {
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  readonly filters = signal<Record<string, any>>({});

  readonly columns: TableColumn[] = [
    { key: 'matricula', label: 'Matrícula', width: '120px' },
    { key: 'nome', label: 'Nome' },
    { key: 'cpf', label: 'CPF', width: '140px' },
    { key: 'cargoDescricao', label: 'Cargo' },
    { key: 'tipoVinculoDescricao', label: 'Vínculo', width: '140px' },
    { key: 'dataAdmissao', label: 'Admissão', width: '120px' },
    { key: 'situacao', label: 'Situação', width: '110px' },
  ];

  readonly filterFields = signal<FilterField[]>([
    { key: 'nome', label: 'Nome', type: 'text' },
    { key: 'matricula', label: 'Matrícula', type: 'text' },
    {
      key: 'situacao', label: 'Situação', type: 'select', options: [
        { value: 'ATIVO', label: 'Ativo' }, { value: 'AFASTADO', label: 'Afastado' },
        { value: 'CEDIDO', label: 'Cedido' }, { value: 'FERIAS', label: 'Férias' },
        { value: 'LICENCA', label: 'Licença' }, { value: 'DESLIGADO', label: 'Desligado' },
      ],
    },
  ]);

  getRouteBase(): string { return '/rh/servidores'; }

  loadData(): void {
    this.loading.set(true);
    this.rhService.findAllServidores(this.filters(), this.pageIndex(), this.pageSize()).subscribe({
      next: page => { this.items.set(page.content); this.totalElements.set(page.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  protected resetFilters(): void { this.filters.set({}); }
  onFilterSearch(values: Record<string, any>): void { this.filters.set(values); this.onSearch(); }
  onFilterClear(): void { this.onClearFilters(); }
  onEditItem(item: ServidorListItem): void { this.onEdit(item.id); }

  onDeleteItem(item: ServidorListItem): void {
    if (confirm('Tem certeza que deseja excluir este servidor?')) {
      this.rhService.deleteServidor(item.id).subscribe({
        next: () => { this.snackBar.open('Servidor excluído!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.loadData(); },
      });
    }
  }
}
