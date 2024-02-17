import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationRequest } from '../auth/models/registration-request.model';
import { Observable, catchError, throwError } from 'rxjs';
import { ResetPasswordRequest } from '../auth/models/resetPass-request.model';
import { UpdatePasswordRequest } from '../auth/models/updatePassword-request.model';
import { LoginRequest } from '../auth/models/login-request.model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
    

  API_PATH = "http://localhost:8080/project";
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });
  constructor(private httpClient: HttpClient) {

   }

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }
   
   public singUp(signupData: RegistrationRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/auth/registration", signupData, {headers: this.requestHeader})
    .pipe(
      catchError(this.handleError)
    );
   }
   
   public logIn(loginData: LoginRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/auth/authenticate", loginData, {headers: this.requestHeader})
    .pipe(
      catchError(this.handleError)
    );
   }

   public sendResetPasswordEmail(resetPassData: ResetPasswordRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/auth/resetPass", resetPassData, {headers: this.requestHeader})
    .pipe(
      catchError(this.handleError)
    );
   }

   public updatePassword(updatePassword: UpdatePasswordRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/auth/changePassword", updatePassword, {headers: this.requestHeader})
    .pipe(
      catchError(this.handleError)
    );
   }

   public setRole(role: string){
    localStorage.setItem("role", role);
   }

   public getRole(): string{
    return localStorage.getItem('role');
   }

   public setToken(jwtToken: string){
    localStorage.setItem('jwtToken', jwtToken);
   }

   setResetPassToken(resetPassToken: string) {
    localStorage.setItem('resetPassToken', resetPassToken);
  }

  public getResetPassToken(): string{
    return localStorage.getItem('resetPassToken');
   }

   public setRefreshToken(jwtRefreshToken: string){
    localStorage.setItem('jwtRefreshToken',jwtRefreshToken);
   }
  

   public getToken(): string{
    return localStorage.getItem('jwtToken');
   }

   public getRefreshToken(): string{
    return localStorage.getItem('jwtRefreshToken');
   }

   public clear(){
    localStorage.clear();
   }

}