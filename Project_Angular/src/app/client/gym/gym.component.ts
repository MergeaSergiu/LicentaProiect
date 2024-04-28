import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { ClientService } from '../../services/client.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UtilComponentComponent } from '../../util-component/util-component.component';

@Component({
  selector: 'app-gym',
  templateUrl: './gym.component.html',
  styleUrl: './gym.component.css'
})
export class GymComponent implements OnInit{

  selectedTrainingClass: TrainingClassResponse;
  subscriptions = [];
  trainingClassesData: TrainingClassResponse[];
  userTrainingClassesData: TrainingClassResponse[];
  selectedTrainingClassId: number;
  hasActiveSubscription: boolean;
  successfulMessage: string;
  panelOpenState = false;
  constructor(private router: Router, private adminService: AdminService, private clientService: ClientService, private _responseBar: MatSnackBar){}

  ngOnInit(): void{
    this.fetchTrainingClassesData();
    this.fetchUserTrainingClassesData();
    this.fetchSubscriptions();
    this.checkUserActiveSubscription();
  }

  selectionChangeHandler(event: any) {
    this.selectedTrainingClassId = event.value;
    this.fetchTrainingClassData(this.selectedTrainingClassId);
  }

  checkUserActiveSubscription(){
    this.clientService.checkUserActiveSubscriptions().subscribe({
      next: (response) => {
          this.hasActiveSubscription = response.hasActiveSubscription;
      }
    })
  }

  fetchTrainingClassesData(){
    this.adminService.getTrainingClassesData().subscribe({
      next: (response) => {
        this.trainingClassesData = response;
        console.log(this.trainingClassesData);
      }
    })
  }

  fetchTrainingClassData(id: number){
    this.adminService.getTrainingClassData(id).subscribe({
      next:(response) => {
        this.selectedTrainingClass = response;
  
      }
    })
  }


  enrollUserToTrainingClass(classId: number){
    this.clientService.enrollUserToTrainingClass(classId).subscribe({
      next:(response) =>{
        this.fetchUserTrainingClassesData();
        UtilComponentComponent.openSnackBar("You have enrolled to training Class", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
      },error:(error) =>{
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }


  fetchUserTrainingClassesData(){
    this.clientService.getUserTrainingClasses().subscribe({
      next: (response) => {
        this.userTrainingClassesData = response;
      }
    })
  }

  dropOutOfTrainingClass(classId: number){
    this.clientService.unEnrolleUser(classId).subscribe({
      next: (response) =>{
        this.fetchTrainingClassData(classId);
        this.fetchUserTrainingClassesData();
        UtilComponentComponent.openSnackBar("You have drop out from training Class", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  fetchSubscriptions(){
    return this.adminService.getAllSubscriptions().subscribe({
      next: (response) => {
        console.log(response);
        this.subscriptions = response;
      }
    })
  }

  goToCheckoutPage(subscriptionId: number) {
    this.router.navigate(['/client/checkout'], { queryParams: { subscId: subscriptionId } });
  }

}
