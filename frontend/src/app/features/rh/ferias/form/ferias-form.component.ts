import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FeriasRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-ferias-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './ferias-form.component.html',
  styleUrl: './ferias-form.component.scss',
})
export class FeriasFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly servidorOptions = signal<SelectOption[]>([]);
  readonly statusOptions = signal<SelectOption[]>([
    { value: 'PENDENTE', label: 'Pendente' }, { value: 'APROVADA', label: 'Aprovada' },
    { value: 'EM_GOZO', label: 'Em Gozo' }, { value: 'CONCLUIDA', label: 'Concluída' },
    { value: 'CANCELADA', label: 'Cancelada' },
  ]);

  getRouteBase(): string { return '/rh/ferias'; }

  override ngOnInit(): void {
    super.ngOnInit();
    this.rhService.findAllServidores({}, 0, 1000, 'nome,asc').subscribe(page =>
      this.servidorOptions.set(page.content.map(s => ({ value: s.id, label: `${s.matricula} - ${s.nome}` })))
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      servidorId: [null, [Validators.required]],
      periodoAquisitivoInicio: ['', [Validators.required]],
      periodoAquisitivoFim: ['', [Validators.required]],
      periodoGozoInicio: [''],
      periodoGozoFim: [''],
      diasGozo: [null],
      diasAbono: [0],
      parcelas: [1],
      valorFerias: [null],
      valorAbono: [null],
      valor13Ferias: [null],
      status: ['PENDENTE'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findFeriasById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: FeriasRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateFerias(this.entityId()!, data) : this.rhService.createFerias(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Férias atualizadas!' : 'Férias cadastradas!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
