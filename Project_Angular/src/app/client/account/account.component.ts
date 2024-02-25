import { Component, ViewChild } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ClientService } from '../../services/client.service';
import { response } from 'express';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  
  reservations = [];
  displayedColumns: string[] = ['Date', 'HourSchedule', 'Court', 'Delete'];
  deleteMessage: string;
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private registrationService: RegistrationService, private router: Router, private clientService: ClientService ) {
    
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  ngAfterViewInit(){
    
  }

  ngOnInit():void{
    this.fetchReservationsForClient();
  }

  public fetchReservationsForClient(){
    this.clientService.getAllReservationsForClient().subscribe({
      next: (response) => {
        this.reservations = response;
        this.dataSource = new MatTableDataSource<any>(this.reservations);
        this.dataSource.paginator = this.paginator;
        }
      });
  }

  public deleteReservation(id: number){
      this.clientService.deleteReservation(id).subscribe({
        next:(response:any) =>{
          this.deleteMessage = "Reservation was deleted";
          this.fetchReservationsForClient();
          setTimeout(() => {
              this.deleteMessage = '';
          }, 2000)

        }
      })
  }
  
  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }
  
}
