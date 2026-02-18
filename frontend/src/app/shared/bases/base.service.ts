import { inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export abstract class BaseService<T> {
  protected readonly http = inject(HttpClient);
  protected readonly apiUrl = environment.apiUrl;

  protected abstract get endpoint(): string;

  protected get fullUrl(): string {
    return `${this.apiUrl}/${this.endpoint}`;
  }

  getAll(params?: Record<string, string>): Observable<T[]> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        httpParams = httpParams.set(key, params[key]);
      });
    }
    return this.http.get<T[]>(this.fullUrl, { params: httpParams });
  }

  getById(id: number | string): Observable<T> {
    return this.http.get<T>(`${this.fullUrl}/${id}`);
  }

  create(data: Partial<T>): Observable<T> {
    return this.http.post<T>(this.fullUrl, data);
  }

  update(id: number | string, data: Partial<T>): Observable<T> {
    return this.http.put<T>(`${this.fullUrl}/${id}`, data);
  }

  delete(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.fullUrl}/${id}`);
  }
}
