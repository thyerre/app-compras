import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import {
    DotacaoOrcamentariaRequest,
    DotacaoOrcamentariaResponse,
    FonteRecurso,
    Funcao,
    NaturezaDespesa,
    OrgaoResponse,
    ProgramaListItem,
    Subfuncao,
    UnidadeResponse,
} from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-dotacao-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './dotacao-form.component.html',
  styleUrl: './dotacao-form.component.scss',
})
export class DotacaoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly orgaos = signal<OrgaoResponse[]>([]);
  readonly unidades = signal<UnidadeResponse[]>([]);
  readonly funcoes = signal<Funcao[]>([]);
  readonly subfuncoes = signal<Subfuncao[]>([]);
  readonly programas = signal<ProgramaListItem[]>([]);
  readonly naturezas = signal<NaturezaDespesa[]>([]);
  readonly fontes = signal<FonteRecurso[]>([]);

  readonly orgaosOptions = computed<SelectOption[]>(() =>
    this.orgaos().map(o => ({ value: o.id, label: `${o.codigo} - ${o.nome}` }))
  );
  readonly unidadesOptions = computed<SelectOption[]>(() =>
    this.unidades().map(u => ({ value: u.id, label: `${u.codigo} - ${u.nome}` }))
  );
  readonly funcoesOptions = computed<SelectOption[]>(() =>
    this.funcoes().map(f => ({ value: f.id, label: `${f.codigo} - ${f.descricao}` }))
  );
  readonly subfuncoesOptions = computed<SelectOption[]>(() =>
    this.subfuncoes().map(s => ({ value: s.id, label: `${s.codigo} - ${s.descricao}` }))
  );
  readonly programasOptions = computed<SelectOption[]>(() =>
    this.programas().map(p => ({ value: p.id, label: `${p.codigo} - ${p.nome}` }))
  );
  readonly naturezasOptions = computed<SelectOption[]>(() =>
    this.naturezas().map(n => ({ value: n.id, label: `${n.codigo} - ${n.descricao}` }))
  );
  readonly fontesOptions = computed<SelectOption[]>(() =>
    this.fontes().map(f => ({ value: f.id, label: `${f.codigo} - ${f.descricao}` }))
  );

  getRouteBase(): string {
    return '/dotacoes';
  }

  initForm(): void {
    this.form = this.fb.group({
      exercicio: [new Date().getFullYear(), [Validators.required]],
      codigoDotacao: ['', [Validators.required, Validators.maxLength(50)]],
      orgaoId: [null, Validators.required],
      unidadeId: [null],
      funcaoId: [null, Validators.required],
      subfuncaoId: [null],
      programaId: [null, Validators.required],
      acaoId: [null],
      naturezaDespesaId: [null, Validators.required],
      fonteRecursoId: [null, Validators.required],
      valorInicial: [0, Validators.required],
      valorSuplementado: [0],
      valorAnulado: [0],
      valorEmpenhado: [0],
      loaId: [null],
    });

    this.loadDomainData();
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findDotacaoById(id).subscribe({
      next: (d: DotacaoOrcamentariaResponse) => {
        this.form.patchValue({
          exercicio: d.exercicio,
          codigoDotacao: d.codigoDotacao,
          valorInicial: d.valorInicial,
          valorSuplementado: d.valorSuplementado,
          valorAnulado: d.valorAnulado,
          valorEmpenhado: d.valorEmpenhado,
          loaId: d.loaId,
        });
        this.loadingData.set(false);
      },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    const data: DotacaoOrcamentariaRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updateDotacao(this.entityId()!, data)
      : this.planejamentoService.createDotacao(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Dotação atualizada com sucesso!' : 'Dotação cadastrada com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }

  onOrgaoChange(value: unknown): void {
    const orgaoId = value as number;
    this.form.get('unidadeId')?.setValue(null);
    this.unidades.set([]);
    if (orgaoId) {
      this.planejamentoService.getUnidadesSimples(orgaoId).subscribe(data => this.unidades.set(data));
    }
  }

  onFuncaoChange(value: unknown): void {
    const funcaoId = value as number;
    this.form.get('subfuncaoId')?.setValue(null);
    this.subfuncoes.set([]);
    if (funcaoId) {
      this.planejamentoService.getSubfuncoes(funcaoId).subscribe(data => this.subfuncoes.set(data));
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
    this.planejamentoService.getOrgaosSimples().subscribe(data => this.orgaos.set(data));
    this.planejamentoService.getFuncoes().subscribe(data => this.funcoes.set(data));
    this.planejamentoService.getProgramasSimples().subscribe(data => this.programas.set(data));
    this.planejamentoService.getNaturezasDespesa().subscribe(data => this.naturezas.set(data));
    this.planejamentoService.getFontesRecurso().subscribe(data => this.fontes.set(data));
  }
}
