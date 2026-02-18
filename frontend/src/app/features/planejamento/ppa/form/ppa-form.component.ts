import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { PpaRequest, PpaResponse } from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-ppa-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatCheckboxModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './ppa-form.component.html',
  styleUrl: './ppa-form.component.scss',
})
export class PpaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly statusOptions: SelectOption[] = [
    { value: 'ELABORACAO', label: 'Elaboração' },
    { value: 'VIGENTE', label: 'Vigente' },
    { value: 'ENCERRADO', label: 'Encerrado' },
  ];

  getRouteBase(): string {
    return '/ppa';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicioInicio: [null, Validators.required],
      exercicioFim: [null, Validators.required],
      descricao: [''],
      status: ['ELABORACAO'],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findPpaById(id).subscribe({
      next: (p: PpaResponse) => {
        this.form.patchValue({
          exercicioInicio: p.exercicioInicio,
          exercicioFim: p.exercicioFim,
          descricao: p.descricao,
          status: p.status,
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
    const data: PpaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updatePpa(this.entityId()!, data)
      : this.planejamentoService.createPpa(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'PPA atualizado com sucesso!' : 'PPA cadastrado com sucesso!',
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
    }
    return '';
  }
}
