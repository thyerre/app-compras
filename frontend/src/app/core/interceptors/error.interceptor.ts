import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Skip 401 — handled by authInterceptor
      if (error.status === 401) {
        return throwError(() => error);
      }

      const body = error.error;
      let message = '';

      if (body?.message) {
        message = body.message;
      } else {
        message = 'Ocorreu um erro interno. Tente novamente mais tarde.';
      }

      snackBar.open(message, 'Fechar', {
        duration: 6000,
        panelClass: ['snackbar-error'],
        horizontalPosition: 'center',
        verticalPosition: 'top',
      });

      return throwError(() => error);
    })
  );
};
