import { ChangeDetectionStrategy, Component, computed, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ButtonComponent } from '../button/button.component';
import { InputComponent } from '../input/input.component';
import { SelectComponent, SelectOption } from '../select/select.component';
import { FilterField } from './filter.model';

@Component({
  selector: 'app-filter',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    FormsModule,
    MatIconModule,
    ButtonComponent,
    InputComponent,
    SelectComponent,
  ],
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.scss',
})
export class FilterComponent {
  readonly fields = input<FilterField[]>([]);
  readonly search = output<Record<string, any>>();
  readonly clear = output<void>();

  readonly values = signal<Record<string, any>>({});
  readonly expanded = signal(true);

  readonly activeFilterCount = computed(() => {
    const vals = this.values();
    return Object.keys(vals).filter(k => vals[k] !== undefined && vals[k] !== null && vals[k] !== '').length;
  });

  toggleExpanded(): void {
    this.expanded.update(v => !v);
  }

  getAllOptions(field: FilterField): SelectOption[] {
    const all: SelectOption = { value: undefined, label: 'Todos' };
    return [all, ...(field.options || [])];
  }

  onFieldChange(key: string, value: any): void {
    this.values.update(v => ({ ...v, [key]: value }));
  }

  onSearch(): void {
    this.search.emit(this.values());
    this.expanded.set(false);
  }

  onClear(): void {
    this.values.set({});
    this.expanded.set(true);
    this.clear.emit();
  }
}
