import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ContribuinteRequest } from '../../../../core/models/arrecadacao.model';
import { BaseFormComponent } from '../../../../shared/bases/base-form.component';
import { ArrecadacaoService } from '../../../../shared/services/arrecadacao.service';
import { ButtonComponent } from '../../../../shared/ui/button/button.component';
import { InputComponent } from '../../../../shared/ui/input/input.component';
import { SelectComponent, SelectOption } from '../../../../shared/ui/select/select.component';

@Component({
  selector: 'app-contribuinte-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, MatIconModule, MatSnackBarModule, ButtonComponent, InputComponent, SelectComponent],
  templateUrl: './contribuinte-form.component.html',
  styleUrl: './contribuinte-form.component.scss',
})
export class ContribuinteFormComponent extends BaseFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly arrecadacaoService = inject(ArrecadacaoService);
  private readonly snackBar = inject(MatSnackBar);

  form!: FormGroup;

  readonly tipoPessoaOptions = signal<SelectOption[]>([
    { value: 'PF', label: 'Pessoa Física' },
    { value: 'PJ', label: 'Pessoa Jurídica' },
  ]);

  readonly ativoOptions = signal<SelectOption[]>([
    { value: true, label: 'Sim' },
    { value: false, label: 'Não' },
  ]);

  getRouteBase(): string {
    return '/arrecadacao/contribuintes';
  }

  initForm(): void {
    this.form = this.fb.group({
      tipoPessoa: ['PF', [Validators.required]],
      cpfCnpj: ['', [Validators.required, Validators.maxLength(18)]],
      nomeRazaoSocial: ['', [Validators.required, Validators.maxLength(255)]],
      nomeFantasia: [''],
      inscricaoMunicipal: [''],
      email: ['', [Validators.email]],
      telefone: [''],
      celular: [''],
      cep: [''],
      logradouro: [''],
      numero: [''],
      complemento: [''],
      bairro: [''],
      municipioId: [null],
      estadoId: [null],
      ativo: [true],
      observacoes: [''],
    });
  }

  loadEntity(id: number): void {
    this.loadingData.set(true);
    this.arrecadacaoService.findContribuinteById(id).subscribe({
      next: (entity) => {
        this.form.patchValue({
          tipoPessoa: entity.tipoPessoa,
          cpfCnpj: entity.cpfCnpj,
          nomeRazaoSocial: entity.nomeRazaoSocial,
          nomeFantasia: entity.nomeFantasia ?? '',
          inscricaoMunicipal: entity.inscricaoMunicipal ?? '',
          email: entity.email ?? '',
          telefone: entity.telefone ?? '',
          celular: entity.celular ?? '',
          cep: entity.cep ?? '',
          logradouro: entity.logradouro ?? '',
          numero: entity.numero ?? '',
          complemento: entity.complemento ?? '',
          bairro: entity.bairro ?? '',
          municipioId: entity.municipioId ?? null,
          estadoId: entity.estadoId ?? null,
          ativo: entity.ativo ?? true,
          observacoes: entity.observacoes ?? '',
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
    const data: ContribuinteRequest = this.form.value;

    const operation = this.isEditMode()
      ? this.arrecadacaoService.updateContribuinte(this.entityId()!, data)
      : this.arrecadacaoService.createContribuinte(data);

    operation.subscribe({
      next: () => {
        this.snackBar.open(
          this.isEditMode() ? 'Contribuinte atualizado com sucesso!' : 'Contribuinte cadastrado com sucesso!',
          'Fechar',
          { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' }
        );
        this.saving.set(false);
        this.navigateBack();
      },
      error: () => this.saving.set(false),
    });
  }
}
