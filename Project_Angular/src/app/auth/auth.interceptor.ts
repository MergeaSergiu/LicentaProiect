import { HttpClient, HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError, switchMap } from "rxjs";
import { RegistrationService } from "../services/registration.service";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor{
    
    constructor(private registrationService: RegistrationService, private http: HttpClient, private router: Router){}
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if(req.headers.get('No-Auth') === 'True'){
            return next.handle(req.clone());
        }

        const access_token = this.registrationService.getToken();
        const refresh_token = this.registrationService.getRefreshToken();
        req = this.addToken(req, access_token);
        
        return next.handle(req).pipe(
            catchError((error: HttpErrorResponse) =>{
                    if(error.status === 417){
                        const headers = new HttpHeaders().set('Authorization', `Bearer ${refresh_token}`);
                       return this.http.post('http://localhost:8080/project/auth/refresh-token', {},{headers}).pipe(
                        switchMap((res: any) => {
                            this.registrationService.setToken(res.access_token);
                            req = this.addToken(req, res.access_token);
                            return next.handle(req);
                        }),
                        catchError((error: HttpErrorResponse) =>{
                            if(error.status === 403){
                                this.registrationService.clear();
                                this.router.navigate(['./login']);
                            }
                            throw throwError(refresh_token);
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
                    Authorization : `Bearer ${access_token}`
                }
            }
        );
    }

}