import { Component } from '@angular/core';
import { RegistrationService } from '../services/registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  constructor(private registrationService: RegistrationService, private router: Router) { }

  ngOnInit(): void { }

  redirectToReservationsHistory() {
    this.router.navigate(['/admin/reservations']);
  }

  redirectToGymInformation() {
    this.router.navigate(['/admin/gym']);
  }

  redirectToUsersInformation() {
    this.router.navigate(['/admin/users'])
  }

  public logout() {
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }
}
