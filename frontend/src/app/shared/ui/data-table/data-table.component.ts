import { ChangeDetectionStrategy, Component, computed, input, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TableColumn } from './data-table.model';

@Component({
  selector: 'app-data-table',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatProgressBarModule,
  ],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.scss',
})
export class DataTableComponent<T = any> {
  readonly columns = input<TableColumn[]>([]);
  readonly data = input<T[]>([]);
  readonly totalElements = input(0);
  readonly pageSize = input(20);
  readonly pageIndex = input(0);
  readonly loading = input(false);
  readonly showActions = input(true);
  readonly showViewAction = input(false);
  readonly pageSizeOptions = input([10, 20, 50, 100]);

  readonly pageChange = output<PageEvent>();
  readonly sortChange = output<Sort>();
  readonly view = output<T>();
  readonly edit = output<T>();
  readonly remove = output<T>();

  readonly displayedColumns = computed(() => {
    const cols = this.columns().map(c => c.key);
    if (this.showActions()) {
      cols.push('actions');
    }
    return cols;
  });

  getCellValue(row: T, column: TableColumn): string {
    const value = (row as any)[column.key];
    if (column.format) {
      return column.format(value, row);
    }
    return value ?? '';
  }

  onPageChange(event: PageEvent): void {
    this.pageChange.emit(event);
  }

  onSortChange(sort: Sort): void {
    this.sortChange.emit(sort);
  }

  onView(row: T): void {
    this.view.emit(row);
  }

  onEdit(row: T): void {
    this.edit.emit(row);
  }

  onRemove(row: T): void {
    this.remove.emit(row);
  }
}
