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
    .pipe(
      catchError(this.handleError)
    );
   }

   public getAllSubscriptions():Observable<any>{
    return this.httpClient.get<any>(`${this.API_PATH}/subscriptions`)
    .pipe(
      catchError(this.handleError)
    );
   }

   public deleteSubscription(id: number){
    return this.httpClient.delete<any>(`${this.API_PATH}/subscriptions/${id}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public createSubscription(subscriptionRequest: SubscriptionRequest){
    return this.httpClient.post(`${this.API_PATH}/subscriptions`, subscriptionRequest)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getSubscriptionById(subscriptionId: number) {
    return this.httpClient.get(`${this.API_PATH}/subscriptions/${subscriptionId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public updateSubscriptionData(subscriptionId:number, subscriptionRequest: SubscriptionRequest){
    return this.httpClient.put(`${this.API_PATH}/subscriptions/${subscriptionId}`, subscriptionRequest)
    .pipe(
      catchError(this.handleError)
    ) 
  }

  public getTrainingClassesData(){
    return this.httpClient.get<any>(`${this.API_PATH}/classes`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getTrainingClassData(trainingClassId:number): Observable<any>{
    return this.httpClient.get<any>(`${this.API_PATH}/classes/${trainingClassId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public deleteTrainingClass(trainingClassId:number): Observable<any>{
    return this.httpClient.delete<any>(`${this.API_PATH}/classes/${trainingClassId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public createTrainingClass(trainingClassRequest : TrainingClassRequest){
    return this.httpClient.post(`${this.API_PATH}/classes`, trainingClassRequest)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getAllTrainers(): Observable<TrainerDataResponse[]>{
    return this.httpClient.get<TrainerDataResponse[]>(`${this.API_PATH}/trainers`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public updateTrainingClass(traininClassId: number, trainingClassRequest : TrainingClassRequest){
    return this.httpClient.put(`${this.API_PATH}/classes/${traininClassId}`, trainingClassRequest)
    .pipe(
      catchError(this.handleError)
    )
  }

  public getAllUsers(){
    return this.httpClient.get<UserDataResponse[]>(`${this.API_PATH}/users`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUser(userId: number): Observable<UserDataResponse>{
    return this.httpClient.get<UserDataResponse>(`${this.API_PATH}/users/${userId}`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public deleteUser(userId: number): Observable<any>{
    return this.httpClient.delete<any>(`${this.API_PATH}/users/${userId}`)
    .pipe(
      catchError(this.handleError)
    )
  }

  public updateUserRole(userId: number, roleRequest: RoleRequest): Observable<any>{
    return this.httpClient.put(`${this.API_PATH}/users/${userId}/role`, roleRequest)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getUserSubscriptionsData(userId:number):Observable<UserSubscriptionsDataResponse[]>{
    return this.httpClient.get<UserSubscriptionsDataResponse[]>(`${this.API_PATH}/users/${userId}/subscriptions`)
    .pipe(
      catchError(this.handleError)
    );
  }

  public AddUserSubscription(userSubscriptionRequest: UserSubscriptionRequest){
    return this.httpClient.post<any>(`${this.API_PATH}/users/subscriptions`, userSubscriptionRequest)
    .pipe(
      catchError(this.handleError)
    );
  }

  

}