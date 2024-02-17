import { Component } from '@angular/core';
import { RegistrationService } from '../services/registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent {

  constructor(private registrationService: RegistrationService, private router: Router ) {
      
    }


redirectToAccount() {
  this.router.navigate(['/account']);
  }
redirectToReservations() {
  this.router.navigate(['/reservations']);
}
redirectToTrainers() {
  this.router.navigate(['/trainers']);
}
redirectToGym() {
  this.router.navigate(['/gym']);
}

  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/']);
  }

}
