import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BemPatrimonialRequest, BemPatrimonialResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-bem-patrimonial-form',
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
  templateUrl: './bem-patrimonial-form.component.html',
  styleUrl: './bem-patrimonial-form.component.scss',
})
export class BemPatrimonialFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'MOVEL', label: 'Móvel' },
    { value: 'IMOVEL', label: 'Imóvel' },
    { value: 'INTANGIVEL', label: 'Intangível' },
  ]);

  readonly metodoDepreciacaoOptions = signal<SelectOption[]>([
    { value: 'LINEAR', label: 'Linear' },
    { value: 'SOMA_DIGITOS', label: 'Soma dos Dígitos' },
    { value: 'UNIDADES_PRODUZIDAS', label: 'Unidades Produzidas' },
  ]);

  readonly estadoConservacaoOptions = signal<SelectOption[]>([
    { value: 'BOM', label: 'Bom' },
    { value: 'REGULAR', label: 'Regular' },
    { value: 'RUIM', label: 'Ruim' },
    { value: 'INSERVIVEL', label: 'Inservível' },
  ]);

  readonly situacaoOptions = signal<SelectOption[]>([
    { value: 'ATIVO', label: 'Ativo' },
    { value: 'BAIXADO', label: 'Baixado' },
    { value: 'CEDIDO', label: 'Cedido' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/bens-patrimoniais';
  }

  initForm(): void {
    this.form = this.fb.group({
      numeroPatrimonio: ['', [Validators.required, Validators.maxLength(30)]],
      descricao: ['', [Validators.required, Validators.maxLength(500)]],
      tipo: ['MOVEL', Validators.required],
      orgaoId: [null, Validators.required],
      unidadeId: [null],
      fornecedorId: [null],
      empenhoId: [null],
      planoContaId: [null],
      dataAquisicao: [null, Validators.required],
      valorOriginal: [0, [Validators.required, Validators.min(0.01)]],
      valorAtual: [0],
      vidaUtilAnos: [null],
      valorResidual: [0],
      depreciacaoAcumulada: [0],
      metodoDepreciacao: ['LINEAR'],
      estadoConservacao: ['BOM'],
      localizacao: [''],
      responsavel: [''],
      situacao: ['ATIVO'],
      dataBaixa: [null],
      motivoBaixa: [''],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findBemPatrimonialById(id).subscribe({
      next: (entity: BemPatrimonialResponse) => {
        this.form.patchValue({
          numeroPatrimonio: entity.numeroPatrimonio,
          descricao: entity.descricao,
          tipo: entity.tipo,
          orgaoId: entity.orgaoId,
          unidadeId: entity.unidadeId ?? null,
          fornecedorId: entity.fornecedorId ?? null,
          empenhoId: entity.empenhoId ?? null,
          planoContaId: entity.planoContaId ?? null,
          dataAquisicao: entity.dataAquisicao,
          valorOriginal: entity.valorOriginal,
          valorAtual: entity.valorAtual ?? 0,
          vidaUtilAnos: entity.vidaUtilAnos ?? null,
          valorResidual: entity.valorResidual ?? 0,
          depreciacaoAcumulada: entity.depreciacaoAcumulada ?? 0,
          metodoDepreciacao: entity.metodoDepreciacao ?? 'LINEAR',
          estadoConservacao: entity.estadoConservacao ?? 'BOM',
          localizacao: entity.localizacao ?? '',
          responsavel: entity.responsavel ?? '',
          situacao: entity.situacao ?? 'ATIVO',
          dataBaixa: entity.dataBaixa ?? null,
          motivoBaixa: entity.motivoBaixa ?? '',
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
    const data: BemPatrimonialRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateBemPatrimonial(this.entityId()!, data)
      : this.contabilidadeService.createBemPatrimonial(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Bem Patrimonial atualizado com sucesso!' : 'Bem Patrimonial cadastrado com sucesso!',
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
}
