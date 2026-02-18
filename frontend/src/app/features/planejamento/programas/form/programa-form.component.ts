import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ProgramaRequest, ProgramaResponse } from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';

@Component({
  selector: 'app-programa-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatCheckboxModule, MatSnackBarModule, ButtonComponent, InputComponent],
  templateUrl: './programa-form.component.html',
  styleUrl: './programa-form.component.scss',
})
export class ProgramaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  getRouteBase(): string {
    return '/programas';
  }

  initForm(): void {
    this.form = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(10)]],
      nome: ['', [Validators.required, Validators.maxLength(200)]],
      objetivo: [''],
      publicoAlvo: [''],
      indicador: [''],
      unidadeMedida: [''],
      metaFisica: [''],
      ativo: [true],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findProgramaById(id).subscribe({
      next: (p: ProgramaResponse) => {
        this.form.patchValue({
          codigo: p.codigo,
          nome: p.nome,
          objetivo: p.objetivo,
          publicoAlvo: p.publicoAlvo,
          indicador: p.indicador,
          unidadeMedida: p.unidadeMedida,
          metaFisica: p.metaFisica,
          ativo: p.ativo,
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
    const data: ProgramaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updatePrograma(this.entityId()!, data)
      : this.planejamentoService.createPrograma(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Programa atualizado com sucesso!' : 'Programa cadastrado com sucesso!',
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
