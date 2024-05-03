import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { RegistrationService } from '../../services/registration.service';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
   state: RouterStateSnapshot
  ) => {
  const registrationService = inject(RegistrationService);
  const router = inject(Router);

  if (registrationService.isLoggedIn()) {
    const role = route.data["roles"] as Array<string>;
    if (role && role.includes(registrationService.getRole())) {
      return true;
    }else {
      router.navigate(['login']);
      registrationService.clear();
      return false;
  }
 } else {
    router.navigate(['login']);
    registrationService.clear();
    return false;
  }
};

