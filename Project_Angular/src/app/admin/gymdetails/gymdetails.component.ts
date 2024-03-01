import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { PopupSubscriptionComponent } from '../../popup-subscription/popup-subscription.component';
import { MatDialog } from '@angular/material/dialog';
import { response } from 'express';
import { PopupUpdateSubscriptionComponent } from '../../popup-update-subscription/popup-update-subscription.component';
import { PopupCreateTrClassComponent } from '../../popup-create-tr-class/popup-create-tr-class.component';
import { PopupEditTrClassComponent } from '../../popup-edit-tr-class/popup-edit-tr-class.component';

interface TrainingClass {
  id: number;
  className: string;
  duration: number;
  intensity: string;
  localDate: string;
  trainerId: number;
  // Add more properties as needed
}

@Component({
  selector: 'app-gymdetails',
  templateUrl: './gymdetails.component.html',
  styleUrl: './gymdetails.component.css'
})
export class GymdetailsComponent {

  subscriptions = [];
  selectedTrainingClassId: number;
  selectedTrainingClass: TrainingClass;
  trainingClassesData: TrainingClass[] = [];
  trainingClassId: number;
  constructor(private router: Router, private adminService: AdminService, private dialog: MatDialog) {
}

  ngOnInit(): void {
    this.fetchSubscriptions();
    this.fetchAllTrainingClassData();
  }

  selectionChangeHandler(event: any) {
    this.selectedTrainingClassId = event.value;
    this.fetchTrainingClassData(this.selectedTrainingClassId);
  }

  closeCard() {
    this.selectedTrainingClassId = null;
    this.selectedTrainingClass = null;
}

  public fetchSubscriptions() {
    return this.adminService.getAllSubscriptions().subscribe({
      next: (response) => {
        this.subscriptions = response;
        console.log(this.subscriptions);
      }
    })
  }

  public deleteSubscription(id: number) {
    return this.adminService.deleteSubscription(id).subscribe({
      next: (response) => {
        alert("Subscription was deleted");
        this.fetchSubscriptions();
      },
      error: (error) => {
        alert(error);
      }
    })
  }

  public fetchTrainingClassData(id: number){
    this.adminService.getTrainingClassData(id).subscribe(
      response => {
        this.selectedTrainingClass = response;
      }
    )
  }

  public fetchAllTrainingClassData(){
    this.adminService.getTrainingClassesData().subscribe(
      response => {
        console.log(response);
        this.trainingClassesData = response;
      }
    )
  }

  public deleteTrainingClass(id: number){
    this.adminService.deleteTrainingClass(id).subscribe(
      response => {
        this.fetchAllTrainingClassData();
        alert("TrainingClass was deleted");
        this.selectedTrainingClassId = null;
        this.selectedTrainingClass = null;
      }
    )
  }

  OpenCreatePopUp() {
    var _popUp = this.dialog.open(PopupSubscriptionComponent, {
      width: '50%',
      enterAnimationDuration: '700ms',
      exitAnimationDuration: '700ms'
    });

    _popUp.afterClosed().subscribe(response =>{
      this.fetchSubscriptions();
    })
  }

  OpenCreateTrainingClassPopUp(){
      var _popUpTrainingClass = this.dialog.open(PopupCreateTrClassComponent, {
        width: '50%',
        enterAnimationDuration: '400ms',
        exitAnimationDuration: '400ms'
      });

      _popUpTrainingClass.afterClosed().subscribe(response => {
        this.fetchAllTrainingClassData();
      })
  }

  OpenEditTrainingClassPopUp(id: number){
    var _popUpEditTrainingClass = this.dialog.open(PopupEditTrClassComponent, {
      width: '50%',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '500ms',
      data: {
          id: id
      }
    });

    _popUpEditTrainingClass.afterClosed().subscribe(response =>{
      this.fetchTrainingClassData(id);
      this.fetchAllTrainingClassData();
      this.closeCard();
    })
  }

  OpenEditPopUp(id:number) {
    var _popUp = this.dialog.open(PopupUpdateSubscriptionComponent, {
      width: '50%',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '500ms',
      data: {
          id: id
      }
    });

    _popUp.afterClosed().subscribe(response =>{
      this.fetchSubscriptions();
    })
  }

}
