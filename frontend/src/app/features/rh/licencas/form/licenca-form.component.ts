import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LicencaRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-licenca-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './licenca-form.component.html',
  styleUrl: './licenca-form.component.scss',
})
export class LicencaFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly servidorOptions = signal<SelectOption[]>([]);
  readonly tipoLicencaOptions = signal<SelectOption[]>([
    { value: 'MEDICA', label: 'Médica' }, { value: 'MATERNIDADE', label: 'Maternidade' },
    { value: 'PATERNIDADE', label: 'Paternidade' }, { value: 'PREMIO', label: 'Prêmio' },
    { value: 'SEM_VENCIMENTOS', label: 'Sem Vencimentos' }, { value: 'CAPACITACAO', label: 'Capacitação' },
    { value: 'OUTRO', label: 'Outro' },
  ]);
  readonly simNaoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' }, { value: false, label: 'Não' },
  ]);

  getRouteBase(): string { return '/rh/licencas'; }

  override ngOnInit(): void {
    super.ngOnInit();
    this.rhService.findAllServidores({}, 0, 1000, 'nome,asc').subscribe(page =>
      this.servidorOptions.set(page.content.map(s => ({ value: s.id, label: `${s.matricula} - ${s.nome}` })))
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      servidorId: [null, [Validators.required]],
      tipoLicenca: ['', [Validators.required]],
      dataInicio: ['', [Validators.required]],
      dataFim: [''],
      quantidadeDias: [null],
      remunerada: [true],
      cid: [''],
      medicoNome: [''],
      medicoCrm: [''],
      numeroDocumento: [''],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findLicencaById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: LicencaRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateLicenca(this.entityId()!, data) : this.rhService.createLicenca(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Licença atualizada!' : 'Licença cadastrada!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
