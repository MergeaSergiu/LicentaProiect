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
import { provideHttpClient, withFetch } from '@angular/common/http';
import { ClientService } from './services/client.service';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_LOCALE, MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { ReservationdetailsComponent } from './admin/reservationdetails/reservationdetails.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AdminService } from './services/admin.service';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { NavbarComponent } from './navbar/navbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import { TrainersComponent } from './client/trainer/trainer.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { DatePipe } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
  declarations: [
    AppComponent,
    AuthenticationComponent,
    LoginComponent,
    RecoverPasswordComponent,
    ClientComponent,
    TrainersComponent,
    AdminComponent,
    AccountComponent,
    GymComponent,
    ReservationComponent,
    ReservationdetailsComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    MatTableModule,
    MatPaginator,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatDatepickerModule,
    MatButtonModule,
    MatToolbarModule,
    MatCheckboxModule,
    MatIconModule,
    MatTooltipModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}],
    RegistrationService,
    ClientService,
    AdminService,
    provideClientHydration(),
    provideHttpClient(withFetch()),
    provideAnimationsAsync(),
    provideNativeDateAdapter(),
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
