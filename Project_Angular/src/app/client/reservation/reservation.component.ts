import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../../services/client.service';
import { ReservationResponse } from '../../models/reservation-response.model';
import { ReservationRequest } from '../../models/reservation-request.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar} from '@angular/material/snack-bar';
import { UtilComponentComponent } from '../../util-component/util-component.component';
import { error } from 'console';
import { AdminService } from '../../services/admin.service';


interface HourSchedule {
  time: string;
  reserved: boolean;
}

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
})
export class ReservationComponent implements OnInit{

  reservationsTenis: ReservationResponse[] = [];
  reservationsBasketball: ReservationResponse[] = [];
  reservationsFotball: ReservationResponse[] = [];
  selectedDate: Date | null;
  hourSchedulesFootball: HourSchedule[] = [];
  hourSchedulesBasketball: HourSchedule[] = [];
  hourSchedulesTenis: HourSchedule[] = [];

  minDate: Date;
  maxDate: Date;
  successfullMessage: string;

  reservations = [];
  displayedColumns: string[] = ['Date', 'HourSchedule', 'Court', 'Delete'];
  deleteMessage: string;
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor( private clientService: ClientService,  private _responseBar: MatSnackBar, private adminService: AdminService ) {
    const today = new Date();
    this.minDate = new Date(today.getFullYear(), today.getMonth(), 0); // Start of current month
    this.maxDate = new Date(today.getFullYear(), today.getMonth() + 2, 0); // End of next month
    this.fetchReservationsForClient();
  }

  Filterchange(data:Event){
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter=value;
  }

  async ngOnInit(): Promise<void>{
    await this.fetchCourtDetails('FOOTBALL');
    await this.fetchCourtDetails('BASKETBALL');
    await this.fetchCourtDetails('TENNIS');
    await this.fetchReservationsForClient();
    await this.fetchFootballReservations();
    await this.fetchFootballBasketball();
    await this.fetchFootballTenis();

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

  public fetchCourtDetails(court: string){
    this.adminService.getTimeSlots(court).subscribe({
      next: (response) => {
        console.log(response);
          const startTime = response.startTime;
          const endTime = response.endTime;

          const hourSlots: HourSchedule[] = [];
          for (let i = startTime; i < endTime; i++) {
            const timeSlot = `${i}-${i + 1}`; // Example: '16-17'
            const hourSlot: HourSchedule = { time: timeSlot, reserved: false };
            hourSlots.push(hourSlot);
          }

          if (court === 'FOOTBALL') {
            this.hourSchedulesFootball = hourSlots;
          } else if (court === 'TENNIS') {
            this.hourSchedulesBasketball = hourSlots;
          } else if (court === 'BASKETBALL') {
            this.hourSchedulesTenis = hourSlots;
          }
      }
    })
  }

  public deleteReservation(id: number){
      this.clientService.deleteReservation(id).subscribe({
        next:(response:any) =>{
          this.fetchReservationsForClient();
          UtilComponentComponent.openSnackBar("Your reservation was deleted", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
        },error:(error) =>{
          UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        }
      })
  }

    fetchFootballReservations():void{
      this.clientService.getReservations('FOOTBALL').subscribe(
        (response: ReservationResponse[]) => {
          this.reservationsFotball = response;
        },
        error => {
          console.log('Error fetching reservations:', error);
        }
      )
    }

    fetchFootballBasketball():void{
      this.clientService.getReservations('BASKETBALL').subscribe(
        (response: ReservationResponse[]) => {
          this.reservationsBasketball = response;
        },
        error => {
          console.log('Error fetching reservations:', error);
        }
      )
    }

    fetchFootballTenis():void{
      this.clientService.getReservations('TENNIS').subscribe(
        (response: ReservationResponse[]) => {
          this.reservationsTenis = response;
        },
        error => {
          console.log('Error fetching reservations:', error);
        }
      )
    }

    onDateSelected(selectedDate: Date): void {
      this.updateHourSchedules(selectedDate);
    }

    updateHourSchedules(selectedDate: Date): void {
      const year = selectedDate.getFullYear();
      const month = (selectedDate.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-indexed
      const day = selectedDate.getDate().toString().padStart(2, '0');

      // Format the date as 'yyyy-mm-dd'
      const formattedDate = `${year}-${month}-${day}`;
      //console.log(formattedDate);
      this.hourSchedulesFootball.forEach(schedule => {
        const isInReservation = this.reservationsFotball.some(reservation =>
          reservation.hourSchedule === schedule.time && reservation.reservationDate === formattedDate
        );
        schedule.reserved = isInReservation;
      });

      this.hourSchedulesBasketball.forEach(schedule => {
        const isInReservation = this.reservationsBasketball.some(reservation =>
          reservation.hourSchedule === schedule.time && reservation.reservationDate === formattedDate
        );
        schedule.reserved = isInReservation;
      });

      this.hourSchedulesTenis.forEach(schedule => {
        const isInReservation = this.reservationsTenis.some(reservation =>
          reservation.hourSchedule === schedule.time && reservation.reservationDate === formattedDate
        );
        schedule.reserved = isInReservation;
      });
    }

    saveReservation(date: Date, schedule: HourSchedule, court: string){
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-indexed
      const day = date.getDate().toString().padStart(2, '0');
      // Format the date as 'yyyy-mm-dd'
      const formattedDate = `${year}-${month}-${day}`;

      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      const currentMonth = currentDate.getMonth() + 1; // Adding 1 because getMonth() returns zero-based month (0-11)
      const currentDay = currentDate.getDate();
      const formattedCurrentDate = `${currentYear}-${currentMonth}-${currentDay}`;

      const date1 = new Date(formattedDate);
      const date2 = new Date(formattedCurrentDate);

      if (date1 < date2) {
        UtilComponentComponent.openSnackBar("Reservation can not be in the past", this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        return;
      }

      const registrationRequest: ReservationRequest = {
            localDate: formattedDate,
            hourSchedule: schedule.time,
            court: court
    };

      this.clientService.createReservation(registrationRequest).subscribe({
        next: (response: any) =>{
          UtilComponentComponent.openSnackBar("Your reservation was created", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
          schedule.reserved = true;
        },
        error: (error) =>{
          UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
        }
      });

      this.fetchFootballReservations();
      this.fetchFootballBasketball();
      this.fetchFootballTenis();
      
    }

}
