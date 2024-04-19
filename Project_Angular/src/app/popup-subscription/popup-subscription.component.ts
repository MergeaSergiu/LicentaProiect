import { Component, Inject } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SubscriptionRequest } from '../models/subscription-request.model';
import { AdminService } from '../services/admin.service';
import { response } from 'express';

@Component({
  selector: 'app-popup-subscription',
  templateUrl: './popup-subscription.component.html',
  styleUrl: './popup-subscription.component.css'
})
export class PopupSubscriptionComponent {

  SubscriptionResponse:any;
  constructor(private matDialog: MatDialogRef<PopupSubscriptionComponent>, private adminService: AdminService){}

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
  next: (response) =>{
    this.closePopUp();
}, error: (any) =>{
  alert("Can not create a subscription");
    }
  });
 }
}
