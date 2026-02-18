import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { FonteRecurso, ReceitaPrevistaRequest, ReceitaPrevistaResponse } from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-receita-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './receita-form.component.html',
  styleUrl: './receita-form.component.scss',
})
export class ReceitaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly fontes = signal<FonteRecurso[]>([]);
  readonly loas = signal<{ id: number; exercicio: number; descricao: string }[]>([]);

  readonly fontesOptions = computed<SelectOption[]>(() =>
    this.fontes().map(f => ({ value: f.id, label: `${f.codigo} - ${f.descricao}` }))
  );

  readonly loasOptions = computed<SelectOption[]>(() =>
    this.loas().map(l => ({ value: l.id, label: `${l.exercicio} - ${l.descricao}` }))
  );

  readonly categoriasOptions: SelectOption[] = [
    { value: 'RECEITA_CORRENTE', label: 'Receita Corrente' },
    { value: 'RECEITA_CAPITAL', label: 'Receita de Capital' },
    { value: 'TRANSFERENCIA', label: 'Transferência' },
    { value: 'OUTRAS', label: 'Outras' },
  ];

  getRouteBase(): string {
    return '/receitas';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [new Date().getFullYear(), [Validators.required]],
      codigoReceita: ['', [Validators.required, Validators.maxLength(20)]],
      descricao: ['', [Validators.required, Validators.maxLength(500)]],
      categoria: [''],
      fonteRecursoId: [null],
      valorPrevisto: [0, Validators.required],
      valorArrecadado: [0],
      loaId: [null, Validators.required],
    });

    this.planejamentoService.getFontesRecurso().subscribe(data => this.fontes.set(data));
    this.planejamentoService.getLoasSimples().subscribe(data => this.loas.set(data));
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findReceitaById(id).subscribe({
      next: (r: ReceitaPrevistaResponse) => {
        this.form.patchValue({
          exercicio: r.exercicio,
          codigoReceita: r.codigoReceita,
          descricao: r.descricao,
          categoria: r.categoria,
          fonteRecursoId: r.fonteRecursoId,
          valorPrevisto: r.valorPrevisto,
          valorArrecadado: r.valorArrecadado,
          loaId: r.loaId,
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
    const data: ReceitaPrevistaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updateReceita(this.entityId()!, data)
      : this.planejamentoService.createReceita(data);

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
    }
    return '';
  }
}
