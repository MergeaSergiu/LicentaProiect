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
import { MatButtonModule} from '@angular/material/button';
import { NavbarComponent } from './navbarClient/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { TrainersComponent } from './client/trainer/trainer.component';
import { MatCheckboxModule} from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { GymdetailsComponent } from './admin/gymdetails/gymdetails.component';
import { NavbarAdminComponent } from './navbar-admin/navbar-admin.component';
import { PopupSubscriptionComponent } from './popup-subscription/popup-subscription.component';
import { PopupUpdateSubscriptionComponent } from './popup-update-subscription/popup-update-subscription.component';
import { MatSelectModule } from '@angular/material/select';
import { PopupCreateTrClassComponent } from './popup-create-tr-class/popup-create-tr-class.component';
import { PopupEditTrClassComponent } from './popup-edit-tr-class/popup-edit-tr-class.component';
import { UserdetailsComponent } from './admin/userdetails/userdetails.component';

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
    GymdetailsComponent,
    NavbarAdminComponent,
    PopupSubscriptionComponent,
    PopupUpdateSubscriptionComponent,
    PopupCreateTrClassComponent,
    PopupEditTrClassComponent,
    UserdetailsComponent,
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
    MatTooltipModule,
    MatIconModule,
    MatSelectModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    RegistrationService,
    ClientService,
    AdminService,
    provideClientHydration(),
    provideHttpClient(withFetch()),
    provideAnimationsAsync(),
    provideNativeDateAdapter(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
