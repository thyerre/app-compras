import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DividaPublicaRequest, DividaPublicaResponse } from '../../../../core/models/contabilidade.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ContabilidadeService } from '../../../../shared/services/contabilidade.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-divida-publica-form',
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
  templateUrl: './divida-publica-form.component.html',
  styleUrl: './divida-publica-form.component.scss',
})
export class DividaPublicaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contabilidadeService = inject(ContabilidadeService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'FUNDADA', label: 'Fundada' },
    { value: 'FLUTUANTE', label: 'Flutuante' },
  ]);

  readonly indexadorOptions = signal<SelectOption[]>([
    { value: 'IPCA', label: 'IPCA' },
    { value: 'IGPM', label: 'IGP-M' },
    { value: 'SELIC', label: 'SELIC' },
    { value: 'PREFIXADO', label: 'Prefixado' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'ATIVA', label: 'Ativa' },
    { value: 'QUITADA', label: 'Quitada' },
    { value: 'RENEGOCIADA', label: 'Renegociada' },
  ]);

  getRouteBase(): string {
    return '/contabilidade/divida-publica';
  }

  initForm(): void {
    this.form = this.fb.group({
      tipo: ['FUNDADA', Validators.required],
      credor: ['', [Validators.required, Validators.maxLength(255)]],
      cnpjCredor: [''],
      numeroContrato: [''],
      dataContratacao: [null, Validators.required],
      dataVencimento: [null],
      valorOriginal: [0, [Validators.required, Validators.min(0.01)]],
      saldoDevedor: [0],
      taxaJuros: [null],
      indexador: [''],
      finalidade: [''],
      leiAutorizativa: [''],
      status: ['ATIVA'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.contabilidadeService.findDividaPublicaById(id).subscribe({
      next: (entity: DividaPublicaResponse) => {
        this.form.patchValue({
          tipo: entity.tipo,
          credor: entity.credor,
          cnpjCredor: entity.cnpjCredor ?? '',
          numeroContrato: entity.numeroContrato ?? '',
          dataContratacao: entity.dataContratacao,
          dataVencimento: entity.dataVencimento ?? null,
          valorOriginal: entity.valorOriginal,
          saldoDevedor: entity.saldoDevedor ?? 0,
          taxaJuros: entity.taxaJuros ?? null,
          indexador: entity.indexador ?? '',
          finalidade: entity.finalidade ?? '',
          leiAutorizativa: entity.leiAutorizativa ?? '',
          status: entity.status ?? 'ATIVA',
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
    const data: DividaPublicaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.contabilidadeService.updateDividaPublica(this.entityId()!, data)
      : this.contabilidadeService.createDividaPublica(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Dívida Pública atualizada com sucesso!' : 'Dívida Pública cadastrada com sucesso!',
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
