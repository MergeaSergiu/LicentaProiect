import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { RegistrationService } from "../services/registration.service";
import { RegistrationRequest } from "../models/registration-request.model";
import { MatSnackBar, MatSnackBarConfig } from "@angular/material/snack-bar";
import { UtilComponentComponent } from "../util-component/util-component.component";

@Component({
    selector: 'app-auth',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.css']
})
export class AuthenticationComponent {
    isAdmin: boolean;
    alertMessage: string;
    succesfullMessage: string;
    password: string = '';
    hide: boolean = true;

    ngOnInit(): void {
        this.registrationService.clear();
    }

    constructor(private registrationService: RegistrationService, private _responseBar: MatSnackBar) { }

    togglePasswordVisibility(): void {
        this.hide = !this.hide;
    }

    onSubmitSignUp(form: NgForm) {
        const signUpData: RegistrationRequest = {
            firstName: form.value.firstName,
            lastName: form.value.lastName,
            email: form.value.email,
            password: form.value.password,
        };
        this.registrationService.singUp(signUpData).subscribe({
            next: (response) => {
                UtilComponentComponent.openSnackBar(response.registrationResponse, this._responseBar, UtilComponentComponent.SnackbarStates.Success);
            }, error: (errorMessage) => {
                UtilComponentComponent.openSnackBar(errorMessage, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
            }
        });
        form.reset();
    }
}