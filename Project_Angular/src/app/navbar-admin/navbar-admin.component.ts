import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationService } from '../services/registration.service';

@Component({
  selector: 'app-navbar-admin',
  templateUrl: './navbar-admin.component.html',
  styleUrl: './navbar-admin.component.css'
})
export class NavbarAdminComponent {

  constructor(private router: Router, private registrationService: RegistrationService){}
  
  public goToReservationsDetails(){
    this.router.navigate(['/admin/reservations']);
  }

  public goToUsersDetails(){
    this.router.navigate(['admin/users'])
  }

  public goToGymDetails(){
    this.router.navigate(['/admin/gym']);
  }

  public logOut(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }


}
