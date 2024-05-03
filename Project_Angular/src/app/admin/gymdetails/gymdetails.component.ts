import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { PopupSubscriptionComponent } from '../../popup-subscription/popup-subscription.component';
import { MatDialog } from '@angular/material/dialog';
import { PopupUpdateSubscriptionComponent } from '../../popup-update-subscription/popup-update-subscription.component';
import { PopupCreateTrClassComponent } from '../../popup-create-tr-class/popup-create-tr-class.component';
import { PopupEditTrClassComponent } from '../../popup-edit-tr-class/popup-edit-tr-class.component';
import { TrainingClassResponse } from '../../models/trainingclass-response.model';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SubscriptionResponse } from '../../models/subscription-response.model';


@Component({
  selector: 'app-gymdetails',
  templateUrl: './gymdetails.component.html',
  styleUrl: './gymdetails.component.css'
})
export class GymdetailsComponent {

  subscriptions: SubscriptionResponse[];
  selectedTrainingClassId: number;
  selectedTrainingClass: TrainingClassResponse;
  trainingClassesData: TrainingClassResponse[];
  trainingClassId: number;
  constructor(private adminService: AdminService, private dialog: MatDialog, private _responseBar: MatSnackBar) { }

  ngOnInit(): void {
    this.fetchSubscriptions();
    this.fetchAllTrainingClassData();
  }

  selectionChangeHandler(event: any) {
    this.selectedTrainingClassId = event.value;
    this.fetchTrainingClassData(this.selectedTrainingClassId);
  }


  public fetchSubscriptions() {
    return this.adminService.getAllSubscriptions().subscribe({
      next: (response) => {
        this.subscriptions = response;
      }
    })
  }

  public deleteSubscription(id: number) {
    return this.adminService.deleteSubscription(id).subscribe({
      next: () => {
        UtilComponentComponent.openSnackBar("Subscription was deleted", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
        this.fetchSubscriptions();
      },
      error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  public fetchTrainingClassData(id: number) {
    this.adminService.getTrainingClassData(id).subscribe(
      response => {
        this.selectedTrainingClass = response;
      }
    )
  }

  public fetchAllTrainingClassData() {
    this.adminService.getTrainingClassesData().subscribe({
      next: (response) => {
        this.trainingClassesData = response;
      }
    })
  }

  public deleteTrainingClass(id: number) {
    this.adminService.deleteTrainingClass(id).subscribe({
      next: () => {
        this.fetchAllTrainingClassData();
        this.selectedTrainingClassId = null;
        this.selectedTrainingClass = null;
        UtilComponentComponent.openSnackBar("Training class was deleted", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
      }, error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  OpenCreatePopUp() {
    var _popUp = this.dialog.open(PopupSubscriptionComponent, {
      width: '50%',
      enterAnimationDuration: '700ms',
      exitAnimationDuration: '700ms'
    });

    _popUp.afterClosed().subscribe(response => {
      this.fetchSubscriptions();
    })
  }

  OpenCreateTrainingClassPopUp() {
    var _popUpTrainingClass = this.dialog.open(PopupCreateTrClassComponent, {
      width: '50%',
      enterAnimationDuration: '400ms',
      exitAnimationDuration: '400ms'
    });

    _popUpTrainingClass.afterClosed().subscribe(response => {
      this.fetchAllTrainingClassData();
    })
  }

  OpenEditTrainingClassPopUp(id: number) {
    var _popUpEditTrainingClass = this.dialog.open(PopupEditTrClassComponent, {
      width: '50%',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '500ms',
      data: {
        id: id
      }
    });

    _popUpEditTrainingClass.afterClosed().subscribe(response => {
      this.fetchTrainingClassData(id);
      this.fetchAllTrainingClassData();
    })
  }

  OpenEditPopUp(id: number) {
    var _popUp = this.dialog.open(PopupUpdateSubscriptionComponent, {
      width: '50%',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '500ms',
      data: {
        id: id
      }
    });

    _popUp.afterClosed().subscribe({
      next: () => {
        this.fetchSubscriptions();
      }
    })
  }

}
