import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoaderService } from '../services/loader.service';

const MUTATION_METHODS = ['POST', 'PUT', 'DELETE', 'PATCH'];

export const loaderInterceptor: HttpInterceptorFn = (req, next) => {
  const loaderService = inject(LoaderService);

  if (MUTATION_METHODS.includes(req.method.toUpperCase())) {
    loaderService.show();

    return next(req).pipe(
      finalize(() => loaderService.hide())
    );
  }

  return next(req);
};
