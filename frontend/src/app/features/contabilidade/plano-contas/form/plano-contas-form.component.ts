import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PlanoContasRequest } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-plano-contas-form',
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
  templateUrl: './plano-contas-form.component.html',
  styleUrl: './plano-contas-form.component.scss',
})
export class PlanoContasFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'S', label: 'Sintética' },
    { value: 'A', label: 'Analítica' },
  ]);

  readonly naturezaOptions = signal<SelectOption[]>([
    { value: '', label: '—' },
    { value: 'D', label: 'Devedora' },
    { value: 'C', label: 'Credora' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/plano-contas';
  }

  initForm(): void {
    this.form = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(30)]],
      descricao: ['', [Validators.required, Validators.maxLength(500)]],
      classe: [1, Validators.required],
      nivel: [1, Validators.required],
      tipo: ['A', Validators.required],
      natureza: [''],
      escrituracao: [''],
      parentId: [null],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findPlanoContasById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          codigo: entity.codigo,
          descricao: entity.descricao,
          classe: entity.classe,
          nivel: entity.nivel,
          tipo: entity.tipo,
          natureza: entity.natureza ?? '',
          escrituracao: entity.escrituracao ?? '',
          parentId: entity.parentId ?? null,
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
    const data: PlanoContasRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updatePlanoContas(this.entityId()!, data)
      : this.contabilidadeService.createPlanoContas(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Conta atualizada com sucesso!' : 'Conta cadastrada com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }
}
