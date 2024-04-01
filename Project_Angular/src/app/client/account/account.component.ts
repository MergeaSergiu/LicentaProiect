import { Component, OnInit, ViewChild} from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { UserDataResponse } from '../../models/user-response.model';
import { UpdateUserRequest } from '../../models/userdata-request.model';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { UserSubscriptionsDataResponse } from '../../models/userSubscriptionData-response.model';
import { response } from 'express';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';

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
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  displayedColumns: string[] = ['Subscription', 'Price', 'StartDate', 'EndDate'];
  
  constructor(private registrationService: RegistrationService, private router: Router, private clientService: ClientService, private _responseBar: MatSnackBar ) {}

  toggleEditMode(): void {
    this.inEditMode = true;
  }

  ngOnInit():void{
    this.fetchUserData();
    this.fetchUserSubscriptions();
    this.role = this.registrationService.getRole();
    if(this.role === 'TRAINER'){
      this.fetchTrainerClassesData();
    }
    
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
      const userDataRequest: UpdateUserRequest = {
          firstName: currentUser.firstName,
          lastName: currentUser.lastName
      };
      this.clientService.updateUserData(userDataRequest).subscribe({
        next: (response) =>{
          this.inEditMode = false;
          this.fetchUserData();
          UtilComponentComponent.openSnackBar("Your data was updated", this._responseBar, UtilComponentComponent.SnackbarStates.Default);
        }
      })
  }

  cancelEdit(): void {
    this.inEditMode = false;
    this.fetchUserData();
  }

  
  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }

  public fetchUserSubscriptions(){
    this.clientService.getUserSubscriptionsData().subscribe({
        next: (response) => {
          this.userSubscriptionsData = response;
          this.dataSource = new MatTableDataSource<any>(this.userSubscriptionsData);
          this.dataSource.paginator = this.paginator;
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
