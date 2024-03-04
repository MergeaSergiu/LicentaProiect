import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { ReservationResponse } from "../models/reservation-response.model";
import { ReservationRequest } from "../models/reservation-request.model";
import { UserDataResponse } from "../models/user-response.model";
import { UpdateUserRequest } from "../models/userdata-request.model";
import { TrainingClassResponse } from "../models/trainingclass-response.model";


@Injectable({
    providedIn: 'root'
  })
export class ClientService{

  API_PATH = "http://localhost:8080/project"
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });


  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }

   public getReservations(court: string): Observable<ReservationResponse[]>{
    const params = new HttpParams().set('court', court);
    return this.httpClient.get<ReservationResponse[]>(this.API_PATH + "/api/user/getReservationsByCourt",  {params})
    .pipe(
      catchError(this.handleError)
    );
   }

   public getAllReservationsForClient(): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(this.API_PATH + "/api/user/clientReservations")
    .pipe(
      catchError(this.handleError)
    );
   }

   public createReservation(reservationRequest: ReservationRequest):Observable<any>{
      return this.httpClient.post<any>(this.API_PATH + "/api/user/createReservation", reservationRequest)
      .pipe(
        catchError(this.handleError)
      );
   }

  public deleteReservation(id: number){
    const params = new HttpParams().set('id', id.toString());
    return this.httpClient.delete<any>(this.API_PATH + "/api/user/deleteReservation", { params})
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUserProfileData(): Observable<UserDataResponse>{
    return this.httpClient.get<UserDataResponse>(this.API_PATH + "/api/user/getUserProfileData")
    .pipe(
      catchError(this.handleError)
    );
}


public updateUserData(updateUserRequest:UpdateUserRequest): Observable<any>{
  return this.httpClient.put<UpdateUserRequest>(this.API_PATH + "/api/user/updateUserProfile", updateUserRequest)
  .pipe(
    catchError(this.handleError)
  );
}

public getTrainerClasses(): Observable<TrainingClassResponse[]>{
  return this.httpClient.get<TrainingClassResponse[]>(this.API_PATH + "/api/user/getTrainerClasses")
  .pipe(
    catchError(this.handleError)
  );
}

}