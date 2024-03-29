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
   }

   public getAllReservationsForClient(): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(`${this.API_PATH}/users/reservations`)
   }

   public createReservation(reservationRequest: ReservationRequest):Observable<ReservationRequest>{
      return this.httpClient.post<any>(`${this.API_PATH}/reservations`, reservationRequest);
   }

  public deleteReservation(reservationId: number){
    return this.httpClient.delete<any>(`${this.API_PATH}/reservations/${reservationId}`)
  }

  public getUserProfileData(): Observable<UserDataResponse>{
    return this.httpClient.get<UserDataResponse>(`${this.API_PATH}/users/profile`)
}


public updateUserData(updateUserRequest:UpdateUserRequest): Observable<any>{
  return this.httpClient.put<UpdateUserRequest>(`${this.API_PATH}/users`, updateUserRequest)
}

public getTrainerClasses(): Observable<TrainingClassResponse[]>{
  return this.httpClient.get<TrainingClassResponse[]>(`${this.API_PATH}/users/trainer/classes`)
}

public enrollUserToTrainingClass(trainingClassId: number){
  return this.httpClient.post<any>(`${this.API_PATH}/users/classes/${trainingClassId}`, null)
}

public checkEnrollmentStatus(trainingClassId: number): Observable<StatusEnrollResponse>{
    return this.httpClient.get<StatusEnrollResponse>(`${this.API_PATH}/users/classes/${trainingClassId}`)
}

  public getUserTrainingClasses(): Observable<TrainingClassResponse[]>{
    return this.httpClient.get<TrainingClassResponse[]>(`${this.API_PATH}/users/classes`)
}

  public unEnrolleUser(trainingClassId: number){
    return this.httpClient.delete<any>(`${this.API_PATH}/users/classes/${trainingClassId}`)
}

  public getUserSubscriptionsData(): Observable<UserSubscriptionsDataResponse[]>{
    return this.httpClient.get<UserSubscriptionsDataResponse[]>(`${this.API_PATH}/users/subscriptions`)
}

  public getSubscriptionById(subscriptionId: number): Observable<any> {
    return this.httpClient.get<any>(`${this.API_PATH}/subscriptions/${subscriptionId}`)
}

  public createPaymentIntent(paymentData: PaymentData): Observable<any> {
    return this.httpClient.post<any>(this.API_PATH + "/payment/payment-intent", paymentData)
}

  public AddUserSubscriptionByCard(subscriptionId: number): Observable<any> {
    return this.httpClient.post<any>(`${this.API_PATH}/users/subscriptions/${subscriptionId}`, null)
}

  public checkUserActiveSubscriptions(): Observable<any> {
    return this.httpClient.get<any>(`${this.API_PATH}/users/activeSubscriptions`)
}

public sendCollabRequest(trainerId: number): Observable<any>{
  return this.httpClient.post<any>(`${this.API_PATH}/collaboration/users/${trainerId}`, null)
}

public getTrainerCollaborations():Observable<any> {
  return this.httpClient.get<any>(`${this.API_PATH}/collaboration/users/trainers`);
}

public acceptRequestForCollaboration(collaborationId: number): Observable<any>{
  return this.httpClient.post<any>(`${this.API_PATH}/collaboration/users/${collaborationId}`, null)
}

}