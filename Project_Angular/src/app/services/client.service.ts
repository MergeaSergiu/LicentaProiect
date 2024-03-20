import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { ReservationResponse } from "../models/reservation-response.model";
import { ReservationRequest } from "../models/reservation-request.model";
import { UserDataResponse } from "../models/user-response.model";
import { UpdateUserRequest } from "../models/userdata-request.model";
import { TrainingClassResponse } from "../models/trainingclass-response.model";
import { StatusEnrollResponse } from "../models/statusEnroll-response.model";
import { UserSubscriptionsDataResponse } from "../models/userSubscriptionData-response.model";


@Injectable({
    providedIn: 'root'
  })
export class ClientService{

  API_PATH = "http://localhost:8080/project"
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });


  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    console.log(error);
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

   public createReservation(reservationRequest: ReservationRequest):Observable<ReservationRequest>{
      return this.httpClient.post<any>(this.API_PATH + "/api/user/createReservation", reservationRequest);
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

public enrollUserToTrainingClass(id: number){
  const params = new HttpParams().set('id', id.toString());
  return this.httpClient.post<any>(this.API_PATH  + "/api/user/enrollUser", null,{params})
  .pipe(
    catchError(this.handleError)
  );
}

public checkEnrollmentStatus(trainingClassId: number): Observable<StatusEnrollResponse>{
    const params = new HttpParams().set('trainingClassId', trainingClassId.toString());
    return this.httpClient.get<StatusEnrollResponse>(this.API_PATH  + "/api/user/checkEnrollmentStatus", {params})
    .pipe(
      catchError(this.handleError)
      );
  }

  public getUserTrainingClasses(): Observable<TrainingClassResponse[]>{
    return this.httpClient.get<TrainingClassResponse[]>(this.API_PATH + "/api/user/getEnrollClasses")
    .pipe(
      catchError(this.handleError)
    );
  }

  public unEnrolleUser(classId: number){
    const params = new HttpParams().set('classId', classId.toString());
    return this.httpClient.delete<any>(this.API_PATH + "/api/user/unEnroll", {params})
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUserSubscriptionsData(): Observable<UserSubscriptionsDataResponse[]>{
    return this.httpClient.get<UserSubscriptionsDataResponse[]>(this.API_PATH + "/api/admin/users/subscriptionsData")
    .pipe(
      catchError(this.handleError)
    );
  }

}