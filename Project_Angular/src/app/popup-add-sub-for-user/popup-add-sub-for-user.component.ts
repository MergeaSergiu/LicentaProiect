import { Component, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../services/admin.service';
import { NgForm } from '@angular/forms';
import { UserSubscriptionRequest } from '../models/userSubscription-request.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../util-component/util-component.component';
@Component({
  selector: 'app-popup-add-sub-for-user',
  templateUrl: './popup-add-sub-for-user.component.html',
  styleUrl: './popup-add-sub-for-user.component.css'
})
export class PopupAddSubForUserComponent {

  selectedSubscriptionId: number;
  userId: number;
  subscriptions: any[];
  inputData: any;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private _responseBar: MatSnackBar, private matDialog: MatDialogRef<PopupAddSubForUserComponent>, private adminService: AdminService){}

  closePopUp(){
    this.matDialog.close();
  }

  ngOnInit(): void{
    this.fetchSubscriptions();
    this.inputData = this.data;
  }

  public fetchSubscriptions(){
    return this.adminService.getAllSubscriptions().subscribe(
      response => {
        this.subscriptions = response;
      }
    )
  }
  onSubmitAddSubscription(form: NgForm){
    const userSubscription: UserSubscriptionRequest = {
      subscriptionId: form.value.subscriptionId,
      userId: this.inputData.id
    };

    this.adminService.AddUserSubscription(userSubscription).subscribe({
      next: () => {
        this.closePopUp();
      }, error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    });
  }
}


