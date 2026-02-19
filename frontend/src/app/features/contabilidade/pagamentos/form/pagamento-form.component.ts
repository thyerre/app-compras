import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PagamentoRequest } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-pagamento-form',
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
  templateUrl: './pagamento-form.component.html',
  styleUrl: './pagamento-form.component.scss',
})
export class PagamentoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly formaPagamentoOptions = signal<SelectOption[]>([
    { value: 'ORDEM_BANCARIA', label: 'Ordem Bancária' },
    { value: 'TRANSFERENCIA', label: 'Transferência' },
    { value: 'CHEQUE', label: 'Cheque' },
    { value: 'DINHEIRO', label: 'Dinheiro' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'PAGO', label: 'Pago' },
    { value: 'ANULADO', label: 'Anulado' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/pagamentos';
  }

  initForm(): void {
    this.form = this.fb.group({
      liquidacaoId: [null, Validators.required],
      contaBancariaId: [null, Validators.required],
      numeroPagamento: ['', [Validators.required, Validators.maxLength(30)]],
      dataPagamento: [null, Validators.required],
      valor: [0, [Validators.required, Validators.min(0.01)]],
      formaPagamento: ['ORDEM_BANCARIA'],
      numeroDocumento: [''],
      descricao: [''],
      status: ['PAGO'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findPagamentoById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          liquidacaoId: entity.liquidacaoId,
          contaBancariaId: entity.contaBancariaId,
          numeroPagamento: entity.numeroPagamento,
          dataPagamento: entity.dataPagamento,
          valor: entity.valor,
          formaPagamento: entity.formaPagamento ?? 'ORDEM_BANCARIA',
          numeroDocumento: entity.numeroDocumento ?? '',
          descricao: entity.descricao ?? '',
          status: entity.status ?? 'PAGO',
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
    const data: PagamentoRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updatePagamento(this.entityId()!, data)
      : this.contabilidadeService.createPagamento(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Pagamento atualizado com sucesso!' : 'Pagamento cadastrado com sucesso!',
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
