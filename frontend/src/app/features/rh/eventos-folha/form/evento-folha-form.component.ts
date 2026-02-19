import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { EventoFolhaRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-evento-folha-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './evento-folha-form.component.html',
  styleUrl: './evento-folha-form.component.scss',
})
export class EventoFolhaFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'PROVENTO', label: 'Provento' }, { value: 'DESCONTO', label: 'Desconto' },
  ]);
  readonly tipoCalculoOptions = signal<SelectOption[]>([
    { value: 'PERCENTUAL', label: 'Percentual' }, { value: 'VALOR_FIXO', label: 'Valor Fixo' },
    { value: 'FORMULA', label: 'Fórmula' },
  ]);
  readonly simNaoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' }, { value: false, label: 'Não' },
  ]);

  getRouteBase(): string { return '/rh/eventos-folha'; }

  initForm(): void {
    this.form = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(10)]],
      descricao: ['', [Validators.required, Validators.maxLength(255)]],
      tipo: ['PROVENTO', [Validators.required]],
      incidenciaInss: [false],
      incidenciaIrrf: [false],
      incidenciaFgts: [false],
      automatico: [false],
      formula: [''],
      percentual: [null],
      valorFixo: [null],
      tipoCalculo: ['PERCENTUAL'],
      aplicaMensal: [true],
      aplicaFerias: [false],
      aplica13: [false],
      aplicaRescisao: [false],
      ativo: [true],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findEventoFolhaById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: EventoFolhaRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateEventoFolha(this.entityId()!, data) : this.rhService.createEventoFolha(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Evento de folha atualizado!' : 'Evento de folha cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
