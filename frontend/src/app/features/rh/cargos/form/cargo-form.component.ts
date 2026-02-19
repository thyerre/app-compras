import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CargoRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-cargo-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './cargo-form.component.html',
  styleUrl: './cargo-form.component.scss',
})
export class CargoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly tipoOptions = signal<SelectOption[]>([
    { value: 'EFETIVO', label: 'Efetivo' }, { value: 'COMISSIONADO', label: 'Comissionado' },
    { value: 'TEMPORARIO', label: 'Temporário' }, { value: 'ELETIVO', label: 'Eletivo' },
  ]);

  readonly escolaridadeOptions = signal<SelectOption[]>([
    { value: 'FUNDAMENTAL', label: 'Fundamental' }, { value: 'MEDIO', label: 'Médio' },
    { value: 'TECNICO', label: 'Técnico' }, { value: 'SUPERIOR', label: 'Superior' },
    { value: 'POS_GRADUACAO', label: 'Pós-Graduação' },
  ]);

  readonly ativoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' }, { value: false, label: 'Não' },
  ]);

  getRouteBase(): string { return '/rh/cargos'; }

  initForm(): void {
    this.form = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(10)]],
      descricao: ['', [Validators.required, Validators.maxLength(255)]],
      tipo: ['EFETIVO', [Validators.required]],
      escolaridadeMinima: [''],
      cbo: [''],
      vagas: [0],
      vagasOcupadas: [0],
      cargaHorariaSemanal: [40],
      ativo: [true],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findCargoById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: CargoRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateCargo(this.entityId()!, data) : this.rhService.createCargo(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Cargo atualizado!' : 'Cargo cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
