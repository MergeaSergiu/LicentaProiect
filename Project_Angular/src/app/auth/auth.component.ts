import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { RegistrationService } from "../services/registration.service";
import { RegistrationRequest } from "../models/registration-request.model";

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

    ngOnInit():void{
        this.registrationService.clear();
    }

    constructor(private registrationService: RegistrationService ) {}

    togglePasswordVisibility(): void {
        this.hide = !this.hide;
    }

    onSubmitSignUp(form: NgForm){
        const signUpData: RegistrationRequest = {
            firstName: form.value.firstName,
            lastName: form.value.lastName,
            email: form.value.email,
            password: form.value.password,
        };
        this.registrationService.singUp(signUpData).subscribe({
            next: (response) =>{
                   this.succesfullMessage = response.registrationResponse;
                   setTimeout(() => {
                    this.succesfullMessage = '';
                }, 2000)
            }, error: (errorMessage) =>{
                this.alertMessage = errorMessage;
                setTimeout(() => {
                    this.alertMessage = '';
                }, 2000)
            }
        });
        form.reset();
    }
}