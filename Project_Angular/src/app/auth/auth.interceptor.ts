import {  HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, map, mergeMap, of, throwError } from "rxjs";
import { catchError, switchMap } from "rxjs";
import { RegistrationService } from "../services/registration.service";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { JwtRefreshToken} from "./refresh-token.model";

@Injectable()
export class AuthInterceptor implements HttpInterceptor{
    
    constructor(private registrationService: RegistrationService, private router: Router){}

     
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if(req.headers.get('No-Auth') === 'True'){
            return next.handle(req.clone());
        }

        if(this.registrationService.getToken()){
            req = this.addToken(req, this.registrationService.getToken());
        }
        
        const refresh_token: JwtRefreshToken = {
            refreshToken: this.registrationService.getRefreshToken()
        }

       
        return next.handle(req).pipe(
            catchError((error: any) =>{
                    if(error.status === 401){
                       return this.registrationService.refreshToken(refresh_token).pipe(
                        switchMap((res: any) => {
                            this.registrationService.setToken(res.access_token);
                            req = this.addToken(req, res.access_token);
                            return next.handle(req);
                        }),
                        catchError((error: any) =>{
                            if(error.status === 403){
                                this.registrationService.clear();
                                this.router.navigate(['./login']);
                            }
                            return throwError (() => (error.error.errorMessage));
                        })
                       );
                    }
                })
        );

    }

    private addToken(request: HttpRequest<any>, access_token: string){
        return request.clone(
            {
                setHeaders: {
                    'Authorization' : `Bearer ${access_token}`
                }
            }
        );
    }

}