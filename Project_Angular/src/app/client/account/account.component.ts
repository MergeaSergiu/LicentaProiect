import { Component, OnInit, ViewChild} from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { UserDataResponse } from '../../models/user-response.model';
import { UpdateUserRequest } from '../../models/userdata-request.model';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { UserSubscriptionsDataResponse } from '../../models/userSubscriptionData-response.model';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { error } from 'console';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent implements OnInit {
  
  role: string;
  currentUser: UserDataResponse;
  userSubscriptionsData : UserSubscriptionsDataResponse[];
  inEditMode = false;
  traininClassReponse: TrainingClassResponse[];
  panelOpenState = false;
  
  constructor(private registrationService: RegistrationService, private router: Router, private clientService: ClientService, private _responseBar: MatSnackBar ) {}

  toggleEditMode(): void {
    this.inEditMode = true;
  }

  ngOnInit():void{
    this.fetchUserData();
    this.role = this.registrationService.getRole();
    if(this.role === 'USER'){
      this.fetchUserSubscriptions();
    }

    if(this.role === 'TRAINER'){
      this.fetchTrainerClassesData();
    }
    
  }

  fetchUserData(){
    this.clientService.getUserProfileData().subscribe({
      next: (response) => {
        this.currentUser = response;
      },error: (error) =>{
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
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
      const userDataRequest: UpdateUserRequest = {
          firstName: currentUser.firstName,
          lastName: currentUser.lastName
      };
      this.clientService.updateUserData(userDataRequest).subscribe({
        next: () =>{
          this.inEditMode = false;
          this.fetchUserData();
          UtilComponentComponent.openSnackBar("Your data was updated", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
        },error:(error) =>{
          UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        }
      })
  }

  cancelEdit(): void {
    this.inEditMode = false;
    this.fetchUserData();
  }

  public fetchUserSubscriptions(){
    this.clientService.getUserSubscriptionsData().subscribe({
        next: (response) => {
          this.userSubscriptionsData = response;
        },error:(error) =>{
          UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        }
    })
  }

  isSubscriptionActive(endDate: Date): boolean{
    const today = new Date();
    const endDateOnly = new Date(endDate);
    endDateOnly.setHours(0,0,0,0);
    today.setHours(0,0,0,0);
    return endDateOnly >= today;
  }
  
}
