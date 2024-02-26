import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { ReservationResponse } from "../client/reservation/reservation-response.model";


@Injectable({
    providedIn: 'root'
  })
export class AdminService{

  API_PATH = "http://localhost:8080/project"

  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }

   public getAllReservations(): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(this.API_PATH + "/api/admin/allReservations")
    .pipe(
      catchError(this.handleError)
    );
   }



}