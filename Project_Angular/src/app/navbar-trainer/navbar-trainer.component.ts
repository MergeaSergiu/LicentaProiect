import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationService } from '../services/registration.service';

@Component({
  selector: 'app-navbar-trainer',
  templateUrl: './navbar-trainer.component.html',
  styleUrl: './navbar-trainer.component.css'
})
export class NavbarTrainerComponent {

  constructor(private router: Router, private registrationService: RegistrationService){}

  public goToProfile(){
    this.router.navigate(['/trainer/account'])
  }

  goToCollaborations(){
    this.router.navigate(['/trainer/trainerDashboard'])
  }

  public logOut(){
    this.registrationService.clear();
    this.router.navigate(['/home']);
  }
}
