import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { LdoListItem, LoaRequest, LoaResponse } from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-loa-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatCheckboxModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './loa-form.component.html',
  styleUrl: './loa-form.component.scss',
})
export class LoaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly ldos = signal<LdoListItem[]>([]);

  readonly statusOptions: SelectOption[] = [
    { value: 'ELABORACAO', label: 'Elaboração' },
    { value: 'VIGENTE', label: 'Vigente' },
    { value: 'ENCERRADO', label: 'Encerrado' },
  ];

  readonly ldosOptions = computed<SelectOption[]>(() =>
    this.ldos().map(l => ({ value: l.id, label: `${l.exercicio} - ${l.descricao}` }))
  );

  getRouteBase(): string {
    return '/loa';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [null, Validators.required],
      descricao: [''],
      valorTotal: [null],
      status: ['ELABORACAO'],
      ldoId: [null],
    });

    this.loadDomainData();
  }

  private loadDomainData(): void {
    this.planejamentoService.findAllLdos({}, 0, 100).subscribe(page => this.ldos.set(page.content));
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findLoaById(id).subscribe({
      next: (l: LoaResponse) => {
        this.form.patchValue({
          exercicio: l.exercicio,
          descricao: l.descricao,
          valorTotal: l.valorTotal,
          status: l.status,
          ldoId: l.ldoId,
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
    const data: LoaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updateLoa(this.entityId()!, data)
      : this.planejamentoService.createLoa(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'LOA atualizada com sucesso!' : 'LOA cadastrada com sucesso!',
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
