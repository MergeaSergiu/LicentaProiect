import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { ReservationResponse } from "../models/reservation-response.model";
import { SubscriptionRequest } from "../models/subscription-request.model";


@Injectable({
    providedIn: 'root'
  })
export class AdminService{
  

  API_PATH = "http://localhost:8080/project";

  constructor(private httpClient: HttpClient) {}
   
   ngOnInit(){}

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }

   public getAllReservations(): Observable<any>{
    return this.httpClient.get<ReservationResponse[]>(this.API_PATH + "/api/admin/allReservations")
    .pipe(
      catchError(this.handleError)
    );
   }

   public getAllSubscriptions():Observable<any>{
    return this.httpClient.get<any>(this.API_PATH + "/api/admin/getSubscriptions")
    .pipe(
      catchError(this.handleError)
    );
   }

   public deleteSubscription(id: number){
    const params = new HttpParams().set('id', id.toString());
    return this.httpClient.delete<any>(this.API_PATH + "/api/admin/deleteSubscription", {params})
    .pipe(
      catchError(this.handleError)
    );
  }

  public createSubscription(subscriptionRequest: SubscriptionRequest){
    return this.httpClient.post(this.API_PATH + "/api/admin/createSubscription", subscriptionRequest)
    .pipe(
      catchError(this.handleError)
    );
  }

  public getSubscriptionById(id: number) {
    const params = new HttpParams().set('id', id.toString());
    return this.httpClient.get(this.API_PATH  + "/api/admin/getSubscription", {params})
    .pipe(
      catchError(this.handleError)
    );
  }

  public updateSubscriptionData(id:number, subscriptionRequest: SubscriptionRequest){
    const params = new HttpParams().set('id', id.toString());
    return this.httpClient.put(this.API_PATH + "/api/admin/updateSubscription", subscriptionRequest, {params})
    .pipe(
      catchError(this.handleError)
    ) 
  }

}