import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from '../services/session.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);
  const isPublic = route.data?.['public'];

  if (isPublic) {
    if (sessionService.isLogged) {
      return router.createUrlTree(['/feed']);
    }
    return true;
  }

  if (sessionService.isLogged) {
    return true;
  }

  return router.createUrlTree(['/login']);
};