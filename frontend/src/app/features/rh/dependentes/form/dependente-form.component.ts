import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DependenteRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-dependente-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './dependente-form.component.html',
  styleUrl: './dependente-form.component.scss',
})
export class DependenteFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly servidorOptions = signal<SelectOption[]>([]);
  readonly parentescoOptions = signal<SelectOption[]>([
    { value: 'CONJUGE', label: 'Cônjuge' }, { value: 'FILHO', label: 'Filho(a)' },
    { value: 'PAI', label: 'Pai' }, { value: 'MAE', label: 'Mãe' },
    { value: 'ENTEADO', label: 'Enteado(a)' }, { value: 'TUTELADO', label: 'Tutelado(a)' },
    { value: 'OUTRO', label: 'Outro' },
  ]);
  readonly sexoOptions = signal<SelectOption[]>([
    { value: 'M', label: 'Masculino' }, { value: 'F', label: 'Feminino' },
  ]);
  readonly ativoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' }, { value: false, label: 'Não' },
  ]);
  readonly simNaoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' }, { value: false, label: 'Não' },
  ]);

  getRouteBase(): string { return '/rh/dependentes'; }

  override ngOnInit(): void {
    super.ngOnInit();
    this.rhService.findAllServidores({}, 0, 1000, 'nome,asc').subscribe(page =>
      this.servidorOptions.set(page.content.map(s => ({ value: s.id, label: `${s.matricula} - ${s.nome}` })))
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      servidorId: [null, [Validators.required]],
      nome: ['', [Validators.required, Validators.maxLength(255)]],
      cpf: [''],
      dataNascimento: ['', [Validators.required]],
      parentesco: ['', [Validators.required]],
      sexo: [''],
      dependenteIr: [false],
      dependenteSalarioFamilia: [false],
      dependentePlanoSaude: [false],
      pensaoAlimenticia: [false],
      percentualPensao: [null],
      dataInicioDependencia: [''],
      dataFimDependencia: [''],
      ativo: [true],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findDependenteById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: DependenteRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateDependente(this.entityId()!, data) : this.rhService.createDependente(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Dependente atualizado!' : 'Dependente cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
