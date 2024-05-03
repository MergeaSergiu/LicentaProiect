import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { RegistrationService } from "../services/registration.service";
import { RegistrationRequest } from "../models/registration-request.model";
import { MatSnackBar } from "@angular/material/snack-bar";
import { UtilComponentComponent } from "../util-component/util-component.component";

@Component({
    selector: 'app-auth',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.css']
})
export class AuthenticationComponent {
    password: string = '';
    showPassword: boolean = false;

    ngOnInit(): void {}

    togglePasswordVisibility() {
        this.showPassword = !this.showPassword;
    }


    constructor(private registrationService: RegistrationService, private _responseBar: MatSnackBar) { }

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
            }, error: (error) => {
                UtilComponentComponent.openSnackBar(error, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
            }
        });
        form.reset();
    }
}