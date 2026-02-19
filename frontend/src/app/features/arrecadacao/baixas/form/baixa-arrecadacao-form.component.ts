import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BaixaArrecadacaoRequest } from '../../../../core/models/arrecadacao.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-baixa-arrecadacao-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './baixa-arrecadacao-form.component.html',
  styleUrl: './baixa-arrecadacao-form.component.scss',
})
export class BaixaArrecadacaoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly tipoBaixaOptions = signal<SelectOption[]>([
    { value: 'NORMAL', label: 'Normal' }, { value: 'ESTORNO', label: 'Estorno' },
    { value: 'DACAO', label: 'Dação' }, { value: 'COMPENSACAO', label: 'Compensação' },
  ]);

  getRouteBase(): string { return '/arrecadacao/baixas'; }

  initForm(): void {
    this.form = this.fb.group({
      guiaArrecadacaoId: [null, [Validators.required]], agenteArrecadadorId: [null],
      dataPagamento: ['', [Validators.required]], dataCredito: [''],
      valorPago: [0, [Validators.required]], valorJuros: [0], valorMulta: [0], valorDesconto: [0],
      tipoBaixa: ['NORMAL'], autenticacao: [''], receitaOrcamentariaId: [null], observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.arrecadacaoService.findBaixaById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: BaixaArrecadacaoRequest = this.form.value;
    const op = this.isEditMode() ? this.arrecadacaoService.updateBaixa(this.entityId()!, data) : this.arrecadacaoService.createBaixa(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Baixa atualizada!' : 'Baixa registrada!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
