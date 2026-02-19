import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LancamentoContabilRequest } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-lancamento-form',
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
  templateUrl: './lancamento-form.component.html',
  styleUrl: './lancamento-form.component.scss',
})
export class LancamentoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'NORMAL', label: 'Normal' },
    { value: 'ESTORNO', label: 'Estorno' },
    { value: 'ENCERRAMENTO', label: 'Encerramento' },
  ]);

  readonly origemOptions = signal<SelectOption[]>([
    { value: '', label: '—' },
    { value: 'ORCAMENTARIO', label: 'Orçamentário' },
    { value: 'FINANCEIRO', label: 'Financeiro' },
    { value: 'PATRIMONIAL', label: 'Patrimonial' },
    { value: 'COMPENSACAO', label: 'Compensação' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/lancamentos';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [new Date().getFullYear(), Validators.required],
      numeroLancamento: ['', [Validators.required, Validators.maxLength(30)]],
      dataLancamento: [null, Validators.required],
      tipo: ['NORMAL', Validators.required],
      origem: [''],
      historico: ['', [Validators.required, Validators.maxLength(1000)]],
      empenhoId: [null],
      liquidacaoId: [null],
      pagamentoId: [null],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findLancamentoById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          exercicio: entity.exercicio,
          numeroLancamento: entity.numeroLancamento,
          dataLancamento: entity.dataLancamento,
          tipo: entity.tipo,
          origem: entity.origem ?? '',
          historico: entity.historico,
          empenhoId: entity.empenhoId ?? null,
          liquidacaoId: entity.liquidacaoId ?? null,
          pagamentoId: entity.pagamentoId ?? null,
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
    const formValue = this.form.value;
    const data: LancamentoContabilRequest = {
      ...formValue,
      itens: [],
    };

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateLancamento(this.entityId()!, data)
      : this.contabilidadeService.createLancamento(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Lançamento atualizado com sucesso!' : 'Lançamento cadastrado com sucesso!',
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
