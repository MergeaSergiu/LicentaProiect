import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-reservationdetails',
  templateUrl: './reservationdetails.component.html',
  styleUrl: './reservationdetails.component.css'
})
export class ReservationdetailsComponent implements AfterViewInit{

  reservations = [];
  displayedColumns: string[] = ['Date', 'HourSchedule', 'Email', 'Court'];
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private registrationService: RegistrationService, private router: Router, private adminService: AdminService ) {
    this.adminService.getAllReservations().subscribe({
      next: (response) => {
        this.reservations = response;
        this.dataSource = new MatTableDataSource<any>(this.reservations);
        this.dataSource.paginator = this.paginator;
        }
      });
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  ngAfterViewInit(){}

  ngOnInit():void{}
  
  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }

  
}
