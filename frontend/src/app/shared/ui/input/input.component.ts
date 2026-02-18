import { Component, computed, forwardRef, input, output, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatIconModule, ReactiveFormsModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
  templateUrl: './input.component.html',
  styleUrl: './input.component.scss',
})
export class InputComponent implements ControlValueAccessor {
  readonly label = input<string>('');
  readonly type = input<string>('text');
  readonly placeholder = input<string>('');
  readonly hint = input<string>('');
  readonly errorMessage = input<string>('');
  readonly prefixIcon = input<string>('');
  readonly suffixIcon = input<string>('');
  readonly appearance = input<'fill' | 'outline'>('fill');
  readonly suffixIconClick = output<void>();

  readonly value = signal<string>('');
  readonly disabled = signal<boolean>(false);

  /** ErrorStateMatcher que ativa o estado de erro quando errorMessage tem valor */
  readonly errorMatcher = computed<ErrorStateMatcher>(() => {
    const hasError = !!this.errorMessage();
    return { isErrorState: () => hasError };
  });

  private onChange: (value: string) => void = () => {};
  onTouched: () => void = () => {};

  writeValue(value: string): void {
    this.value.set(value ?? '');
  }

  registerOnChange(fn: (value: string) => void): void {
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
    this.value.set(val);
    this.onChange(val);
  }

  onSuffixIconClick(): void {
    this.suffixIconClick.emit();
  }
}
