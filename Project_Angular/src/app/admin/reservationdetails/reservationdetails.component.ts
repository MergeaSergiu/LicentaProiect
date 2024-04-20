import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from '../../services/admin.service';
import { ClientService } from '../../services/client.service';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserDataResponse } from '../../models/user-response.model';
import { NgForm } from '@angular/forms';
import { ReservationRequestByAdmin } from '../../models/reservationByAdmin-request.model';
import { error } from 'console';
import { CourtDetailsResponse } from '../../models/court-details-response.model';

@Component({
  selector: 'app-reservationdetails',
  templateUrl: './reservationdetails.component.html',
  styleUrl: './reservationdetails.component.css'
})
export class ReservationdetailsComponent implements OnInit {

  reservations = [];
  users: UserDataResponse[];
  selectedUserId: number; 
  selectedHourSchedule: string;
  FootballDetails: CourtDetailsResponse;
  BasketballDetails: CourtDetailsResponse;
  TennisDetails: CourtDetailsResponse;
  selectedCourt: string;
  displayedColumns: string[] = ['Date', 'HourSchedule', 'Email', 'Court', 'Delete'];
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private adminService: AdminService, private clientService: ClientService, private _responseBar: MatSnackBar) { }

  hourSchedules: string[] = ['16-17', '17-18', '18-19', '19-20', '20-21', '21-22', '22-23'];
  courts: string[] = ['TENNIS', 'FOOTBALL', 'BASKETBALL'];
  
  ngOnInit(): void {
    this.fetchAllReservations();
    this.fetchAllUsers();
    this.getTimeSlotForFootball(this.courts[1]);
  }

  Filterchange(data: Event) {
    const value = (data.target as HTMLInputElement).value;
    this.dataSource.filter = value;
  }

  public fetchAllUsers() {
    return this.adminService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response;
      }
    })
  }

  public deleteReservation(id: number) {
    this.clientService.deleteReservation(id).subscribe({
      next: (response: any) => {
        this.fetchAllReservations();
        UtilComponentComponent.openSnackBar("The reservation was deleted", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  public fetchAllReservations() {
    this.adminService.getAllReservations().subscribe({
      next: (response) => {
        this.reservations = response;
        this.dataSource = new MatTableDataSource<any>(this.reservations);
        this.dataSource.paginator = this.paginator;
      }
    });
  }

  getReservationDate(reservationDate: string) {
    const [year, month, day] = reservationDate.split('-').map(Number);
    return new Date(year, month - 1, day);
  }

  getCurrentDate(): Date {
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0); // Set hours, minutes, seconds, and milliseconds to zero
    return currentDate;
  }

  onSubmitAddReservationForUser(form: NgForm) {
    const year = form.value.selectedDate.getFullYear();
    const month = (form.value.selectedDate.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-indexed
    const day = form.value.selectedDate.getDate().toString().padStart(2, '0');
    // Format the date as 'yyyy-mm-dd'
    const formattedDate = `${year}-${month}-${day}`;
    const reservationRequest: ReservationRequestByAdmin = {
      userId: form.value.userId,
      localDate: formattedDate,
      court: form.value.court,
      hourSchedule: form.value.hourSchedule
    };
    this.adminService.addReservationForUser(reservationRequest).subscribe({
      next: (response) => {
        this.fetchAllReservations();
        UtilComponentComponent.openSnackBar("Your reservation was created", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
      }, error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  deleteUserData(form:NgForm){
      form.resetForm(); // Reset the form
  }


  getTimeSlotForFootball(court: string){
    this.adminService.getTimeSlots(court).subscribe({
      next: (response) => {
        console.log(response);
      },error:(error) =>{
        console.log(error);
      }
    })
  }

  getTimeSlotForBasketball(court: string){
    this.adminService.getTimeSlots(court).subscribe({
      next: (response) => {
          
      }
    })
  }

  getTimeSlotForTennis(court: string){
    this.adminService.getTimeSlots(court).subscribe({
      next: (response) => {
          
      }
    })
  }



}
