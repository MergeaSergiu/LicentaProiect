import { Component, Inject } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SubscriptionRequest } from '../models/subscription-request.model';
import { AdminService } from '../services/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../util-component/util-component.component';

@Component({
  selector: 'app-popup-subscription',
  templateUrl: './popup-subscription.component.html',
  styleUrl: './popup-subscription.component.css'
})
export class PopupSubscriptionComponent {

  SubscriptionResponse:any;
  constructor(private matDialog: MatDialogRef<PopupSubscriptionComponent>, private adminService: AdminService, private _responseBar: MatSnackBar){}

  closePopUp(){
    this.matDialog.close();
  }

  onSubmitCreateSubscription(form: NgForm){
    const subscriptionData: SubscriptionRequest = {
      subscriptionName: form.value.subscriptionName,
      subscriptionPrice: form.value.subscriptionPrice,
      subscriptionDescription: form.value.subscriptionDescription
  };
 this.adminService.createSubscription(subscriptionData).subscribe({
  next: () =>{
    this.closePopUp();
}, error: (any) =>{
    UtilComponentComponent.openSnackBar("Can not create the subscription", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
    }
  });
 }
}
