import { Component, forwardRef, input, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

export interface AutocompleteOption {
  value: unknown;
  label: string;
}

@Component({
  selector: 'app-autocomplete',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatAutocompleteModule, ReactiveFormsModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AutocompleteComponent),
      multi: true,
    },
  ],
  templateUrl: './autocomplete.component.html',
  styleUrl: './autocomplete.component.scss',
})
export class AutocompleteComponent implements ControlValueAccessor {
  readonly label = input<string>('');
  readonly placeholder = input<string>('');
  readonly options = input<AutocompleteOption[]>([]);
  readonly errorMessage = input<string>('');
  readonly appearance = input<'fill' | 'outline'>('fill');

  readonly value = signal<unknown>(null);
  readonly displayValue = signal<string>('');
  readonly disabled = signal<boolean>(false);
  readonly filteredOptions = signal<AutocompleteOption[]>([]);

  private onChange: (value: unknown) => void = () => {};
  onTouched: () => void = () => {};

  writeValue(value: unknown): void {
    this.value.set(value);
    const option = this.options().find(o => o.value === value);
    this.displayValue.set(option?.label ?? '');
    this.filteredOptions.set(this.options());
  }

  registerOnChange(fn: (value: unknown) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled.set(isDisabled);
  }

  onInput(event: Event): void {
    const val = (event.target as HTMLInputElement).value;
    this.displayValue.set(val);
    this.filteredOptions.set(
      this.options().filter(o =>
        o.label.toLowerCase().includes(val.toLowerCase())
      )
    );
  }

  onOptionSelected(value: unknown): void {
    this.value.set(value);
    this.onChange(value);
    const option = this.options().find(o => o.value === value);
    this.displayValue.set(option?.label ?? '');
  }

  displayFn(value: unknown): string {
    const option = this.options().find(o => o.value === value);
    return option?.label ?? '';
  }
}
