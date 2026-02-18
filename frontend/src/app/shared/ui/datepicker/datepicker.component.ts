import { Component, computed, forwardRef, input, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ErrorStateMatcher, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-datepicker',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatDatepickerModule, MatNativeDateModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DatepickerComponent),
      multi: true,
    },
  ],
  templateUrl: './datepicker.component.html',
  styleUrl: './datepicker.component.scss',
})
export class DatepickerComponent implements ControlValueAccessor {
  readonly label = input<string>('');
  readonly placeholder = input<string>('');
  readonly errorMessage = input<string>('');
  readonly appearance = input<'fill' | 'outline'>('fill');
  readonly minDate = input<Date | null>(null);
  readonly maxDate = input<Date | null>(null);

  readonly value = signal<Date | null>(null);
  readonly disabled = signal<boolean>(false);

  readonly errorMatcher = computed<ErrorStateMatcher>(() => {
    const hasError = !!this.errorMessage();
    return { isErrorState: () => hasError };
  });

  private onChange: (value: Date | null) => void = () => {};
  onTouched: () => void = () => {};

  writeValue(value: Date | null): void {
    this.value.set(value ?? null);
  }

  registerOnChange(fn: (value: Date | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled.set(isDisabled);
  }

  onDateChange(value: Date | null): void {
    this.value.set(value);
    this.onChange(value);
  }
}
