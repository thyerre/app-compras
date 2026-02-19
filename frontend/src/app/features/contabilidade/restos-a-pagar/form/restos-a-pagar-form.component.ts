import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RestosAPagarRequest, RestosAPagarResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-restos-a-pagar-form',
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
  templateUrl: './restos-a-pagar-form.component.html',
  styleUrl: './restos-a-pagar-form.component.scss',
})
export class RestosAPagarFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'PROCESSADO', label: 'Processado' },
    { value: 'NAO_PROCESSADO', label: 'Não Processado' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'INSCRITO', label: 'Inscrito' },
    { value: 'LIQUIDADO', label: 'Liquidado' },
    { value: 'PAGO', label: 'Pago' },
    { value: 'CANCELADO', label: 'Cancelado' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/restos-a-pagar';
  }

  initForm(): void {
    this.form = this.fb.group({
      empenhoId: [null, Validators.required],
      exercicioOrigem: [new Date().getFullYear() - 1, Validators.required],
      tipo: ['PROCESSADO', Validators.required],
      dataInscricao: [null, Validators.required],
      valorInscrito: [0, [Validators.required, Validators.min(0.01)]],
      valorCancelado: [0],
      valorPago: [0],
      valorALiquidar: [0],
      status: ['INSCRITO'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findRestosAPagarById(id).subscribe({
      next: (entity: RestosAPagarResponse) => {
        this.form.patchValue({
          empenhoId: entity.empenhoId,
          exercicioOrigem: entity.exercicioOrigem,
          tipo: entity.tipo,
          dataInscricao: entity.dataInscricao,
          valorInscrito: entity.valorInscrito,
          valorCancelado: entity.valorCancelado ?? 0,
          valorPago: entity.valorPago ?? 0,
          valorALiquidar: entity.valorALiquidar ?? 0,
          status: entity.status ?? 'INSCRITO',
          observacoes: entity.observacoes ?? '',
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
    const data: RestosAPagarRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateRestosAPagar(this.entityId()!, data)
      : this.contabilidadeService.createRestosAPagar(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Restos a Pagar atualizado com sucesso!' : 'Restos a Pagar cadastrado com sucesso!',
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
