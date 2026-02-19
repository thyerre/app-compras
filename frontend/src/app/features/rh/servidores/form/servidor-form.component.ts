import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CargoListItem, ServidorRequest, TipoVinculo } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-servidor-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './servidor-form.component.html',
  styleUrl: './servidor-form.component.scss',
})
export class ServidorFormComponent extends BaseFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly rhService = inject(RhService);
  private readonly snackBar = inject(MatSnackBar);
  form!: FormGroup;

  readonly cargoOptions = signal<SelectOption[]>([]);
  readonly tipoVinculoOptions = signal<SelectOption[]>([]);

  readonly sexoOptions = signal<SelectOption[]>([
    { value: 'M', label: 'Masculino' }, { value: 'F', label: 'Feminino' },
  ]);

  readonly estadoCivilOptions = signal<SelectOption[]>([
    { value: 'SOLTEIRO', label: 'Solteiro(a)' }, { value: 'CASADO', label: 'Casado(a)' },
    { value: 'DIVORCIADO', label: 'Divorciado(a)' }, { value: 'VIUVO', label: 'Viúvo(a)' },
    { value: 'UNIAO_ESTAVEL', label: 'União Estável' },
  ]);

  readonly grauInstrucaoOptions = signal<SelectOption[]>([
    { value: 'FUNDAMENTAL_INCOMPLETO', label: 'Fundamental Incompleto' },
    { value: 'FUNDAMENTAL', label: 'Fundamental Completo' },
    { value: 'MEDIO_INCOMPLETO', label: 'Médio Incompleto' },
    { value: 'MEDIO', label: 'Médio Completo' },
    { value: 'SUPERIOR_INCOMPLETO', label: 'Superior Incompleto' },
    { value: 'SUPERIOR', label: 'Superior Completo' },
    { value: 'POS_GRADUACAO', label: 'Pós-Graduação' },
    { value: 'MESTRADO', label: 'Mestrado' },
    { value: 'DOUTORADO', label: 'Doutorado' },
  ]);

  readonly situacaoOptions = signal<SelectOption[]>([
    { value: 'ATIVO', label: 'Ativo' }, { value: 'AFASTADO', label: 'Afastado' },
    { value: 'CEDIDO', label: 'Cedido' }, { value: 'FERIAS', label: 'Férias' },
    { value: 'LICENCA', label: 'Licença' }, { value: 'DESLIGADO', label: 'Desligado' },
  ]);

  readonly tipoContaOptions = signal<SelectOption[]>([
    { value: 'CORRENTE', label: 'Corrente' }, { value: 'POUPANCA', label: 'Poupança' },
    { value: 'SALARIO', label: 'Salário' },
  ]);

  override ngOnInit(): void {
    super.ngOnInit();
    this.loadLookups();
  }

  getRouteBase(): string { return '/rh/servidores'; }

  initForm(): void {
    this.form = this.fb.group({
      matricula: ['', [Validators.required, Validators.maxLength(20)]],
      nome: ['', [Validators.required, Validators.maxLength(255)]],
      cpf: ['', [Validators.required, Validators.maxLength(14)]],
      rg: [''], rgOrgaoEmissor: [''],
      dataNascimento: [''], sexo: [''], estadoCivil: [''],
      naturalidade: [''], nacionalidade: ['Brasil'],
      nomeMae: [''], nomePai: [''],
      email: [''], telefone: [''], celular: [''],
      cep: [''], logradouro: [''], numero: [''], complemento: [''],
      bairro: [''], municipioId: [null], estadoId: [null],
      pisPasep: [''], ctpsNumero: [''], ctpsSerie: [''],
      cnhNumero: [''], cnhCategoria: [''],
      tituloEleitor: [''], zonaEleitoral: [''], secaoEleitoral: [''],
      grauInstrucao: [''],
      cargoId: [null, [Validators.required]],
      nivelSalarialId: [null],
      tipoVinculoId: [null, [Validators.required]],
      orgaoId: [null], unidadeId: [null],
      dataAdmissao: ['', [Validators.required]],
      dataPosse: [''], dataExercicio: [''],
      dataDemissao: [''], motivoDemissao: [''],
      bancoCodigo: [''], bancoNome: [''], agencia: [''], conta: [''], tipoConta: [''],
      regimePrevidencia: ['RGPS'], numeroPrevidencia: [''],
      situacao: ['ATIVO'],
      ativo: [true],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findServidorById(id).subscribe({
      next: (e) => { this.form.patchValue(e); this.loadingData.set(false); },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const data: ServidorRequest = this.form.value;
    const op = this.isEditMode() ? this.rhService.updateServidor(this.entityId()!, data) : this.rhService.createServidor(data);
    op.subscribe({
      next: () => {
        this.snackBar.open(this.isEditMode() ? 'Servidor atualizado!' : 'Servidor cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
        this.saving.set(false); this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }

  private loadLookups(): void {
    this.rhService.findCargosLookup().subscribe({
      next: (list: CargoListItem[]) => this.cargoOptions.set(list.map(c => ({ value: c.id, label: `${c.codigo} - ${c.descricao}` }))),
    });
    this.rhService.findTiposVinculo().subscribe({
      next: (list: TipoVinculo[]) => this.tipoVinculoOptions.set(list.map(t => ({ value: t.id, label: `${t.codigo} - ${t.descricao}` }))),
    });
  }
}
