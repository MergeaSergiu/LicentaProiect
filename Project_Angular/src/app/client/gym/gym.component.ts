import { Component, Input, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { ClientService } from '../../services/client.service';
import { MatSnackBar} from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { SubscriptionResponse } from '../../models/subscription-response.model';
import { error } from 'console';

@Component({
  selector: 'app-gym',
  templateUrl: './gym.component.html',
  styleUrl: './gym.component.css',
})
export class GymComponent implements OnInit{

  selectedTrainingClass: TrainingClassResponse;
  subscriptions : SubscriptionResponse[];
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
      }
    })
  }

  fetchTrainingClassData(id: number){
    this.adminService.getTrainingClassData(id).subscribe({
      next:(response) => {
        this.selectedTrainingClass = response;
      },error:(error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }


  enrollUserToTrainingClass(classId: number){
    this.clientService.enrollUserToTrainingClass(classId).subscribe({
      next:(response) =>{
        UtilComponentComponent.openSnackBar("You have enrolled to training Class", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
        this.fetchUserTrainingClassesData();
      },error:(error) =>{
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }


  fetchUserTrainingClassesData(){
    this.clientService.getUserTrainingClasses().subscribe({
      next: (response) => {
        this.userTrainingClassesData = response;
      },error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  dropOutOfTrainingClass(classId: number){
    this.clientService.unEnrolleUser(classId).subscribe({
      next: () =>{
        UtilComponentComponent.openSnackBar("You have drop out from training Class", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        this.fetchTrainingClassData(classId);
        this.fetchUserTrainingClassesData();
      },error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  fetchSubscriptions(){
    return this.adminService.getAllSubscriptions().subscribe({
      next: (response) => {
        this.subscriptions = response;
      }
    })
  }

  goToCheckoutPage(id: number, subscriptionPrice: number) {
     this.router.navigate(['/client/checkout'], { state: { price: subscriptionPrice, subscriptionId: id } });
  }

}
