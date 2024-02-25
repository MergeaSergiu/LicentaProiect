import { Component } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { ClientService } from '../../services/client.service';
import { Router } from '@angular/router';
import { ReservationResponse } from './reservation-response.model';
import { DateAdapter } from '@angular/material/core';
import { ReservationRequest } from '../../models/reservation-request.model';
import { DatePipe } from '@angular/common';


interface HourSchedule {
  time: string;
  reserved: boolean;
}

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
})
export class ReservationComponent {


  reservationsTenis: ReservationResponse[] = [];
  reservationsBasketball: ReservationResponse[] = [];
  reservationsFotball: ReservationResponse[] = [];
  private datePipe: DatePipe;

  selectedDate: Date | null;
  hourSchedulesFootball: HourSchedule[] = [
    {time:'16-17', reserved: false},
    {time:'17-18', reserved: false},
    {time:'18-19', reserved: false},
    {time:'19-20', reserved: false},
    {time:'20-21', reserved: false},
    {time:'21-22', reserved: false},
    {time:'22-23', reserved: false}
  ];

  hourSchedulesBasketball: HourSchedule[] = [
    {time:'16-17', reserved: false},
    {time:'17-18', reserved: false},
    {time:'18-19', reserved: false},
    {time:'19-20', reserved: false},
    {time:'20-21', reserved: false}
  ];

  hourSchedulesTenis: HourSchedule[] = [
    {time:'16-17', reserved: false},
    {time:'17-18', reserved: false},
    {time:'18-19', reserved: false},
    {time:'19-20', reserved: false},
    {time:'20-21', reserved: false},
    {time:'21-22', reserved: false},
  ];

  minDate: Date;
  maxDate: Date;
  successfullMessage: string;

  constructor(private registrationService: RegistrationService, private clientService: ClientService, private router: Router,private dateAdapter: DateAdapter<Date> ) {
    const today = new Date();
    this.minDate = new Date(today.getFullYear(), today.getMonth(), 0); // Start of current month
    this.maxDate = new Date(today.getFullYear(), today.getMonth() + 2, 0); // End of next month
  }

  ngOnInit(): void{
      
      
    }

    fetchFootballReservations():void{
      this.clientService.getReservations('Fotball').subscribe(
        (response: ReservationResponse[]) => {
          this.reservationsFotball = response;
        },
        error => {
          console.log('Error fetching reservations:', error);
        }
      )
    }

    fetchFootballBasketball():void{
      this.clientService.getReservations('Basketball').subscribe(
        (response: ReservationResponse[]) => {
          this.reservationsBasketball = response;
        },
        error => {
          console.log('Error fetching reservations:', error);
        }
      )
    }

    fetchFootballTenis():void{
      this.clientService.getReservations('Tenis').subscribe(
        (response: ReservationResponse[]) => {
          this.reservationsTenis = response;
        },
        error => {
          console.log('Error fetching reservations:', error);
        }
      )
    }

    onDateSelected(selectedDate: Date): void {
      //console.log(selectedDate);
      this.fetchFootballReservations();
      this.fetchFootballBasketball();
      this.fetchFootballTenis();
      //selectedDate =  new Date(this.moment(selectedDate).utcOffset('+0000').format('YYYY-MM-DD HH:MM'))
      // Update the hour schedules here based on the selected date
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
          reservation.hourSchedule === schedule.time && reservation.localDate === formattedDate
        );
        schedule.reserved = isInReservation;
      });

      this.hourSchedulesBasketball.forEach(schedule => {
        const isInReservation = this.reservationsBasketball.some(reservation =>
          reservation.hourSchedule === schedule.time && reservation.localDate === formattedDate
        );
        schedule.reserved = isInReservation;
      });

      this.hourSchedulesTenis.forEach(schedule => {
        const isInReservation = this.reservationsTenis.some(reservation =>
          reservation.hourSchedule === schedule.time && reservation.localDate === formattedDate
        );
        schedule.reserved = isInReservation;
      });
    }

    saveReservation(date: Date, schedule: HourSchedule, court: string ){
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-indexed
      const day = date.getDate().toString().padStart(2, '0');

      // Format the date as 'yyyy-mm-dd'
      const formattedDate = `${year}-${month}-${day}`;
      const registrationRequest: ReservationRequest = {
            localDate: formattedDate,
            hourSchedule: schedule.time,
            court: court};

      this.clientService.createReservation(registrationRequest).subscribe({
        next: (response: any) =>{
          this.successfullMessage = "The reservation was created successfully";
          setTimeout(() => {
              this.successfullMessage = '';
          }, 2000)
          schedule.reserved = true;
        },
        error: (error: any) =>{
            console.log(error);
        }
      });

      this.fetchFootballReservations();
      this.fetchFootballBasketball();
      this.fetchFootballTenis();
      
    }

  public goToGym(){
    this.router.navigate(['/client/gym']);
  }

  public goToAccount(){
    this.router.navigate(['/client/account']);
  }

  public goToTrainers(){
    this.router.navigate(['/client/trainers']);
  }


  public logout(){
    this.registrationService.clear();
    this.router.navigate(['/login']);
  }

}
