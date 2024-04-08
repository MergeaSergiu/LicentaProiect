import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from '../../services/admin.service';
import { ClientService } from '../../services/client.service';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-reservationdetails',
  templateUrl: './reservationdetails.component.html',
  styleUrl: './reservationdetails.component.css'
})
export class ReservationdetailsComponent implements OnInit{

  reservations = [];
  displayedColumns: string[] = ['Date', 'HourSchedule', 'Email', 'Court', 'Delete'];
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private adminService: AdminService, private clientService: ClientService,  private _responseBar: MatSnackBar ) {}

  ngOnInit():void{
    this.fetchAllReservations();
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  public deleteReservation(id: number){
    this.clientService.deleteReservation(id).subscribe({
      next:(response:any) =>{
        this.fetchAllReservations();
        UtilComponentComponent.openSnackBar("The reservation was deleted", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
}

public fetchAllReservations(){
this.adminService.getAllReservations().subscribe({
  next: (response) => {
    this.reservations = response;
    this.dataSource = new MatTableDataSource<any>(this.reservations);
    this.dataSource.paginator = this.paginator;
    }
  });
}

getReservationDate(reservationDate: string){
  const [year, month, day] = reservationDate.split('-').map(Number);
  return new Date(year, month - 1, day);
}

getCurrentDate(): Date {
  const currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0); // Set hours, minutes, seconds, and milliseconds to zero
  return currentDate;
}

 
  
}
