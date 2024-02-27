import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../services/admin.service';
import { SubscriptionRequest } from '../models/subscription-request.model';
import { FormBuilder, NgForm } from '@angular/forms';

@Component({
  selector: 'app-popup-update-subscription',
  templateUrl: './popup-update-subscription.component.html',
  styleUrl: './popup-update-subscription.component.css'
})
export class PopupUpdateSubscriptionComponent {

  @ViewChild('authForm') authForm: NgForm;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private matDialog: MatDialogRef<PopupUpdateSubscriptionComponent>, private adminService: AdminService){}
  editData: any;
  inputData: any;
  
  closePopUp(){
    this.matDialog.close();
  }

  ngOnInit(): void{
    this.inputData = this.data;
    console.log(this.data);
    this.setPopUpData(this.inputData.id);
  }

  setPopUpData(id: number){
    this.adminService.getSubscriptionById(id).subscribe(
      response => {
        
        this.editData= response;
        console.log(this.editData);
      if (this.authForm) {
        this.authForm.form.patchValue({
          subscriptionName: this.editData.subscriptionName,
          subscriptionPrice: this.editData.subscriptionPrice,
          subscriptionTime: this.editData.subscriptionTime,
          subscriptionDescription: this.editData.subscriptionDescription
        });
      }
      }
    )
  }

  onSubmitUpdateSubscription(form: NgForm){
    const subscriptionData: SubscriptionRequest = {
      subscriptionName: form.value.subscriptionName,
      subscriptionPrice: form.value.subscriptionPrice,
      subscriptionTime: form.value.subscriptionTime,
      subscriptionDescription: form.value.subscriptionDescription
  };
 this.adminService.updateSubscriptionData(this.inputData.id, subscriptionData).subscribe({
  next: (response) =>{
   this.closePopUp();
 }, error: (any) =>{
  alert("Can not create a reservation");
    }
  });
 }

}
