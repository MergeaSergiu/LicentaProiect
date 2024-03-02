import { Component, ViewChild } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ClientService } from '../../services/client.service';
import { response } from 'express';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  
  constructor(private registrationService: RegistrationService, private router: Router, private clientService: ClientService ) {
    
  }

  ngOnInit():void{}

  
  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }
  
}
