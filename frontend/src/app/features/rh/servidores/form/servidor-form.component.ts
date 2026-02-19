import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CargoListItem, RoleItem, ServidorRequest, TipoVinculo } from '../../../../core/models/rh.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { RhService } from '../../../../shared/services/rh.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-servidor-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, MatCheckboxModule, MatSlideToggleModule, ButtonComponent, InputComponent, SelectComponent],
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
  readonly availableRoles = signal<RoleItem[]>([]);
  readonly criarAcesso = signal(false);
  readonly hasExistingUser = signal(false);

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
      // Acesso ao sistema
      senhaUsuario: [''],
      confirmarSenha: [''],
      roleNames: [[] as string[]],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.rhService.findServidorById(id).subscribe({
      next: (e) => {
        this.form.patchValue(e);
        // Se já tem usuário vinculado, ativar seção de acesso
        if (e.usuarioId) {
          this.criarAcesso.set(true);
          this.hasExistingUser.set(true);
          this.form.patchValue({ roleNames: e.roles || [] });
        }
        this.loadingData.set(false);
      },
      error: () => this.loadingData.set(false),
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    // Validar senhas se está criando acesso
    if (this.criarAcesso()) {
      const senha = this.form.get('senhaUsuario')?.value;
      const confirmar = this.form.get('confirmarSenha')?.value;

      // Senha obrigatória apenas ao criar novo acesso (não ao editar)
      if (!this.hasExistingUser() && (!senha || senha.length < 6)) {
        this.snackBar.open('A senha deve ter pelo menos 6 caracteres.', 'Fechar', { duration: 4000, horizontalPosition: 'center', verticalPosition: 'top' });
        return;
      }

      if (senha && senha !== confirmar) {
        this.snackBar.open('As senhas não conferem.', 'Fechar', { duration: 4000, horizontalPosition: 'center', verticalPosition: 'top' });
        return;
      }

      if (!this.form.get('email')?.value) {
        this.snackBar.open('O e-mail é obrigatório para criar acesso ao sistema.', 'Fechar', { duration: 4000, horizontalPosition: 'center', verticalPosition: 'top' });
        return;
      }
    }

    this.saving.set(true);
    const formValue = { ...this.form.value };
    delete formValue.confirmarSenha; // Remover campo auxiliar

    // Se não está criando acesso, limpar campos de usuário
    if (!this.criarAcesso()) {
      delete formValue.senhaUsuario;
      delete formValue.roleNames;
    } else if (!formValue.senhaUsuario) {
      delete formValue.senhaUsuario; // Não enviar senha vazia no update
    }

    const data: ServidorRequest = formValue;
    const op = this.isEditMode() ? this.rhService.updateServidor(this.entityId()!, data) : this.rhService.createServidor(data);
    op.subscribe({
      next: () => {
        this.snackBar.open(this.isEditMode() ? 'Servidor atualizado!' : 'Servidor cadastrado!', 'Fechar', { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
        this.saving.set(false); this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }

  toggleCriarAcesso(checked: boolean): void {
    this.criarAcesso.set(checked);
  }

  onRoleToggle(roleName: string, checked: boolean): void {
    const current: string[] = this.form.get('roleNames')?.value || [];
    if (checked) {
      this.form.patchValue({ roleNames: [...current, roleName] });
    } else {
      this.form.patchValue({ roleNames: current.filter(r => r !== roleName) });
    }
  }

  isRoleSelected(roleName: string): boolean {
    const roles: string[] = this.form.get('roleNames')?.value || [];
    return roles.includes(roleName);
  }

  private loadLookups(): void {
    this.rhService.findCargosLookup().subscribe({
      next: (list: CargoListItem[]) => this.cargoOptions.set(list.map(c => ({ value: c.id, label: `${c.codigo} - ${c.descricao}` }))),
    });
    this.rhService.findTiposVinculo().subscribe({
      next: (list: TipoVinculo[]) => this.tipoVinculoOptions.set(list.map(t => ({ value: t.id, label: `${t.codigo} - ${t.descricao}` }))),
    });
    this.rhService.findRoles().subscribe({
      next: (roles: RoleItem[]) => this.availableRoles.set(roles),
    });
  }
}
