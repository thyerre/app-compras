import { createStore, withProps } from '@ngneat/elf';
import { Usuario } from '../../../core/models/auth.model';

export interface AuthStateProps {
  loading: boolean;
  error: string | null;
  usuario: Usuario | null;
  token: string | null;
  isAuthenticated: boolean;
}

export const initialAuthState: AuthStateProps = {
  loading: false,
  error: null,
  usuario: null,
  token: null,
  isAuthenticated: false,
};

export const authStore = createStore(
  { name: 'auth' },
  withProps<AuthStateProps>(initialAuthState)
);
