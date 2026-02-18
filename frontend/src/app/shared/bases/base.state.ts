import { Store, createStore, withProps, select } from '@ngneat/elf';
import { Signal, signal, computed } from '@angular/core';

export interface BaseStateProps {
  loading: boolean;
  error: string | null;
}

export const initialBaseState: BaseStateProps = {
  loading: false,
  error: null,
};

export abstract class BaseState<T extends BaseStateProps> {
  protected abstract store: Store;

  protected readonly _loading = signal(false);
  protected readonly _error = signal<string | null>(null);

  readonly loading = this._loading.asReadonly();
  readonly error = this._error.asReadonly();

  setLoading(loading: boolean): void {
    this._loading.set(loading);
    this.store.update((state: T) => ({ ...state, loading }));
  }

  setError(error: string | null): void {
    this._error.set(error);
    this.store.update((state: T) => ({ ...state, error }));
  }

  reset(): void {
    this._loading.set(false);
    this._error.set(null);
  }
}
