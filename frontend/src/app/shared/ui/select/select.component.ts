import { Component, computed, forwardRef, input, output, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

export interface SelectOption {
  value: unknown;
  label: string;
}

@Component({
  selector: 'app-select',
  standalone: true,
  imports: [MatFormFieldModule, MatSelectModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
  templateUrl: './select.component.html',
  styleUrl: './select.component.scss',
})
export class SelectComponent implements ControlValueAccessor {
  readonly label = input<string>('');
  readonly placeholder = input<string>('');
  readonly options = input<SelectOption[]>([]);
  readonly errorMessage = input<string>('');
  readonly appearance = input<'fill' | 'outline'>('fill');
  readonly valueChange = output<unknown>();

  readonly value = signal<unknown>(null);
  readonly disabled = signal<boolean>(false);

  /** ErrorStateMatcher que ativa o estado de erro quando errorMessage tem valor */
  readonly errorMatcher = computed<ErrorStateMatcher>(() => {
    const hasError = !!this.errorMessage();
    return { isErrorState: () => hasError };
  });

  private onChange: (value: unknown) => void = () => {};
  onTouched: () => void = () => {};

  writeValue(value: unknown): void {
    this.value.set(value);
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

  onSelectionChange(value: unknown): void {
    this.value.set(value);
    this.onChange(value);
    this.valueChange.emit(value);
  }
}
