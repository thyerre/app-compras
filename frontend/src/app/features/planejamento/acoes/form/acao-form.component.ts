import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import {
    AcaoRequest,
    AcaoResponse,
    Funcao,
    ProgramaListItem,
    Subfuncao,
} from '../../../../core/models/planejamento.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { PlanejamentoService } from '../../../../shared/services/planejamento.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-acao-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatCheckboxModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './acao-form.component.html',
  styleUrl: './acao-form.component.scss',
})
export class AcaoFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly planejamentoService = inject(PlanejamentoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly funcoes = signal<Funcao[]>([]);
  readonly subfuncoes = signal<Subfuncao[]>([]);
  readonly programas = signal<ProgramaListItem[]>([]);

  readonly tiposOptions: SelectOption[] = [
    { value: 'PROJETO', label: 'Projeto' },
    { value: 'ATIVIDADE', label: 'Atividade' },
    { value: 'OPERACAO_ESPECIAL', label: 'Operação Especial' },
  ];

  readonly funcoesOptions = computed<SelectOption[]>(() =>
    this.funcoes().map(f => ({ value: f.id, label: `${f.codigo} - ${f.descricao}` }))
  );

  readonly subfuncoesOptions = computed<SelectOption[]>(() =>
    this.subfuncoes().map(s => ({ value: s.id, label: `${s.codigo} - ${s.descricao}` }))
  );

  readonly programasOptions = computed<SelectOption[]>(() =>
    this.programas().map(p => ({ value: p.id, label: `${p.codigo} - ${p.nome}` }))
  );

  getRouteBase(): string {
    return '/acoes';
  }

  initForm(): void {
    this.form = this.fb.group({
      codigo: ['', [Validators.required, Validators.maxLength(10)]],
      nome: ['', [Validators.required, Validators.maxLength(200)]],
      tipo: ['', Validators.required],
      descricao: [''],
      funcaoId: [null, Validators.required],
      subfuncaoId: [null],
      programaId: [null, Validators.required],
      ativo: [true],
    });

    this.loadDomainData();
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.planejamentoService.findAcaoById(id).subscribe({
      next: (a: AcaoResponse) => {
        if (a.funcaoId) {
          this.planejamentoService.getSubfuncoes(a.funcaoId).subscribe(subs => this.subfuncoes.set(subs));
        }
        this.form.patchValue({
          codigo: a.codigo,
          nome: a.nome,
          tipo: a.tipo,
          descricao: a.descricao,
          funcaoId: a.funcaoId,
          subfuncaoId: a.subfuncaoId,
          programaId: a.programaId,
          ativo: a.ativo,
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
    const data: AcaoRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.planejamentoService.updateAcao(this.entityId()!, data)
      : this.planejamentoService.createAcao(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Ação atualizada com sucesso!' : 'Ação cadastrada com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
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
    this.planejamentoService.getFuncoes().subscribe(data => this.funcoes.set(data));
    this.planejamentoService.getProgramasSimples().subscribe(data => this.programas.set(data));
  }
}
