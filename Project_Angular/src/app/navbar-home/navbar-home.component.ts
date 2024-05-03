import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-home',
  templateUrl: './navbar-home.component.html',
  styleUrl: './navbar-home.component.css'
})
export class NavbarHomeComponent {

  constructor(private router: Router){}

  public goToLogin(){
    this.router.navigate(['/login'])
  }

  public goToSignup(){
    this.router.navigate(['/authentication'])
  }

  public goToHomePage(){
    this.router.navigate(['/home'])
  }

}
