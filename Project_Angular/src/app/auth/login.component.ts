import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { LoginRequest } from "../models/login-request.model";
import { RegistrationService } from "../services/registration.service";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { UtilComponentComponent } from "../util-component/util-component.component";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {

    alertMessage: string;
    password: string = '';
    user: LoginRequest | undefined;

    constructor(private registrationService: RegistrationService, private router: Router, public _responseBar: MatSnackBar) {
    }

    ngOnInit(): void {
        this.user = {} as LoginRequest;
    }
    onSubmitLogIn(form: NgForm) {
        const logInData: LoginRequest = {
            email: form.value.email,
            password: form.value.password,
            showPassword: false
        };
        this.registrationService.logIn(logInData).subscribe({
            next: (response: any) => {
                this.registrationService.setRole(response.user_Role);
                this.registrationService.setToken(response.access_token);
                this.registrationService.setRefreshToken(response.refresh_token);
                const role = response.user_Role;
                if (role === 'USER') {
                    this.router.navigate(['client/clientDashboard'])
                } else if (role == 'ADMIN') {
                    this.router.navigate(['admin/adminDashboard'])
                } else if (role == 'TRAINER') {
                    this.router.navigate(['trainer/trainerDashboard'])
                }
            },
            error: (errorMessage) => {
                UtilComponentComponent.openSnackBar(errorMessage, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
            }
        });
        form.reset();
    }

}