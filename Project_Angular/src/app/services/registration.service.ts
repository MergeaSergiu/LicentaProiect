import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationRequest } from '../models/registration-request.model';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { ResetPasswordRequest } from '../models/resetPass-request.model';
import { UpdatePasswordRequest } from '../models/updatePassword-request.model';
import { LoginRequest } from '../models/login-request.model';
import { JwtRefreshToken } from '../models/refresh-token.model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
    
  API_PATH = "https://sport-center-cc69b9715563.herokuapp.com/project/api/v1/auth"
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });
  constructor(private httpClient: HttpClient) {}

   handleError(error: HttpErrorResponse){
    return throwError (() => (error.error.errorMessage));
   }
   
   public singUp(signupData: RegistrationRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/registration", signupData, {headers: this.requestHeader})
    .pipe(
      catchError(this.handleError)
    );
   }
   
   public logIn(loginData: LoginRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/login", loginData, {headers: this.requestHeader})
    .pipe(
      catchError(this.handleError)
    );
   }

   public refreshToken(jwtRefreshToken: JwtRefreshToken): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/refreshToken", jwtRefreshToken,{headers: this.requestHeader});
   }

   public sendResetPasswordEmail(resetPassData: ResetPasswordRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/resetPass", resetPassData, {headers: this.requestHeader})
   }

   public updatePassword(updatePassword: UpdatePasswordRequest): Observable<any>{
    return this.httpClient.post(this.API_PATH + "/changePassword", updatePassword, {headers: this.requestHeader})
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
    localStorage.setItem('jwtToken',jwtToken);
   }

   setResetPassToken(resetPassToken: string) {
    localStorage.setItem('resetPassToken',resetPassToken);
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
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('jwtRefreshToken');
    localStorage.removeItem('role');
   }

   public isLoggedIn(){
    return !!localStorage.getItem('jwtToken');
   }

}