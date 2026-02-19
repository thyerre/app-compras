import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { GuiaArrecadacaoRequest } from '../../../../core/models/arrecadacao.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-guia-arrecadacao-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './guia-arrecadacao-form.component.html',
  styleUrl: './guia-arrecadacao-form.component.scss',
})
export class GuiaArrecadacaoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'EMITIDA', label: 'Emitida' }, { value: 'PAGA', label: 'Paga' },
    { value: 'VENCIDA', label: 'Vencida' }, { value: 'CANCELADA', label: 'Cancelada' },
  ]);

  getRouteBase(): string { return '/arrecadacao/guias'; }

  initForm(): void {
    this.form = this.fb.group({
      numeroGuia: ['', [Validators.required]], lancamentoTributarioId: [null], contribuinteId: [null, [Validators.required]],
      dataEmissao: ['', [Validators.required]], dataVencimento: ['', [Validators.required]],
      valorPrincipal: [0, [Validators.required]], valorDesconto: [0], valorJuros: [0], valorMulta: [0], valorTotal: [0, [Validators.required]],
      codigoBarras: [''], linhaDigitavel: [''], status: ['EMITIDA'],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.arrecadacaoService.findGuiaById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: GuiaArrecadacaoRequest = this.form.value;
    const op = this.isEditMode() ? this.arrecadacaoService.updateGuia(this.entityId()!, data) : this.arrecadacaoService.createGuia(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Guia atualizada!' : 'Guia cadastrada!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
