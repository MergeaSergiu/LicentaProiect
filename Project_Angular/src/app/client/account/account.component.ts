import { Component, OnInit, ViewChild } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { UserDataResponse } from '../../models/user-response.model';
import { UpdateUserRequest } from '../../models/userdata-request.model';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { response } from 'express';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent implements OnInit {
  
  role: string;
  currentUser: UserDataResponse;
  inEditMode = false;
  traininClassReponse: TrainingClassResponse[];
  
  constructor(private registrationService: RegistrationService, private router: Router, private clientService: ClientService ) {
  }

  toggleEditMode(): void {
    this.inEditMode = true;
  }

  ngOnInit():void{
    this.fetchUserData();
    this.role = this.registrationService.getRole();
    if(this.role === 'TRAINER'){
    this.fetchTrainerClassesData();
    }
    this.inEditMode = false;
    
  }

  fetchUserData(){
    this.clientService.getUserProfileData().subscribe({
      next: (response) => {
        this.currentUser = response;
      }
    })
  }

  fetchTrainerClassesData(){
    this.clientService.getTrainerClasses().subscribe({
      next: (response) =>{
        this.traininClassReponse = response;
      }
    })
  }


  saveProfile(currentUser: UserDataResponse): void{
    console.log("A mers");
      const userDataRequest: UpdateUserRequest = {
          firstName: currentUser.firstName,
          lastName: currentUser.lastName
      };
      console.log(userDataRequest);
      this.clientService.updateUserData(userDataRequest).subscribe({
        next: (response) =>{
          this.inEditMode = false;
          this.fetchUserData();
        }
      })
  }

  cancelEdit(): void {
    // Reset editedUser to the original currentUser
    this.inEditMode = false;
    this.fetchUserData();
    
  }

  
  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }
  
}
