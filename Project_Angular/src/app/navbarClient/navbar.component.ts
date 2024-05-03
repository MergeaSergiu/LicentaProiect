import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationService } from '../services/registration.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(private router: Router, private registrationService: RegistrationService){

  }

  goToReservations(){
    this.router.navigate(['/client/reservations']);
  }

  goToGym(){
    this.router.navigate(['/client/gym']);
  }

  goToTrainers(){
    this.router.navigate(['/client/trainers']);
  }

  goToProfile(){
    this.router.navigate(['/client/account']);
  }

  logOut(){
    this.registrationService.clear();
    this.router.navigate(['/home']);
  }




}
