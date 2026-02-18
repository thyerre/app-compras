import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import {
    ModalidadeLicitacao,
    ProcessoCompraRequest,
    ProcessoCompraResponse,
    StatusProcesso,
    TipoJulgamento,
} from '../../../../core/models/compras.model';
import { OrgaoResponse, UnidadeResponse } from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ComprasService } from '../../../../shared/services/compras.service';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-processo-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatSnackBarModule,
    ButtonComponent,
    InputComponent,
    SelectComponent,
    DatepickerComponent,
  ],
  templateUrl: './processo-form.component.html',
  styleUrl: './processo-form.component.scss',
})
export class ProcessoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly comprasService = inject(ComprasService);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly modalidades = signal<ModalidadeLicitacao[]>([]);
  readonly tiposJulgamento = signal<TipoJulgamento[]>([]);
  readonly statusList = signal<StatusProcesso[]>([]);
  readonly orgaos = signal<OrgaoResponse[]>([]);
  readonly unidades = signal<UnidadeResponse[]>([]);

  readonly modalidadesOptions = computed<SelectOption[]>(() =>
    this.modalidades().map(m => ({ value: m.id, label: m.nome }))
  );
  readonly tiposJulgamentoOptions = computed<SelectOption[]>(() =>
    this.tiposJulgamento().map(t => ({ value: t.id, label: t.descricao }))
  );
  readonly statusOptions = computed<SelectOption[]>(() =>
    this.statusList().map(s => ({ value: s.id, label: s.nome }))
  );
  readonly orgaosOptions = computed<SelectOption[]>(() =>
    this.orgaos().map(o => ({ value: o.id, label: `${o.codigo} - ${o.nome}` }))
  );
  readonly unidadesOptions = computed<SelectOption[]>(() =>
    this.unidades().map(u => ({ value: u.id, label: `${u.codigo} - ${u.nome}` }))
  );

  getRouteBase(): string {
    return '/processos';
  }

  initForm(): void {
    this.form = this.fb.group({
      numeroProcesso: ['', [Validators.required, Validators.maxLength(30)]],
      exercicio: [new Date().getFullYear(), Validators.required],
      objeto: ['', [Validators.required, Validators.maxLength(1000)]],
      justificativa: [''],
      modalidadeId: [null, Validators.required],
      tipoJulgamentoId: [null],
      statusId: [null, Validators.required],
      orgaoId: [null, Validators.required],
      unidadeId: [null],
      dotacaoOrcamentariaId: [null],
      valorEstimado: [null],
      valorHomologado: [null],
      dataAbertura: [null],
      dataEncerramento: [null],
      dataHomologacao: [null],
      dataPublicacao: [null],
      numeroEdital: [''],
      anoEdital: [null],
      observacoes: [''],
      itens: this.fb.array([]),
    });

    this.loadDomainData();
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.comprasService.findProcessoById(id).subscribe({
      next: (p: ProcessoCompraResponse) => this.patchForm(p),
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    const data: ProcessoCompraRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.comprasService.updateProcesso(this.entityId()!, data)
      : this.comprasService.createProcesso(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Processo atualizado com sucesso!' : 'Processo cadastrado com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }

  get itens(): FormArray {
    return this.form.get('itens') as FormArray;
  }

  addItem(): void {
    this.itens.push(
      this.fb.group({
        id: [null],
        numeroItem: [this.itens.length + 1],
        descricao: ['', Validators.required],
        unidade: [''],
        quantidade: [null],
        valorUnitarioEstimado: [null],
        valorTotalEstimado: [null],
      })
    );
  }

  removeItem(index: number): void {
    this.itens.removeAt(index);
  }

  onOrgaoChange(value: unknown): void {
    const orgaoId = value as number;
    this.form.get('unidadeId')?.setValue(null);
    this.unidades.set([]);
    if (orgaoId) {
      this.planejamentoService.getUnidadesSimples(orgaoId).subscribe(data => this.unidades.set(data));
    }
  }

  getError(field: string): string {
    const control = this.form.get(field);
    if (control && control.touched && control.errors) {
      if (control.errors['required']) return 'Campo obrigatório';
      if (control.errors['maxlength']) return `Máximo de ${control.errors['maxlength'].requiredLength} caracteres`;
    }
    return '';
  }

  private loadDomainData(): void {
    this.comprasService.getModalidades().subscribe(data => this.modalidades.set(data));
    this.comprasService.getTiposJulgamento().subscribe(data => this.tiposJulgamento.set(data));
    this.comprasService.getStatusProcesso().subscribe(data => this.statusList.set(data));
    this.planejamentoService.getOrgaosSimples().subscribe(data => this.orgaos.set(data));
  }

  private patchForm(p: ProcessoCompraResponse): void {
    this.form.patchValue({
      numeroProcesso: p.numeroProcesso,
      exercicio: p.exercicio,
      objeto: p.objeto,
      justificativa: p.justificativa,
      modalidadeId: p.modalidadeId,
      tipoJulgamentoId: p.tipoJulgamentoId,
      statusId: p.statusId,
      dotacaoOrcamentariaId: p.dotacaoOrcamentariaId,
      valorEstimado: p.valorEstimado,
      valorHomologado: p.valorHomologado,
      dataAbertura: p.dataAbertura,
      dataEncerramento: p.dataEncerramento,
      dataHomologacao: p.dataHomologacao,
      dataPublicacao: p.dataPublicacao,
      numeroEdital: p.numeroEdital,
      anoEdital: p.anoEdital,
      observacoes: p.observacoes,
    });

    // Itens
    this.itens.clear();
    (p.itens || []).forEach(item => {
      this.itens.push(
        this.fb.group({
          id: [item.id],
          numeroItem: [item.numeroItem],
          descricao: [item.descricao, Validators.required],
          unidade: [item.unidade],
          quantidade: [item.quantidade],
          valorUnitarioEstimado: [item.valorUnitarioEstimado],
          valorTotalEstimado: [item.valorTotalEstimado],
        })
      );
    });

    this.loadingData.set(false);
  }
}
