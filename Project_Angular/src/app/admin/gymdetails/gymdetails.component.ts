import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { PopupSubscriptionComponent } from '../../popup-subscription/popup-subscription.component';
import { MatDialog } from '@angular/material/dialog';
import { response } from 'express';
import { PopupUpdateSubscriptionComponent } from '../../popup-update-subscription/popup-update-subscription.component';

@Component({
  selector: 'app-gymdetails',
  templateUrl: './gymdetails.component.html',
  styleUrl: './gymdetails.component.css'
})
export class GymdetailsComponent {

  subscriptions = [];
  constructor(private router: Router, private adminService: AdminService, private dialog: MatDialog) {
}

  ngOnInit(): void {
    this.fetchSubscriptions();
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

  OpenEditPopUp(id:number) {
    var _popUp = this.dialog.open(PopupUpdateSubscriptionComponent, {
      width: '50%',
      enterAnimationDuration: '700ms',
      exitAnimationDuration: '700ms',
      data: {
          id: id
      }
    });

    _popUp.afterClosed().subscribe(response =>{
      this.fetchSubscriptions();
    })
  }

}
