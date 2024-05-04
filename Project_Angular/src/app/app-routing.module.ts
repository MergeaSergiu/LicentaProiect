import { RouterModule, Routes } from "@angular/router";
import { AuthenticationComponent } from "./auth/auth.component";
import { LoginComponent } from "./auth/login.component";
import { NgModule } from "@angular/core";
import { RecoverPasswordComponent } from "./auth/recoverpassword.component";
import { ClientComponent } from "./client/client.component";
import { AdminComponent } from "./admin/admin.component";
import { TrainerComponent } from "./trainerDashboard/trainer.component";
import { ReservationComponent } from "./client/reservation/reservation.component";
import { GymComponent } from "./client/gym/gym.component";
import { AccountComponent } from "./client/account/account.component";
import { authGuard} from "./auth/auth-guard/auth.guard";
import { ReservationdetailsComponent } from "./admin/reservationdetails/reservationdetails.component";
import { GymdetailsComponent } from "./admin/gymdetails/gymdetails.component";
import { UserdetailsComponent } from "./admin/userdetails/userdetails.component";
import { UserSubscriptionsComponent } from "./admin/user-subscriptions/user-subscriptions.component";
import { CheckoutComponent } from "./client/checkout/checkout.component";
import { TrainersComponent } from "./client/trainers/trainers.component";
import { HomepageComponent } from "./homepage/homepage.component";

const appRoutes : Routes = [
    { path: '', component: HomepageComponent, pathMatch: 'full'},
    { path: 'authentication', component: AuthenticationComponent},
    { path: 'login', component: LoginComponent},
    { path: 'recoverPass', component: RecoverPasswordComponent},
    { path: 'home', component: HomepageComponent},
    { path: 'client',
    canActivate: [authGuard],
    data: {roles: 'USER'},
    children: [
    {path: 'clientDashboard', component: ClientComponent, canActivate: [authGuard]},
    { path: 'reservations', component: ReservationComponent, canActivate: [authGuard]},
    { path: 'gym', component: GymComponent, canActivate: [authGuard]},
    { path: 'account', component: AccountComponent, canActivate: [authGuard]},
    { path: 'checkout', component: CheckoutComponent, canActivate: [authGuard]},
    { path: 'trainers', component: TrainersComponent,canActivate: [authGuard]}
    ]},
    { path: 'admin',
    canActivate: [authGuard],
    data: {roles: 'ADMIN'},
    children: [
    {path: 'adminDashboard', component: AdminComponent, canActivate: [authGuard]},
    {path: 'reservations', component: ReservationdetailsComponent, canActivate: [authGuard]},
    {path: 'gym',component: GymdetailsComponent, canActivate: [authGuard]},
    {path: 'users', component: UserdetailsComponent, canActivate: [authGuard]},
    {path: 'account', component: AccountComponent, canActivate: [authGuard]},
    {path: 'subscriptionHistory', component: UserSubscriptionsComponent, canActivate: [authGuard]}
    ]},
    { path: 'trainer',
    canActivate: [authGuard],
    data: {roles: 'TRAINER'},
    children: [
    { path: 'trainerDashboard', component: TrainerComponent, canActivate: [authGuard]},
    { path: 'account', component: AccountComponent, canActivate: [authGuard]},
    ]}
]

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule],
})
export class AppRoutingModule {

}