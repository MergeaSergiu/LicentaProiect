import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AuthenticationComponent } from './auth/auth.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './auth/login.component';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { RecoverPasswordComponent } from './auth/recoverpassword.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ClientComponent } from './client/client.component';
import { TrainerComponent } from './trainerDashboard/trainer.component';
import { AdminComponent } from './admin/admin.component';
import { RegistrationService } from './services/registration.service';
import { AuthInterceptor } from './auth/auth.interceptor';
import { AccountComponent } from './client/account/account.component';
import { GymComponent } from './client/gym/gym.component';
import { ReservationComponent } from './client/reservation/reservation.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthenticationComponent,
    LoginComponent,
    RecoverPasswordComponent,
    ClientComponent,
    TrainerComponent,
    AdminComponent,
    AccountComponent,
    GymComponent,
    ReservationComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    RegistrationService,
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
