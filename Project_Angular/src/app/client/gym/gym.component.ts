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
  isEnrolled: boolean;
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

  isEnrolledStatus(id: number){
    return this.checkEnrollmentStatus(id);
  }

  closeCard() {
    this.selectedTrainingClassId = null;
    this.selectedTrainingClass = null;
    this.isEnrolled = false;
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
        this.checkEnrollmentStatus(id);
      }
    })
  }

  checkEnrollmentStatus(selectedTrainingClass: number){
    this.clientService.checkEnrollmentStatus(selectedTrainingClass).subscribe({
        next: (response) => {
          this.isEnrolled = response.status === "enrolled";
        }
      })
    }

  enrollUserToTrainingClass(classId: number){
    this.clientService.enrollUserToTrainingClass(classId).subscribe({
      next:(response) =>{
        this.fetchUserTrainingClassesData();
        this.isEnrolled = true;
        UtilComponentComponent.openSnackBar("You have enrolled from training Class", this._responseBar, UtilComponentComponent.SnackbarStates.Default);
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
        this.isEnrolled = false;
        this.fetchTrainingClassData(classId);
        this.fetchUserTrainingClassesData();
        this.isEnrolled = true;
        UtilComponentComponent.openSnackBar("You have drop out from training Class", this._responseBar, UtilComponentComponent.SnackbarStates.Default);
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

  goToCheckoutPage(subscriptionId: number) {
    this.router.navigate(['/client/checkout'], { queryParams: { subscId: subscriptionId } });
  }

}
