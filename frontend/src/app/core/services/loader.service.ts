import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoaderService {
  private readonly _loading = signal(false);
  private activeRequests = 0;

  readonly loading = this._loading.asReadonly();

  show(): void {
    this.activeRequests++;
    this._loading.set(true);
  }

  hide(): void {
    this.activeRequests--;
    if (this.activeRequests <= 0) {
      this.activeRequests = 0;
      this._loading.set(false);
    }
  }
}
