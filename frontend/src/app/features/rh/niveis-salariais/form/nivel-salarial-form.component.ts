import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { NivelSalarialRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-nivel-salarial-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './nivel-salarial-form.component.html',
  styleUrl: './nivel-salarial-form.component.scss',
})
export class NivelSalarialFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly cargoOptions = signal<SelectOption[]>([]);
  readonly ativoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' }, { value: false, label: 'Não' },
  ]);

  getRouteBase(): string { return '/rh/niveis-salariais'; }

  override ngOnInit(): void {
    super.ngOnInit();
    this.rhService.findCargosLookup().subscribe(cargos =>
      this.cargoOptions.set(cargos.map(c => ({ value: c.id, label: c.descricao })))
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      cargoId: [null, [Validators.required]],
      nivel: ['', [Validators.required, Validators.maxLength(10)]],
      classe: [''],
      referencia: [''],
      valorBase: [null, [Validators.required]],
      vigenciaInicio: ['', [Validators.required]],
      vigenciaFim: [''],
      ativo: [true],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findNivelSalarialById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: NivelSalarialRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateNivelSalarial(this.entityId()!, data) : this.rhService.createNivelSalarial(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Nível salarial atualizado!' : 'Nível salarial cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
