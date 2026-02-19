import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HistoricoFuncionalRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-historico-funcional-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './historico-funcional-form.component.html',
  styleUrl: './historico-funcional-form.component.scss',
})
export class HistoricoFuncionalFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly servidorOptions = signal<SelectOption[]>([]);
  readonly cargoOptions = signal<SelectOption[]>([]);
  readonly orgaoOptions = signal<SelectOption[]>([]);
  readonly unidadeOptions = signal<SelectOption[]>([]);

  getRouteBase(): string { return '/rh/historico-funcional'; }

  override ngOnInit(): void {
    super.ngOnInit();
    this.rhService.findAllServidores({}, 0, 1000, 'nome,asc').subscribe(page =>
      this.servidorOptions.set(page.content.map(s => ({ value: s.id, label: `${s.matricula} - ${s.nome}` })))
    );
    this.rhService.findCargosLookup().subscribe(cargos =>
      this.cargoOptions.set(cargos.map(c => ({ value: c.id, label: c.descricao })))
    );
    this.planejamentoService.getOrgaosSimples().subscribe(orgaos =>
      this.orgaoOptions.set(orgaos.map(o => ({ value: o.id, label: o.nome })))
    );
    this.planejamentoService.getUnidadesSimples().subscribe(unidades =>
      this.unidadeOptions.set(unidades.map(u => ({ value: u.id, label: u.nome })))
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      servidorId: [null, [Validators.required]],
      tipoEvento: ['', [Validators.required]],
      dataEvento: ['', [Validators.required]],
      numeroAto: [''],
      descricao: ['', [Validators.required]],
      cargoAnteriorId: [null],
      cargoNovoId: [null],
      salarioAnterior: [null],
      salarioNovo: [null],
      orgaoAnteriorId: [null],
      orgaoNovoId: [null],
      unidadeAnteriorId: [null],
      unidadeNovoId: [null],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findHistoricoFuncionalById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: HistoricoFuncionalRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateHistoricoFuncional(this.entityId()!, data) : this.rhService.createHistoricoFuncional(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Registro atualizado!' : 'Registro cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
