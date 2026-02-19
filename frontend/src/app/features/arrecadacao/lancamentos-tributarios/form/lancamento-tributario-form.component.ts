import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LancamentoTributarioRequest } from '../../../../core/models/arrecadacao.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-lancamento-tributario-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './lancamento-tributario-form.component.html',
  styleUrl: './lancamento-tributario-form.component.scss',
})
export class LancamentoTributarioFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'ABERTO', label: 'Aberto' },
    { value: 'PAGO', label: 'Pago' },
    { value: 'CANCELADO', label: 'Cancelado' },
    { value: 'INSCRITO_DIVIDA', label: 'Inscrito em Dívida' },
  ]);

  getRouteBase(): string {
    return '/arrecadacao/lancamentos-tributarios';
  }

  initForm(): void {
    this.form = this.fb.group({
      numeroLancamento: ['', [Validators.required]],
      exercicio: [new Date().getFullYear(), [Validators.required]],
      contribuinteId: [null, [Validators.required]],
      tributoId: [null, [Validators.required]],
      imovelId: [null],
      dataLancamento: ['', [Validators.required]],
      dataVencimento: ['', [Validators.required]],
      baseCalculo: [0],
      aliquota: [0],
      valorPrincipal: [0, [Validators.required]],
      valorDesconto: [0],
      valorJuros: [0],
      valorMulta: [0],
      valorTotal: [0, [Validators.required]],
      status: ['ABERTO'],
      historico: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.arrecadacaoService.findLancamentoById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          numeroLancamento: entity.numeroLancamento,
          exercicio: entity.exercicio,
          contribuinteId: entity.contribuinteId,
          tributoId: entity.tributoId,
          imovelId: entity.imovelId ?? null,
          dataLancamento: entity.dataLancamento ?? '',
          dataVencimento: entity.dataVencimento ?? '',
          baseCalculo: entity.baseCalculo ?? 0,
          aliquota: entity.aliquota ?? 0,
          valorPrincipal: entity.valorPrincipal ?? 0,
          valorDesconto: entity.valorDesconto ?? 0,
          valorJuros: entity.valorJuros ?? 0,
          valorMulta: entity.valorMulta ?? 0,
          valorTotal: entity.valorTotal ?? 0,
          status: entity.status ?? 'ABERTO',
          historico: entity.historico ?? '',
        });
        this.loadingData.set(false);
      },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: LancamentoTributarioRequest = this.form.value;
    const operation = this.isEditMode()
      ? this.arrecadacaoService.updateLancamento(this.entityId()!, data)
      : this.arrecadacaoService.createLancamento(data);
    operation.subscribe({
      next: () => {
        this.snackBar.open(this.isEditMode() ? 'Lançamento atualizado!' : 'Lançamento cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }
}
