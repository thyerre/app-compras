import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import {
    ClassificacaoFornecedor,
    Estado,
    FornecedorRequest,
    FornecedorResponse,
    Municipio,
    TipoFornecedor,
} from '../../../core/models/fornecedor.model';
import { BaseFormComponent } from '../../../shared/bases/base-form.component';
import { FornecedorService } from '../../../shared/services/fornecedor.service';
import { ButtonComponent } from '../../../shared/ui/button/button.component';
import { DatepickerComponent } from '../../../shared/ui/datepicker/datepicker.component';
import { InputComponent } from '../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../shared/ui/select/select.component';

@Component({
  selector: 'app-fornecedor-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatDividerModule,
    MatSnackBarModule,
    ButtonComponent,
    InputComponent,
    SelectComponent,
    DatepickerComponent,
  ],
  templateUrl: './fornecedor-form.component.html',
  styleUrl: './fornecedor-form.component.scss',
})
export class FornecedorFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly fornecedorService = inject(FornecedorService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly estados = signal<Estado[]>([]);
  readonly municipios = signal<Municipio[]>([]);
  readonly tiposFornecedor = signal<TipoFornecedor[]>([]);
  readonly classificacoes = signal<ClassificacaoFornecedor[]>([]);

  readonly tiposFornecedorOptions = computed<SelectOption[]>(() =>
    this.tiposFornecedor().map(t => ({ value: t.id, label: t.descricao }))
  );

  readonly classificacoesOptions = computed<SelectOption[]>(() => [
    { value: null, label: '\u2014' },
    ...this.classificacoes().map(c => ({ value: c.id, label: c.descricao })),
  ]);

  readonly estadosOptions = computed<SelectOption[]>(() =>
    this.estados().map(e => ({ value: e.id, label: `${e.sigla} - ${e.nome}` }))
  );

  readonly municipiosOptions = computed<SelectOption[]>(() =>
    this.municipios().map(m => ({ value: m.id, label: m.nome }))
  );

  getRouteBase(): string {
    return '/fornecedores';
  }

  initForm(): void {
    this.form = this.fb.group({
      // Dados principais
      razaoSocial: ['', [Validators.required, Validators.maxLength(255)]],
      nomeFantasia: ['', Validators.maxLength(255)],
      cnpjCpf: ['', [Validators.required, Validators.maxLength(18)]],
      inscricaoEstadual: ['', Validators.maxLength(20)],
      inscricaoMunicipal: ['', Validators.maxLength(20)],
      tipoFornecedorId: [null, Validators.required],
      classificacaoId: [null],

      // Endereço
      cep: ['', [Validators.required, Validators.maxLength(10)]],
      logradouro: ['', [Validators.required, Validators.maxLength(255)]],
      numero: ['', [Validators.required, Validators.maxLength(20)]],
      complemento: ['', Validators.maxLength(100)],
      bairro: ['', [Validators.required, Validators.maxLength(100)]],
      estadoId: [null, Validators.required],
      municipioId: [null, Validators.required],

      // Contato
      telefone: ['', Validators.maxLength(20)],
      celular: ['', Validators.maxLength(20)],
      email: ['', Validators.maxLength(255)],

      // Responsável legal
      responsavelNome: ['', Validators.maxLength(200)],
      responsavelCpf: ['', Validators.maxLength(14)],
      responsavelRg: ['', Validators.maxLength(20)],
      responsavelCargo: ['', Validators.maxLength(100)],

      // Dados bancários
      bancoNome: ['', Validators.maxLength(100)],
      bancoAgencia: ['', Validators.maxLength(20)],
      bancoConta: ['', Validators.maxLength(30)],

      // Status
      ativo: [true],
      observacoes: [''],

      // Certidões
      certidoes: this.fb.array([]),
    });

    this.loadDomainData();
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.fornecedorService.findById(id).subscribe({
      next: (fornecedor) => this.patchForm(fornecedor),
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    const data: FornecedorRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.fornecedorService.update(this.entityId()!, data)
      : this.fornecedorService.create(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Fornecedor atualizado com sucesso!' : 'Fornecedor cadastrado com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }

  // ─── Certidões ───

  get certidoes(): FormArray {
    return this.form.get('certidoes') as FormArray;
  }

  addCertidao(): void {
    this.certidoes.push(
      this.fb.group({
        id: [null],
        nome: ['', Validators.required],
        numero: [''],
        dataEmissao: [null],
        dataValidade: [null],
        arquivoNome: [''],
        arquivoPath: [''],
        observacoes: [''],
      })
    );
  }

  removeCertidao(index: number): void {
    this.certidoes.removeAt(index);
  }

  // ─── Estado → Município cascade ───

  onEstadoChange(value: unknown): void {
    const estadoId = value as number;
    this.form.get('municipioId')?.setValue(null);
    this.municipios.set([]);

    if (estadoId) {
      this.fornecedorService.getMunicipios(estadoId).subscribe(municipios => {
        this.municipios.set(municipios);
      });
    }
  }

  // ─── Helpers ───

  getError(field: string): string {
    const control = this.form.get(field);
    if (control && control.touched && control.errors) {
      if (control.errors['required']) return 'Campo obrigatório';
      if (control.errors['maxlength']) return `Máximo de ${control.errors['maxlength'].requiredLength} caracteres`;
    }
    return '';
  }

  private loadDomainData(): void {
    this.fornecedorService.getEstados().subscribe(data => this.estados.set(data));
    this.fornecedorService.getTiposFornecedor().subscribe(data => this.tiposFornecedor.set(data));
    this.fornecedorService.getClassificacoes().subscribe(data => this.classificacoes.set(data));
  }

  private patchForm(f: FornecedorResponse): void {
    // If there's a estado, load its municipios first
    if (f.estado?.id) {
      this.fornecedorService.getMunicipios(f.estado.id).subscribe(municipios => {
        this.municipios.set(municipios);

        this.form.patchValue({
          razaoSocial: f.razaoSocial,
          nomeFantasia: f.nomeFantasia,
          cnpjCpf: f.cnpjCpf,
          inscricaoEstadual: f.inscricaoEstadual,
          inscricaoMunicipal: f.inscricaoMunicipal,
          tipoFornecedorId: f.tipoFornecedor?.id,
          classificacaoId: f.classificacao?.id,
          cep: f.cep,
          logradouro: f.logradouro,
          numero: f.numero,
          complemento: f.complemento,
          bairro: f.bairro,
          estadoId: f.estado?.id,
          municipioId: f.municipio?.id,
          telefone: f.telefone,
          celular: f.celular,
          email: f.email,
          responsavelNome: f.responsavelNome,
          responsavelCpf: f.responsavelCpf,
          responsavelRg: f.responsavelRg,
          responsavelCargo: f.responsavelCargo,
          bancoNome: f.bancoNome,
          bancoAgencia: f.bancoAgencia,
          bancoConta: f.bancoConta,
          ativo: f.ativo,
          observacoes: f.observacoes,
        });

        // Patch certidões
        this.certidoes.clear();
        (f.certidoes || []).forEach(c => {
          this.certidoes.push(
            this.fb.group({
              id: [c.id],
              nome: [c.nome, Validators.required],
              numero: [c.numero],
              dataEmissao: [c.dataEmissao],
              dataValidade: [c.dataValidade],
              arquivoNome: [c.arquivoNome],
              arquivoPath: [c.arquivoPath],
              observacoes: [c.observacoes],
            })
          );
        });

        this.loadingData.set(false);
      });
    } else {
      this.loadingData.set(false);
    }
  }
}
