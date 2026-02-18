import { Injectable, signal, computed } from '@angular/core';
import { BaseState, BaseStateProps } from '../../../shared/bases/base.state';
import { authStore, AuthStateProps } from './auth.store';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest, Usuario } from '../../../core/models/auth.model';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthState extends BaseState<AuthStateProps> {
  protected override store = authStore;

  private readonly _usuario = signal<Usuario | null>(null);
  private readonly _isAuthenticated = signal(false);

  readonly usuario = this._usuario.asReadonly();
  readonly isAuthenticated = this._isAuthenticated.asReadonly();
  readonly userName = computed(() => this._usuario()?.nome ?? '');

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    super();
    // Sincronizar com AuthService
    const storedUser = this.authService.usuario();
    if (storedUser) {
      this._usuario.set(storedUser);
      this._isAuthenticated.set(true);
    }
  }

  login(credentials: LoginRequest): void {
    this.setLoading(true);
    this.setError(null);

    this.authService.login(credentials).subscribe({
      next: (response) => {
        this._usuario.set(response.usuario);
        this._isAuthenticated.set(true);
        this.setLoading(false);

        authStore.update(state => ({
          ...state,
          usuario: response.usuario,
          token: response.token,
          isAuthenticated: true,
          loading: false,
          error: null,
        }));

        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        const message = error?.error?.error || 'Erro ao realizar login';
        this.setError(message);
        this.setLoading(false);
      },
    });
  }

  logout(): void {
    this.authService.logout();
    this._usuario.set(null);
    this._isAuthenticated.set(false);
    authStore.update(() => ({
      loading: false,
      error: null,
      usuario: null,
      token: null,
      isAuthenticated: false,
    }));
    this.router.navigate(['/login']);
  }
}
