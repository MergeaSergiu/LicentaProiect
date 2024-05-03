import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { UserSubscriptionsDataResponse } from '../../models/userSubscriptionData-response.model';
import { MatDialog } from '@angular/material/dialog';
import { PopupAddSubForUserComponent } from '../../popup-add-sub-for-user/popup-add-sub-for-user.component';

@Component({
  selector: 'app-user-subscriptions',
  templateUrl: './user-subscriptions.component.html',
  styleUrl: './user-subscriptions.component.css'
})
export class UserSubscriptionsComponent implements OnInit{

  userId: number;
  userSubscriptionsData : UserSubscriptionsDataResponse[];

  constructor(private route: ActivatedRoute, private adminService: AdminService, private dialog: MatDialog){}
   
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'];
    })
    this.fetchUserSubscriptions(this.userId);
  }

  fetchUserSubscriptions(userId: number){
    this.adminService.getUserSubscriptionsData(userId).subscribe({
      next: (response) =>{
        this.userSubscriptionsData = response;
      }
    })
  }

  isSubscriptionActive(endDate: Date): boolean{
    const today = new Date();
    const endDateOnly = new Date(endDate);
    endDateOnly.setHours(0,0,0,0);
    today.setHours(0,0,0,0);
    return endDateOnly >= today;
  }

  OpenAddUserSubscriptionPopUp(){
      var _popUpAddSubsForUser = this.dialog.open(PopupAddSubForUserComponent, {
      width: '50%',
      enterAnimationDuration: '500ms',
      exitAnimationDuration: '500ms',
      data: {
          id: this.userId
      }
      });
      _popUpAddSubsForUser.afterClosed().subscribe(response => {
        this.fetchUserSubscriptions(this.userId);
      })
  }

}
