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
import { PaymentData } from "../models/payment-data.model";


@Injectable({
    providedIn: 'root'
  })
export class ClientService{

  API_PATH = "http://localhost:8080/project/api/v1"
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });


  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    console.log(error);
    return throwError (() => (error.error.errorMessage));
   }

   public getReservations(court: string): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(`${this.API_PATH}/reservations/${court}`)
    .pipe(
      catchError(this.handleError)
      );
   }

   public getAllReservationsForClient(): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(this.API_PATH + "/user/clientReservations")
    .pipe(
      catchError(this.handleError)
    );
   }

   public createReservation(reservationRequest: ReservationRequest):Observable<ReservationRequest>{
      return this.httpClient.post<any>(this.API_PATH + "/reservations", reservationRequest);
   }

  public deleteReservation(reservationId: number){
    return this.httpClient.delete<any>(`${this.API_PATH}/reservations/${reservationId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUserProfileData(): Observable<UserDataResponse>{
    return this.httpClient.get<UserDataResponse>(`${this.API_PATH}/users/profile`)
    .pipe(
      catchError(this.handleError)
    );
}


public updateUserData(updateUserRequest:UpdateUserRequest): Observable<any>{
  return this.httpClient.put<UpdateUserRequest>(`${this.API_PATH}/users`, updateUserRequest)
  .pipe(
    catchError(this.handleError)
  );
}

public getTrainerClasses(): Observable<TrainingClassResponse[]>{
  return this.httpClient.get<TrainingClassResponse[]>(`${this.API_PATH}/users/trainer/classes`)
  .pipe(
    catchError(this.handleError)
  );
}

public enrollUserToTrainingClass(trainingClassId: number){
  return this.httpClient.post<any>(`${this.API_PATH}/users/classes/${trainingClassId}`, null)
  .pipe(
    catchError(this.handleError)
  );
}

public checkEnrollmentStatus(trainingClassId: number): Observable<StatusEnrollResponse>{
    return this.httpClient.get<StatusEnrollResponse>(`${this.API_PATH}/users/classes/${trainingClassId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUserTrainingClasses(): Observable<TrainingClassResponse[]>{
    return this.httpClient.get<TrainingClassResponse[]>(`${this.API_PATH}/users/classes`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public unEnrolleUser(trainingClassId: number){
    return this.httpClient.delete<any>(`${this.API_PATH}/users/classes/${trainingClassId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUserSubscriptionsData(): Observable<UserSubscriptionsDataResponse[]>{
    return this.httpClient.get<UserSubscriptionsDataResponse[]>(`${this.API_PATH}/users/subscriptions`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getSubscriptionById(subscriptionId: number): Observable<any> {
    return this.httpClient.get<any>(`${this.API_PATH}/subscriptions/${subscriptionId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public createPaymentIntent(paymentData: PaymentData): Observable<any> {
    return this.httpClient.post<any>(this.API_PATH + "/payment/payment-intent", paymentData)
    .pipe(
      catchError(this.handleError)
    );
  }

  public AddUserSubscriptionByCard(subscriptionId: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API_PATH}/users/subscriptions/${subscriptionId}`, null)
    .pipe(
      catchError(this.handleError)
    );
  }

  public checkUserActiveSubscriptions(): Observable<any> {
    return this.httpClient.get<any>(`${this.API_PATH}/users/activeSubscriptions`)
    .pipe(
      catchError(this.handleError)
      );
  }

  

}