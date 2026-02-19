import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ContaBancariaRequest } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-conta-bancaria-form',
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
  templateUrl: './conta-bancaria-form.component.html',
  styleUrl: './conta-bancaria-form.component.scss',
})
export class ContaBancariaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'MOVIMENTO', label: 'Movimento' },
    { value: 'VINCULADA', label: 'Vinculada' },
    { value: 'APLICACAO', label: 'Aplicação' },
    { value: 'ESPECIAL', label: 'Especial' },
  ]);

  readonly ativoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' },
    { value: false, label: 'Não' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/contas-bancarias';
  }

  initForm(): void {
    this.form = this.fb.group({
      bancoCodigo: ['', [Validators.required, Validators.maxLength(10)]],
      bancoNome: ['', [Validators.required, Validators.maxLength(100)]],
      agencia: ['', [Validators.required, Validators.maxLength(20)]],
      numeroConta: ['', [Validators.required, Validators.maxLength(20)]],
      digito: [''],
      descricao: ['', [Validators.required, Validators.maxLength(255)]],
      tipo: ['MOVIMENTO'],
      fonteRecursoId: [null],
      saldoAtual: [0],
      ativo: [true],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findContaBancariaById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          bancoCodigo: entity.bancoCodigo,
          bancoNome: entity.bancoNome,
          agencia: entity.agencia,
          numeroConta: entity.numeroConta,
          digito: entity.digito ?? '',
          descricao: entity.descricao,
          tipo: entity.tipo ?? 'MOVIMENTO',
          fonteRecursoId: entity.fonteRecursoId ?? null,
          saldoAtual: entity.saldoAtual ?? 0,
          ativo: entity.ativo ?? true,
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
    const data: ContaBancariaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateContaBancaria(this.entityId()!, data)
      : this.contabilidadeService.createContaBancaria(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Conta Bancária atualizada com sucesso!' : 'Conta Bancária cadastrada com sucesso!',
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
