import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { UserDataResponse} from '../../models/user-response.model';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'app-userdetails',
  templateUrl: './userdetails.component.html',
  styleUrl: './userdetails.component.css'
})
export class UserdetailsComponent implements OnInit{

  users: UserDataResponse[] = [];
  displayedColumns: string[] = ['First Name', 'Last Name', 'Email', 'Role' ,'Delete'];
  dataSource: MatTableDataSource<any>;
  selectedClientRole: string;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild('roleSelect') roleSelect: MatSelect;
  roles: string[] = ["CLIENT", "ADMIN", "TRAINER"];

  constructor(private adminService: AdminService) {
    this.fetchUsersData();
  }
  ngOnInit(): void {}

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
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

  public deleteUserData(id: number){
    this.adminService.deleteUser(id).subscribe({
      next: (response) =>{
        alert('User was deleted');
        this.fetchUsersData();

      }
    })
  }

}
