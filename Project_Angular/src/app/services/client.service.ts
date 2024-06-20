import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { ReservationResponse } from "../models/reservation-response.model";
import { ReservationRequest } from "../models/reservation-request.model";
import { UserDataResponse } from "../models/user-response.model";
import { UpdateUserRequest } from "../models/userdata-request.model";
import { TrainingClassResponse } from "../models/trainingclass-response.model";
import { UserSubscriptionsDataResponse } from "../models/userSubscriptionData-response.model";
import { PaymentData } from "../models/payment-data.model";
import { CollaborationResponse } from "../models/collaboration-response.model";


@Injectable({
    providedIn: 'root'
  })
export class ClientService{

  API_PATH = "https://sport-center-cc69b9715563.herokuapp.com/project/api/v1"
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });


  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }

   public getReservations(court: string): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(`${this.API_PATH}/reservations/${court}`)
   }

   public getAllReservationsForClient(): Observable<ReservationResponse[]>{
    return this.httpClient.get<ReservationResponse[]>(`${this.API_PATH}/reservations/user`)
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

public updateUserData(updateUserRequest:UpdateUserRequest): Observable<UpdateUserRequest>{
  return this.httpClient.put<UpdateUserRequest>(`${this.API_PATH}/users`, updateUserRequest)
}

public getTrainerClasses(): Observable<TrainingClassResponse[]>{
  return this.httpClient.get<TrainingClassResponse[]>(`${this.API_PATH}/users/trainer/classes`)
}

public enrollUserToTrainingClass(trainingClassId: number){
  return this.httpClient.post<any>(`${this.API_PATH}/users/classes/${trainingClassId}`, null)
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

public getTrainerCollaborations():Observable<CollaborationResponse[]> {
  return this.httpClient.get<CollaborationResponse[]>(`${this.API_PATH}/collaboration/users/trainers`);
}

public acceptRequestForCollaboration(collaborationId: number): Observable<any>{
  return this.httpClient.put<any>(`${this.API_PATH}/collaboration/users/trainers/${collaborationId}`, null)
}

public declineRequestForCollaboration(collaborationId: number){
  return this.httpClient.delete<any>(`${this.API_PATH}/collaboration/users/trainers/${collaborationId}`);
}

public finishCollaborationWithUser(collaborationId: number){
  return this.httpClient.put<any>(`${this.API_PATH}/collaboration/users/trainers/ended/${collaborationId}`,null)
}

public getCollaborationsForUser():Observable<CollaborationResponse[]>{
  return this.httpClient.get<CollaborationResponse[]>(`${this.API_PATH}/collaboration/users`)
}

}