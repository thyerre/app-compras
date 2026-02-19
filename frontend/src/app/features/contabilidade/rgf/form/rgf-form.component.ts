import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RelatorioRgfRequest, RelatorioRgfResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-rgf-form',
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
  templateUrl: './rgf-form.component.html',
  styleUrl: './rgf-form.component.scss',
})
export class RgfFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly quadrimestreOptions = signal<SelectOption[]>([
    { value: 1, label: '1º Quadrimestre' },
    { value: 2, label: '2º Quadrimestre' },
    { value: 3, label: '3º Quadrimestre' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'RASCUNHO', label: 'Rascunho' },
    { value: 'GERADO', label: 'Gerado' },
    { value: 'REVISADO', label: 'Revisado' },
    { value: 'PUBLICADO', label: 'Publicado' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/rgf';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [new Date().getFullYear(), Validators.required],
      quadrimestre: [null, [Validators.required, Validators.min(1), Validators.max(3)]],
      receitaCorrenteLiquida: [0],
      despesaPessoalLegislativo: [0],
      despesaPessoalExecutivo: [0],
      despesaPessoalTotal: [0],
      percentualPessoalLegislativo: [0],
      percentualPessoalExecutivo: [0],
      percentualPessoalTotal: [0],
      limitePrudencial: [0],
      limiteMaximo: [0],
      limiteAlerta: [0],
      dividaConsolidadaLiquida: [0],
      operacoesCreditoInternas: [0],
      operacoesCreditoExternas: [0],
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
    this.contabilidadeService.findRgfById(id).subscribe({
      next: (entity: RelatorioRgfResponse) => {
        this.form.patchValue({
          exercicio: entity.exercicio,
          quadrimestre: entity.quadrimestre,
          receitaCorrenteLiquida: entity.receitaCorrenteLiquida ?? 0,
          despesaPessoalLegislativo: entity.despesaPessoalLegislativo ?? 0,
          despesaPessoalExecutivo: entity.despesaPessoalExecutivo ?? 0,
          despesaPessoalTotal: entity.despesaPessoalTotal ?? 0,
          percentualPessoalLegislativo: entity.percentualPessoalLegislativo ?? 0,
          percentualPessoalExecutivo: entity.percentualPessoalExecutivo ?? 0,
          percentualPessoalTotal: entity.percentualPessoalTotal ?? 0,
          limitePrudencial: entity.limitePrudencial ?? 0,
          limiteMaximo: entity.limiteMaximo ?? 0,
          limiteAlerta: entity.limiteAlerta ?? 0,
          dividaConsolidadaLiquida: entity.dividaConsolidadaLiquida ?? 0,
          operacoesCreditoInternas: entity.operacoesCreditoInternas ?? 0,
          operacoesCreditoExternas: entity.operacoesCreditoExternas ?? 0,
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
    const data: RelatorioRgfRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateRgf(this.entityId()!, data)
      : this.contabilidadeService.createRgf(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'RGF atualizado com sucesso!' : 'RGF cadastrado com sucesso!',
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
