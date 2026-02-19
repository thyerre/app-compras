import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LiquidacaoRequest } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-liquidacao-form',
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
  templateUrl: './liquidacao-form.component.html',
  styleUrl: './liquidacao-form.component.scss',
})
export class LiquidacaoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoDocumentoFiscalOptions = signal<SelectOption[]>([
    { value: '', label: '—' },
    { value: 'NOTA_FISCAL', label: 'Nota Fiscal' },
    { value: 'RECIBO', label: 'Recibo' },
    { value: 'FATURA', label: 'Fatura' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'LIQUIDADA', label: 'Liquidada' },
    { value: 'ANULADA', label: 'Anulada' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/liquidacoes';
  }

  initForm(): void {
    this.form = this.fb.group({
      empenhoId: [null, Validators.required],
      numeroLiquidacao: ['', [Validators.required, Validators.maxLength(30)]],
      dataLiquidacao: [null, Validators.required],
      valor: [0, [Validators.required, Validators.min(0.01)]],
      tipoDocumentoFiscal: [''],
      numeroDocumentoFiscal: [''],
      dataDocumentoFiscal: [null],
      descricao: [''],
      status: ['LIQUIDADA'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findLiquidacaoById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          empenhoId: entity.empenhoId,
          numeroLiquidacao: entity.numeroLiquidacao,
          dataLiquidacao: entity.dataLiquidacao,
          valor: entity.valor,
          tipoDocumentoFiscal: entity.tipoDocumentoFiscal ?? '',
          numeroDocumentoFiscal: entity.numeroDocumentoFiscal ?? '',
          dataDocumentoFiscal: entity.dataDocumentoFiscal ?? null,
          descricao: entity.descricao ?? '',
          status: entity.status ?? 'LIQUIDADA',
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
    const data: LiquidacaoRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateLiquidacao(this.entityId()!, data)
      : this.contabilidadeService.createLiquidacao(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Liquidação atualizada com sucesso!' : 'Liquidação cadastrada com sucesso!',
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
