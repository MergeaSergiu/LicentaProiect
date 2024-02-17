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
import { TrainersComponent } from "./client/trainer/trainer.component";

const appRoutes : Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full'},
    { path: 'authentication', component: AuthenticationComponent},
    { path: 'login', component: LoginComponent},
    { path: 'recoverPass', component: RecoverPasswordComponent},
    { path: 'clientDashboard', component: ClientComponent},
    { path: 'adminDashboard', component: AdminComponent},
    { path: 'trainerDashboard', component: TrainerComponent},
    { path: 'reservations', component: ReservationComponent},
    { path: 'gym', component: GymComponent},
    { path: 'account', component: AccountComponent},
    { path: 'trainers', component: TrainersComponent}
]

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule],
})
export class AppRoutingModule {

}