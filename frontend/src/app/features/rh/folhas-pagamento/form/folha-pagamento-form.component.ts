import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FolhaPagamentoRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-folha-pagamento-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './folha-pagamento-form.component.html',
  styleUrl: './folha-pagamento-form.component.scss',
})
export class FolhaPagamentoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'NORMAL', label: 'Normal' }, { value: 'COMPLEMENTAR', label: 'Complementar' },
    { value: 'DECIMO_TERCEIRO', label: '13º Salário' }, { value: 'FERIAS', label: 'Férias' },
    { value: 'RESCISAO', label: 'Rescisão' },
  ]);

  readonly statusOptions = signal<SelectOption[]>([
    { value: 'ABERTA', label: 'Aberta' }, { value: 'CALCULADA', label: 'Calculada' },
    { value: 'CONFERIDA', label: 'Conferida' }, { value: 'FECHADA', label: 'Fechada' },
    { value: 'PAGA', label: 'Paga' },
  ]);

  getRouteBase(): string { return '/rh/folhas-pagamento'; }

  initForm(): void {
    this.form = this.fb.group({
      competencia: ['', [Validators.required, Validators.maxLength(7)]],
      tipo: ['NORMAL'],
      dataPagamento: [''],
      status: ['ABERTA'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findFolhaById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: FolhaPagamentoRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateFolha(this.entityId()!, data) : this.rhService.createFolha(data);
    op.subscribe({
      next: () => {
        this.snackBar.open(this.isEditMode() ? 'Folha atualizada!' : 'Folha cadastrada!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
        this.saving.set(false); this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }
}
