import { Component } from '@angular/core';
import { RegistrationService } from '../services/registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent {
  constructor(private registrationService: RegistrationService, private router: Router ) {}


redirectToAccount() {
  this.router.navigate(['/client/account']);
  }
redirectToReservations() {
  this.router.navigate(['/client/reservations']);
}
redirectToTrainers() {
  this.router.navigate(['/client/trainers']);
}
redirectToGym() {
  this.router.navigate(['/client/gym']);
}

  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }

}
