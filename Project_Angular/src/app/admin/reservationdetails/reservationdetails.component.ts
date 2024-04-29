import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from '../../services/admin.service';
import { ClientService } from '../../services/client.service';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserDataResponse } from '../../models/user-response.model';
import { NgForm } from '@angular/forms';
import { ReservationRequestByAdmin } from '../../models/reservationByAdmin-request.model';
import { CourtDetailsResponse } from '../../models/court-details-response.model';

interface HourSchedule {
  time: string;
  reserved: boolean;
}

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
  courtDetails: CourtDetailsResponse[];
  selectedCourt: string;
  displayedColumns: string[] = ['Date', 'HourSchedule', 'Email', 'Court', 'Delete'];
  hourSchedules: HourSchedule[] = [];
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private adminService: AdminService, private clientService: ClientService, private _responseBar: MatSnackBar) { }
  courts: string[] = ['TENNIS', 'FOOTBALL', 'BASKETBALL'];
  
  ngOnInit(): void {
    this.fetchAllReservations();
    this.fetchAllUsers();
    this.getTimeSlotForCourts();
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
    currentDate.setHours(0, 0, 0, 0);
    return currentDate;
  }

  onSubmitAddReservationForUser(form: NgForm) {
    const year = form.value.selectedDate.getFullYear();
    const month = (form.value.selectedDate.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-indexed
    const day = form.value.selectedDate.getDate().toString().padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    const reservationRequest: ReservationRequestByAdmin = {
      userId: form.value.userId,
      localDate: formattedDate,
      court: form.value.court,
      hourSchedule: form.value.hourSchedule
    };
    this.adminService.addReservationForUser(reservationRequest).subscribe({
      next: () => {
        this.fetchAllReservations();
        UtilComponentComponent.openSnackBar("Your reservation was created", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
      }, error: (error) => {
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  deleteUserData(form:NgForm){
      form.resetForm();
  }


  getTimeSlotForCourts(){
    this.adminService.getCourtsDetails().subscribe({
      next: (response) => {
          this.courtDetails = response;
      },error:(error) =>{
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }

  saveCourtDetails(courtId: number, startTime: number, endTime: number){
    this.adminService.updateCourtDetails(courtId, startTime, endTime).subscribe({
      next: () => {
        UtilComponentComponent.openSnackBar("TimeSlots were updated", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
        this.getTimeSlotForCourts();
      }, error: (error) =>{
        UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
      }
    })
  }
}
