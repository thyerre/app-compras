import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CreditoAdicionalRequest, CreditoAdicionalResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-credito-adicional-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ReactiveFormsModule,
    MatIconModule,
    MatSnackBarModule,
    ButtonComponent,
    InputComponent,
    SelectComponent,
    DatepickerComponent,
  ],
  templateUrl: './credito-adicional-form.component.html',
  styleUrl: './credito-adicional-form.component.scss',
})
export class CreditoAdicionalFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'SUPLEMENTAR', label: 'Suplementar' },
    { value: 'ESPECIAL', label: 'Especial' },
    { value: 'EXTRAORDINARIO', label: 'Extraordinário' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'PENDENTE', label: 'Pendente' },
    { value: 'APROVADO', label: 'Aprovado' },
    { value: 'PUBLICADO', label: 'Publicado' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/creditos-adicionais';
  }

  initForm(): void {
    this.form = this.fb.group({
      dotacaoId: [null, Validators.required],
      exercicio: [new Date().getFullYear(), Validators.required],
      tipo: ['SUPLEMENTAR', Validators.required],
      numeroDecreto: [''],
      dataDecreto: [null],
      numeroLei: [''],
      dataLei: [null],
      valor: [0, [Validators.required, Validators.min(0.01)]],
      fonteAnulacao: [''],
      justificativa: [''],
      status: ['PENDENTE'],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findCreditoAdicionalById(id).subscribe({
      next: (entity: CreditoAdicionalResponse) => {
        this.form.patchValue({
          dotacaoId: entity.dotacaoId,
          exercicio: entity.exercicio,
          tipo: entity.tipo,
          numeroDecreto: entity.numeroDecreto ?? '',
          dataDecreto: entity.dataDecreto ?? null,
          numeroLei: entity.numeroLei ?? '',
          dataLei: entity.dataLei ?? null,
          valor: entity.valor,
          fonteAnulacao: entity.fonteAnulacao ?? '',
          justificativa: entity.justificativa ?? '',
          status: entity.status ?? 'PENDENTE',
        });
        this.loadingData.set(false);
      },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    const data: CreditoAdicionalRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateCreditoAdicional(this.entityId()!, data)
      : this.contabilidadeService.createCreditoAdicional(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Crédito Adicional atualizado com sucesso!' : 'Crédito Adicional cadastrado com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }

  getError(field: string): string {
    const control = this.form.get(field);
    if (control && control.touched && control.errors) {
      if (control.errors['required']) return 'Campo obrigatório';
      if (control.errors['maxlength']) return `Máximo de ${control.errors['maxlength'].requiredLength} caracteres`;
      if (control.errors['min']) return `Valor mínimo: ${control.errors['min'].min}`;
    }
    return '';
  }
}
