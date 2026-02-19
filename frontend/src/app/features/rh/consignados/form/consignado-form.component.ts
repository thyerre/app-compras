import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ConsignadoRequest } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-consignado-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './consignado-form.component.html',
  styleUrl: './consignado-form.component.scss',
})
export class ConsignadoFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly servidorOptions = signal<SelectOption[]>([]);
  readonly eventoFolhaOptions = signal<SelectOption[]>([]);
  readonly statusOptions = signal<SelectOption[]>([
    { value: 'ATIVO', label: 'Ativo' }, { value: 'QUITADO', label: 'Quitado' },
    { value: 'SUSPENSO', label: 'Suspenso' }, { value: 'CANCELADO', label: 'Cancelado' },
  ]);

  getRouteBase(): string { return '/rh/consignados'; }

  override ngOnInit(): void {
    super.ngOnInit();
    this.rhService.findAllServidores({}, 0, 1000, 'nome,asc').subscribe(page =>
      this.servidorOptions.set(page.content.map(s => ({ value: s.id, label: `${s.matricula} - ${s.nome}` })))
    );
    this.rhService.findAllEventosFolha({ ativo: true }, 0, 1000, 'descricao,asc').subscribe(page =>
      this.eventoFolhaOptions.set(page.content.map(e => ({ value: e.id, label: `${e.codigo} - ${e.descricao}` })))
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      servidorId: [null, [Validators.required]],
      eventoFolhaId: [null],
      consignataria: ['', [Validators.required]],
      contrato: [''],
      parcelaAtual: [0],
      parcelaTotal: [null, [Validators.required]],
      valorParcela: [null, [Validators.required]],
      valorTotal: [null, [Validators.required]],
      dataInicio: ['', [Validators.required]],
      dataFim: [''],
      status: ['ATIVO'],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findConsignadoById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: ConsignadoRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateConsignado(this.entityId()!, data) : this.rhService.createConsignado(data);
    op.subscribe({
      next: () => { this.snackBar.open(this.isEditMode() ? 'Consignado atualizado!' : 'Consignado cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }); this.saving.set(false); this.navigateBack(); },
      error: () => this.saving.set(false),
    });
  }
}
