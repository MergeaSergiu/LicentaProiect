import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { RegistrationService } from "../services/registration.service";
import { ResetPasswordRequest } from "../models/resetPass-request.model";
import { UpdatePasswordRequest } from "../models/updatePassword-request.model";
import { MatSnackBar } from "@angular/material/snack-bar";
import { UtilComponentComponent } from "../util-component/util-component.component";
import { Router } from "@angular/router";

@Component({
    selector: 'app-recoverPass',
    templateUrl: './recoverpassword.component.html',
    styleUrls: ['./recoverpassword.component.css']
})
export class RecoverPasswordComponent {

    showPassword: boolean = false;
    resetPasswordFormSubmitted = false;
    constructor(private registrationService: RegistrationService, private _responseBar: MatSnackBar, private router: Router) {
    }

    togglePasswordVisibility() {
        this.showPassword = !this.showPassword;
    }

    ngOnInit(): void {
        const lastSubmittedForm = localStorage.getItem('lastSubmittedForm');
        if (lastSubmittedForm === 'resetPasswordForm') {
            this.resetPasswordFormSubmitted = false;
        } else if (lastSubmittedForm === 'updatePasswordForm') {
            this.resetPasswordFormSubmitted = true;
        }
    }

    onSubmitResetPassword(form: NgForm) {
        const resetPassRequest: ResetPasswordRequest = {
            email: form.value.email
        };
        this.registrationService.sendResetPasswordEmail(resetPassRequest).subscribe({
            next: (response: any) => {
                this.registrationService.setResetPassToken(response.token);
                this.resetPasswordFormSubmitted = true;
                localStorage.setItem('lastSubmittedForm', 'updatePasswordForm');
                UtilComponentComponent.openSnackBar("Confirm the request in the email", this._responseBar, UtilComponentComponent.SnackbarStates.Success);
            }, error: (errorMessage) => {
                UtilComponentComponent.openSnackBar(errorMessage, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
                this.resetPasswordFormSubmitted = false;
                localStorage.setItem('lastSubmittedForm', 'resetPasswordForm');
            }
        });
        form.reset();
    }

    onSubmitUpdatePassword(form: NgForm) {
        const updatePasswordRequest: UpdatePasswordRequest = {
            newPassword: form.value.newPassword,
            confirmedPassword: form.value.confirmedPassword,
            token: this.registrationService.getResetPassToken()
        }
        this.registrationService.updatePassword(updatePasswordRequest).subscribe({
            next: (response: any) => {
                UtilComponentComponent.openSnackBar(response.passwordResetResponse, this._responseBar, UtilComponentComponent.SnackbarStates.Success);
                this.registrationService.clear();
                this.router.navigate(['/login']);
            }, error: (errorMessage) => {
                UtilComponentComponent.openSnackBar(errorMessage, this._responseBar, UtilComponentComponent.SnackbarStates.Error);
            }
        });
        form.reset();
    }
}