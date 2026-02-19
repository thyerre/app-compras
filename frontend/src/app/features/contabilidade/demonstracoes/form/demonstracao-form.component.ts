import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DemonstracaoContabilRequest, DemonstracaoContabilResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-demonstracao-form',
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
  templateUrl: './demonstracao-form.component.html',
  styleUrl: './demonstracao-form.component.scss',
})
export class DemonstracaoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'BO', label: 'Balanço Orçamentário' },
    { value: 'BF', label: 'Balanço Financeiro' },
    { value: 'BP', label: 'Balanço Patrimonial' },
    { value: 'DVP', label: 'Dem. Variações Patrimoniais' },
    { value: 'DFC', label: 'Dem. Fluxos de Caixa' },
    { value: 'DMPL', label: 'Dem. Mutações PL' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'RASCUNHO', label: 'Rascunho' },
    { value: 'GERADO', label: 'Gerado' },
    { value: 'REVISADO', label: 'Revisado' },
    { value: 'PUBLICADO', label: 'Publicado' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/demonstracoes';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [new Date().getFullYear(), Validators.required],
      tipo: ['BO', Validators.required],
      periodoInicio: [null, Validators.required],
      periodoFim: [null, Validators.required],
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
    this.contabilidadeService.findDemonstracaoById(id).subscribe({
      next: (entity: DemonstracaoContabilResponse) => {
        this.form.patchValue({
          exercicio: entity.exercicio,
          tipo: entity.tipo,
          periodoInicio: entity.periodoInicio,
          periodoFim: entity.periodoFim,
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
    const data: DemonstracaoContabilRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateDemonstracao(this.entityId()!, data)
      : this.contabilidadeService.createDemonstracao(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Demonstração atualizada com sucesso!' : 'Demonstração cadastrada com sucesso!',
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
