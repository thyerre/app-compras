import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ReceitaOrcamentariaRequest, ReceitaOrcamentariaResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-receita-orcamentaria-form',
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
  templateUrl: './receita-orcamentaria-form.component.html',
  styleUrl: './receita-orcamentaria-form.component.scss',
})
export class ReceitaOrcamentariaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly categoriaEconomicaOptions = signal<SelectOption[]>([
    { value: 'CORRENTE', label: 'Corrente' },
    { value: 'CAPITAL', label: 'Capital' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/receita-orcamentaria';
  }

  initForm(): void {
    this.form = this.fb.group({
      loaId: [null, Validators.required],
      exercicio: [new Date().getFullYear(), Validators.required],
      codigoReceita: ['', [Validators.required, Validators.maxLength(30)]],
      descricao: ['', [Validators.required, Validators.maxLength(500)]],
      categoriaEconomica: ['', Validators.required],
      origem: [''],
      especie: [''],
      fonteRecursoId: [null],
      valorPrevistoInicial: [0],
      valorPrevistoAtualizado: [0],
      valorLancado: [0],
      valorArrecadado: [0],
      valorRecolhido: [0],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findReceitaOrcamentariaById(id).subscribe({
      next: (entity: ReceitaOrcamentariaResponse) => {
        this.form.patchValue({
          loaId: entity.loaId,
          exercicio: entity.exercicio,
          codigoReceita: entity.codigoReceita,
          descricao: entity.descricao,
          categoriaEconomica: entity.categoriaEconomica,
          origem: entity.origem ?? '',
          especie: entity.especie ?? '',
          fonteRecursoId: entity.fonteRecursoId ?? null,
          valorPrevistoInicial: entity.valorPrevistoInicial ?? 0,
          valorPrevistoAtualizado: entity.valorPrevistoAtualizado ?? 0,
          valorLancado: entity.valorLancado ?? 0,
          valorArrecadado: entity.valorArrecadado ?? 0,
          valorRecolhido: entity.valorRecolhido ?? 0,
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
    const data: ReceitaOrcamentariaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateReceitaOrcamentaria(this.entityId()!, data)
      : this.contabilidadeService.createReceitaOrcamentaria(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Receita atualizada com sucesso!' : 'Receita cadastrada com sucesso!',
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
