import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { LoginRequest } from "./models/login-request.model";
import { RegistrationService } from "../services/registration.service";
import { Router } from "@angular/router";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent{

    alertMessage: string;
    password: string = '';

    constructor(private registrationService: RegistrationService,
        private router: Router) { 
    }
    
    onSubmitLogIn(form: NgForm){
        const logInData: LoginRequest = {
            email: form.value.email,
            password: form.value.password
        };
        this.registrationService.logIn(logInData).subscribe({
            next: (response: any) =>{
                this.registrationService.setRole(response.user_Role);
                this.registrationService.setToken(response.access_token);
                this.registrationService.setRefreshToken(response.refresh_token);
                const role = response.user_Role;
                console.log(response);
                if(role === 'CLIENT'){
                    this.router.navigate(['/clientDashboard'])
                }else if(role == 'ADMIN'){
                    this.router.navigate(['/adminDashboard'])
                }else if(role == 'TRAINER'){
                    this.router.navigate(['/trainerDashboard'])
                }
            },
            error: (errorMessage) => {
                this.alertMessage = errorMessage;
                setTimeout(() => {
                    this.alertMessage = '';
                }, 2000)
                }
            });
            form.reset();
    }

}