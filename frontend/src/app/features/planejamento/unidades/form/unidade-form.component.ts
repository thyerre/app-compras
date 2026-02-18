import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { OrgaoResponse, UnidadeRequest, UnidadeResponse } from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-unidade-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatCheckboxModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './unidade-form.component.html',
  styleUrl: './unidade-form.component.scss',
})
export class UnidadeFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly orgaos = signal<OrgaoResponse[]>([]);

  readonly orgaosOptions = computed<SelectOption[]>(() =>
    this.orgaos().map(o => ({ value: o.id, label: `${o.codigo} - ${o.nome}` }))
  );

  getRouteBase(): string {
    return '/unidades';
  }

  initForm(): void {
    this.form = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(10)]],
      nome: ['', [Validators.required, Validators.maxLength(200)]],
      sigla: ['', Validators.maxLength(20)],
      orgaoId: [null, Validators.required],
      ativo: [true],
    });

    this.planejamentoService.getOrgaosSimples().subscribe(data => this.orgaos.set(data));
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findUnidadeById(id).subscribe({
      next: (unidade: UnidadeResponse) => {
        this.form.patchValue({
          codigo: unidade.codigo,
          nome: unidade.nome,
          sigla: unidade.sigla,
          orgaoId: unidade.orgaoId,
          ativo: unidade.ativo,
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
    const data: UnidadeRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updateUnidade(this.entityId()!, data)
      : this.planejamentoService.createUnidade(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Unidade atualizada com sucesso!' : 'Unidade cadastrada com sucesso!',
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
