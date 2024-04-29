import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { ReservationResponse } from "../models/reservation-response.model";
import { SubscriptionRequest } from "../models/subscription-request.model";
import { TrainerDataResponse } from "../models/trainers-response.model";
import { TrainingClassRequest } from "../models/trainingclass-request.model";
import { UserDataResponse } from "../models/user-response.model";
import { RoleRequest } from "../models/role-request.model";
import { UserSubscriptionsDataResponse } from "../models/userSubscriptionData-response.model";
import { UserSubscriptionRequest } from "../models/userSubscription-request.model";
import { ReservationRequestByAdmin } from "../models/reservationByAdmin-request.model";
import { CourtDetailsResponse } from "../models/court-details-response.model";


@Injectable({
    providedIn: 'root'
  })
export class AdminService{
  

  API_PATH = "http://localhost:8080/project/api/v1";

  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }

   public getAllReservations(): Observable<any>{
    return this.httpClient.get<ReservationResponse[]>(`${this.API_PATH}/reservations`)
   }

   public getAllSubscriptions():Observable<any>{
    return this.httpClient.get<any>(`${this.API_PATH}/subscriptions`)
   }

   public deleteSubscription(id: number){
    return this.httpClient.delete<any>(`${this.API_PATH}/subscriptions/${id}`)
  }

  public createSubscription(subscriptionRequest: SubscriptionRequest){
    return this.httpClient.post(`${this.API_PATH}/subscriptions`, subscriptionRequest)
  }

  public getSubscriptionById(subscriptionId: number) {
    return this.httpClient.get(`${this.API_PATH}/subscriptions/${subscriptionId}`)
  }

  public getTimeSlots(court: string): Observable<any>{
    return this.httpClient.get<CourtDetailsResponse>(`${this.API_PATH}/details/${court}`)
  }

  public updateCourtDetails(courtId: number, startTime: number, endTime: number){
    return this.httpClient.put(`${this.API_PATH}/details/${courtId}`, {}, {params: {
      startTime: startTime.toString(),
      endTime: endTime.toString()
    }})
  }

  public getCourtsDetails(): Observable<any>{
    return this.httpClient.get<CourtDetailsResponse[]>(`${this.API_PATH}/details`)
  }

  public updateSubscriptionData(subscriptionId:number, subscriptionRequest: SubscriptionRequest){
    return this.httpClient.put(`${this.API_PATH}/subscriptions/${subscriptionId}`, subscriptionRequest)
  }

  public getTrainingClassesData(){
    return this.httpClient.get<any>(`${this.API_PATH}/classes`)
  }

  public getTrainingClassData(trainingClassId:number): Observable<any>{
    return this.httpClient.get<any>(`${this.API_PATH}/classes/${trainingClassId}`)
  }

  public deleteTrainingClass(trainingClassId:number): Observable<any>{
    return this.httpClient.delete<any>(`${this.API_PATH}/classes/${trainingClassId}`)
  }

  public createTrainingClass(trainingClassRequest : TrainingClassRequest): Observable<any>{
    return this.httpClient.post(`${this.API_PATH}/classes`, trainingClassRequest);
  }

  public getAllTrainers(): Observable<TrainerDataResponse[]>{
    return this.httpClient.get<TrainerDataResponse[]>(`${this.API_PATH}/users/trainers`)
  }

  public updateTrainingClass(traininClassId: number, trainingClassRequest : TrainingClassRequest){
    return this.httpClient.put(`${this.API_PATH}/classes/${traininClassId}`, trainingClassRequest)
  }

  public getAllUsers(){
    return this.httpClient.get<UserDataResponse[]>(`${this.API_PATH}/users`)
  }

  public getUser(userId: number): Observable<UserDataResponse>{
    return this.httpClient.get<UserDataResponse>(`${this.API_PATH}/users/${userId}`)
  }

  public deleteUser(userId: number): Observable<any>{
    return this.httpClient.delete<any>(`${this.API_PATH}/users/${userId}`)
  }

  public updateUserRole(userId: number, roleRequest: RoleRequest): Observable<any>{
    return this.httpClient.put(`${this.API_PATH}/users/${userId}/role`, roleRequest)
  }

  public getUserSubscriptionsData(userId:number):Observable<UserSubscriptionsDataResponse[]>{
    return this.httpClient.get<UserSubscriptionsDataResponse[]>(`${this.API_PATH}/users/${userId}/subscriptions`)
  }

  public AddUserSubscription(userSubscriptionRequest: UserSubscriptionRequest){
    return this.httpClient.post<any>(`${this.API_PATH}/users/subscriptions`, userSubscriptionRequest)
  }

  public addReservationForUser(reservationRequestByAdmin: ReservationRequestByAdmin){
    return this.httpClient.post<ReservationRequestByAdmin>(`${this.API_PATH}/reservations/admin`, reservationRequestByAdmin)
  }


}