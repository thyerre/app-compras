import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RelatorioRreoRequest, RelatorioRreoResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-rreo-form',
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
  templateUrl: './rreo-form.component.html',
  styleUrl: './rreo-form.component.scss',
})
export class RreoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly bimestreOptions = signal<SelectOption[]>([
    { value: 1, label: '1º Bimestre' },
    { value: 2, label: '2º Bimestre' },
    { value: 3, label: '3º Bimestre' },
    { value: 4, label: '4º Bimestre' },
    { value: 5, label: '5º Bimestre' },
    { value: 6, label: '6º Bimestre' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'RASCUNHO', label: 'Rascunho' },
    { value: 'GERADO', label: 'Gerado' },
    { value: 'REVISADO', label: 'Revisado' },
    { value: 'PUBLICADO', label: 'Publicado' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/rreo';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [new Date().getFullYear(), Validators.required],
      bimestre: [null, [Validators.required, Validators.min(1), Validators.max(6)]],
      receitaPrevista: [0],
      receitaRealizada: [0],
      despesaFixada: [0],
      despesaEmpenhada: [0],
      despesaLiquidada: [0],
      despesaPaga: [0],
      receitaCorrenteLiquida: [0],
      superavitDeficit: [0],
      responsavelNome: [''],
      responsavelCargo: [''],
      contadorNome: [''],
      contadorCrc: [''],
      status: ['RASCUNHO'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findRreoById(id).subscribe({
      next: (entity: RelatorioRreoResponse) => {
        this.form.patchValue({
          exercicio: entity.exercicio,
          bimestre: entity.bimestre,
          receitaPrevista: entity.receitaPrevista ?? 0,
          receitaRealizada: entity.receitaRealizada ?? 0,
          despesaFixada: entity.despesaFixada ?? 0,
          despesaEmpenhada: entity.despesaEmpenhada ?? 0,
          despesaLiquidada: entity.despesaLiquidada ?? 0,
          despesaPaga: entity.despesaPaga ?? 0,
          receitaCorrenteLiquida: entity.receitaCorrenteLiquida ?? 0,
          superavitDeficit: entity.superavitDeficit ?? 0,
          responsavelNome: entity.responsavelNome ?? '',
          responsavelCargo: entity.responsavelCargo ?? '',
          contadorNome: entity.contadorNome ?? '',
          contadorCrc: entity.contadorCrc ?? '',
          status: entity.status ?? 'RASCUNHO',
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
    const data: RelatorioRreoRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateRreo(this.entityId()!, data)
      : this.contabilidadeService.createRreo(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'RREO atualizado com sucesso!' : 'RREO cadastrado com sucesso!',
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
