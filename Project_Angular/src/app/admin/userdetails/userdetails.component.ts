import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { UserDataResponse} from '../../models/user-response.model';
import { MatSelect } from '@angular/material/select';
import { Role } from '../../models/role.model';
import { MatDialog } from '@angular/material/dialog';
import { PopupEditUserDataComponent } from '../../popup-edit-user-data/popup-edit-user-data.component';


@Component({
  selector: 'app-userdetails',
  templateUrl: './userdetails.component.html',
  styleUrl: './userdetails.component.css'
})
export class UserdetailsComponent implements OnInit{

  users: UserDataResponse[] = [];
  displayedColumns: string[] = ['First Name', 'Last Name', 'Email', 'Role','Edit Role','Delete'];
  dataSource: MatTableDataSource<any>;
  selectedUserId: number;
  selectedUser: number;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild('roleSelect') roleSelect: MatSelect;
  roles: Role[] = [
    {id: 1 , name: "USER"},
    {id: 2, name: "ADMIN"},
    {id: 3, name: "TRAINER"}
  ]

  constructor(private adminService: AdminService, private dialog: MatDialog) {
    this.fetchUsersData();
  }
  ngOnInit(): void {}

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  closeCard() {
    this.selectedUserId = null;
    this.selectedUser = null;
}

  fetchUsersData(){
    this.adminService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response;
        this.dataSource = new MatTableDataSource<any>(this.users);
        this.dataSource.paginator = this.paginator;
      }
    })
  }

  OpenEditUserRolePopUp(id: number){
    var _editRolePopUp = this.dialog.open(PopupEditUserDataComponent, {
      width: '50%',
      data: {
        id: id
      }
    });

    _editRolePopUp.afterClosed().subscribe(response =>{
      this.fetchUsersData();

    })
  }


  public deleteUserData(id: number){
    this.adminService.deleteUser(id).subscribe({
      next: (response) =>{
        alert('User was deleted');
        this.fetchUsersData();

      }
    })
  }

}