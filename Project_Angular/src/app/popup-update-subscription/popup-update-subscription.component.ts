import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../services/admin.service';
import { SubscriptionRequest } from '../models/subscription-request.model';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../util-component/util-component.component';

@Component({
  selector: 'app-popup-update-subscription',
  templateUrl: './popup-update-subscription.component.html',
  styleUrl: './popup-update-subscription.component.css'
})
export class PopupUpdateSubscriptionComponent {

  @ViewChild('authForm') authForm: NgForm;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private matDialog: MatDialogRef<PopupUpdateSubscriptionComponent>, private adminService: AdminService, private _responseBar: MatSnackBar){}
  editData: any;
  inputData: any;
  
  closePopUp(){
    this.matDialog.close();
  }

  ngOnInit(): void{
    this.inputData = this.data;
    this.setPopUpData(this.inputData.id);
  }

  setPopUpData(id: number){
    this.adminService.getSubscriptionById(id).subscribe({
      next: (response) => {
        this.editData= response;
        if (this.authForm) {
        this.authForm.form.patchValue({
          subscriptionName: this.editData.subscriptionName,
          subscriptionPrice: this.editData.subscriptionPrice,
          subscriptionDescription: this.editData.subscriptionDescription
        });
      }
      }
  })
  }

  onSubmitUpdateSubscription(form: NgForm){
    const subscriptionData: SubscriptionRequest = {
      subscriptionName: form.value.subscriptionName,
      subscriptionPrice: form.value.subscriptionPrice,
      subscriptionDescription: form.value.subscriptionDescription
  };
 this.adminService.updateSubscriptionData(this.inputData.id, subscriptionData).subscribe({
  next: () =>{
   this.closePopUp();
 }, error: () =>{
  UtilComponentComponent.openSnackBar("Can not update the data", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
    }
  });
 }

}
