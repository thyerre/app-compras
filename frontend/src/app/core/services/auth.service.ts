import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, Usuario, MenuItemModel } from '../models/auth.model';

const TOKEN_KEY = 'compras_token';
const USER_KEY = 'compras_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = environment.apiUrl;

  private readonly _usuario = signal<Usuario | null>(this.loadStoredUser());
  private readonly _token = signal<string | null>(this.loadStoredToken());

  readonly usuario = this._usuario.asReadonly();
  readonly isLoggedIn = computed(() => !!this._token());
  readonly userName = computed(() => this._usuario()?.nome ?? '');

  constructor(private readonly http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        this._token.set(response.token);
        this._usuario.set(response.usuario);
        localStorage.setItem(TOKEN_KEY, response.token);
        localStorage.setItem(USER_KEY, JSON.stringify(response.usuario));
      })
    );
  }

  logout(): void {
    this._token.set(null);
    this._usuario.set(null);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }

  getToken(): string | null {
    return this._token();
  }

  isAuthenticated(): boolean {
    return !!this._token();
  }

  getMenus(): Observable<MenuItemModel[]> {
    return this.http.get<MenuItemModel[]>(`${this.apiUrl}/menus`);
  }

  private loadStoredToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem(TOKEN_KEY);
    }
    return null;
  }

  private loadStoredUser(): Usuario | null {
    if (typeof window !== 'undefined') {
      const stored = localStorage.getItem(USER_KEY);
      return stored ? JSON.parse(stored) : null;
    }
    return null;
  }
}
