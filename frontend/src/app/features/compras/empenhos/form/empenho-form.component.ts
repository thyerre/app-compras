import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { EmpenhoRequest, EmpenhoResponse } from '../../../../core/models/compras.model';
import { FornecedorListItem } from '../../../../core/models/fornecedor.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ComprasService } from '../../../../shared/services/compras.service';
import { FornecedorService } from '../../../../shared/services/fornecedor.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-empenho-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent, DatepickerComponent],
  templateUrl: './empenho-form.component.html',
  styleUrl: './empenho-form.component.scss',
})
export class EmpenhoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly comprasService = inject(ComprasService);
  private readonly fornecedorService = inject(FornecedorService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly fornecedores = signal<FornecedorListItem[]>([]);

  readonly fornecedoresOptions = computed<SelectOption[]>(() =>
    this.fornecedores().map(f => ({ value: f.id, label: `${f.cnpjCpf} - ${f.razaoSocial}` }))
  );

  readonly tipoEmpenhoOptions: SelectOption[] = [
    { value: 'ORDINARIO', label: 'Ordinário' },
    { value: 'ESTIMATIVO', label: 'Estimativo' },
    { value: 'GLOBAL', label: 'Global' },
  ];

  readonly statusOptions: SelectOption[] = [
    { value: 'EMITIDO', label: 'Emitido' },
    { value: 'LIQUIDADO', label: 'Liquidado' },
    { value: 'PAGO', label: 'Pago' },
    { value: 'ANULADO', label: 'Anulado' },
  ];

  getRouteBase(): string {
    return '/empenhos';
  }

  initForm(): void {
    this.form = this.fb.group({
      numeroEmpenho: ['', [Validators.required, Validators.maxLength(30)]],
      exercicio: [new Date().getFullYear(), Validators.required],
      processoCompraId: [null],
      fornecedorId: [null, Validators.required],
      dotacaoOrcamentariaId: [null],
      tipoEmpenho: ['ORDINARIO', Validators.required],
      dataEmpenho: [null, Validators.required],
      descricao: ['', [Validators.required, Validators.maxLength(1000)]],
      valor: [0, [Validators.required, Validators.min(0.01)]],
      valorAnulado: [0],
      valorLiquidado: [0],
      valorPago: [0],
      status: ['EMITIDO'],
      observacoes: [''],
    });

    this.loadDomainData();
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.comprasService.findEmpenhoById(id).subscribe({
      next: (e: EmpenhoResponse) => {
        this.form.patchValue({
          numeroEmpenho: e.numeroEmpenho,
          exercicio: e.exercicio,
          processoCompraId: e.processoCompraId,
          fornecedorId: e.fornecedorId,
          dotacaoOrcamentariaId: e.dotacaoOrcamentariaId,
          tipoEmpenho: e.tipoEmpenho,
          dataEmpenho: e.dataEmpenho,
          descricao: e.descricao,
          valor: e.valor,
          valorAnulado: e.valorAnulado,
          valorLiquidado: e.valorLiquidado,
          valorPago: e.valorPago,
          status: e.status,
          observacoes: e.observacoes,
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
    const data: EmpenhoRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.comprasService.updateEmpenho(this.entityId()!, data)
      : this.comprasService.createEmpenho(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Empenho atualizado com sucesso!' : 'Empenho cadastrado com sucesso!',
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

  private loadDomainData(): void {
    this.fornecedorService.findAll({}, 0, 200).subscribe(page => this.fornecedores.set(page.content));
  }
}
