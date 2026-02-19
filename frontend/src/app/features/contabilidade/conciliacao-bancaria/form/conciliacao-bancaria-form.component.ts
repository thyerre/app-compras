import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ConciliacaoBancariaRequest, ConciliacaoBancariaResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-conciliacao-bancaria-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ReactiveFormsModule,
    MatIconModule,
    MatSnackBarModule,
    ButtonComponent,
    InputComponent,
    SelectComponent,
  ],
  templateUrl: './conciliacao-bancaria-form.component.html',
  styleUrl: './conciliacao-bancaria-form.component.scss',
})
export class ConciliacaoBancariaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'PENDENTE', label: 'Pendente' },
    { value: 'CONCILIADO', label: 'Conciliado' },
    { value: 'DIVERGENTE', label: 'Divergente' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/conciliacao-bancaria';
  }

  initForm(): void {
    this.form = this.fb.group({
      contaBancariaId: [null, Validators.required],
      mesReferencia: [null, [Validators.required, Validators.min(1), Validators.max(12)]],
      anoReferencia: [new Date().getFullYear(), Validators.required],
      saldoExtrato: [0, Validators.required],
      saldoContabil: [0, Validators.required],
      observacoes: [''],
      status: ['PENDENTE'],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findConciliacaoById(id).subscribe({
      next: (entity: ConciliacaoBancariaResponse) => {
        this.form.patchValue({
          contaBancariaId: entity.contaBancariaId,
          mesReferencia: entity.mesReferencia,
          anoReferencia: entity.anoReferencia,
          saldoExtrato: entity.saldoExtrato,
          saldoContabil: entity.saldoContabil,
          observacoes: entity.observacoes ?? '',
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
    const data: ConciliacaoBancariaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateConciliacao(this.entityId()!, data)
      : this.contabilidadeService.createConciliacao(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Conciliação atualizada com sucesso!' : 'Conciliação cadastrada com sucesso!',
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
