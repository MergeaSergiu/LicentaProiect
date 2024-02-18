import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { RegistrationService } from "../services/registration.service";
import { ResetPasswordRequest } from "./models/resetPass-request.model";
import { UpdatePasswordRequest } from "./models/updatePassword-request.model";

@Component({
    selector: 'app-recoverPass',
    templateUrl: './recoverpassword.component.html',
    styleUrls: ['./recoverpassword.component.css']
})
export class RecoverPasswordComponent{

    alertMessageResetEmail : string;
    alertMessageUpdatePassword : string;
    succesfullMessageUpdatePassword: string;
    resetPasswordFormSubmitted = false;
    constructor(private registrationService: RegistrationService) { 
    }

    ngOnInit(): void {
        // Determine which form was last submitted and set the flag accordingly
        const lastSubmittedForm = localStorage.getItem('lastSubmittedForm');
        if (lastSubmittedForm === 'resetPasswordForm') {
          this.resetPasswordFormSubmitted = false;
        } else if (lastSubmittedForm === 'updatePasswordForm') {
          this.resetPasswordFormSubmitted = true;
        }
      }

    onSubmitResetPassword(form: NgForm){
        const resetPassRequest: ResetPasswordRequest = {
            email: form.value.email
        };
            this.registrationService.sendResetPasswordEmail(resetPassRequest).subscribe({
                next: (response: any) =>{
                    this.registrationService.setResetPassToken(response.token);
                    this.resetPasswordFormSubmitted = true;
                    localStorage.setItem('lastSubmittedForm', 'updatePasswordForm');
                },error: (errorMessage) => {
                    this.alertMessageResetEmail = errorMessage; 
                    setTimeout(() => {
                        this.alertMessageResetEmail = '';
                    }, 2000);
                    this.resetPasswordFormSubmitted = false;
                    localStorage.setItem('lastSubmittedForm', 'resetPasswordForm');
                  }
                });
                
            form.reset();
    }

    onSubmitUpdatePassword(form :NgForm){
        const updatePasswordRequest: UpdatePasswordRequest = {
            newPassword: form.value.newPassword,
            confirmedPassword: form.value.confirmedPassword,
            token : this.registrationService.getResetPassToken()
        }
        console.log(updatePasswordRequest);
        this.registrationService.updatePassword(updatePasswordRequest).subscribe({
            next: (response: any) =>{
                this.succesfullMessageUpdatePassword = response.passwordResetResponse;
                setTimeout(() => {
                    this.succesfullMessageUpdatePassword = '';
                }, 2000);
            }, error: (errorMessage) =>{
                this.alertMessageUpdatePassword = errorMessage;
                setTimeout(() => {
                    this.alertMessageUpdatePassword = '';
                }, 2000);

            }
        });
        form.reset();
    }
}