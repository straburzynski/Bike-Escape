import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
// import { HomeComponent } from './main-page/home';
import { LoginComponent } from './main-page/login';
import { AuthGuard } from './guard';
import { NotFoundComponent } from './main-page/not-found';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserProfileComponent } from './dashboard/user-profile/user-profile.component';
import { AllRacesComponent } from './dashboard/admin/all-races/all-races.component';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { RaceDetailsComponent } from './dashboard/race-details/race-details.component';
import { MyRacesComponent } from './dashboard/my-races/my-races.component';
import { CreateRaceComponent } from './dashboard/create-race/create-race.component';
import { CreateCheckpointComponent } from './dashboard/create-checkpoint/create-checkpoint.component';
import { CheckpointDetailsComponent } from './dashboard/checkpoint-details/checkpoint-details.component';
import { MyRacesResultsComponent } from './dashboard/my-races-results/my-races-results.component';
import { StatisticsComponent } from './dashboard/admin/statistics/statistics.component';

export const routes: Routes = [
  {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: '404', component: NotFoundComponent},
  {
    path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard],
    children: [
      {path: '', redirectTo: 'user-profile', pathMatch: 'full'},
      {path: 'user-profile', component: UserProfileComponent},
      {path: 'all-races', component: AllRacesComponent},
      {path: 'statistics', component: StatisticsComponent},
      {path: 'my-races', component: MyRacesComponent},
      {path: 'my-races-results', component: MyRacesResultsComponent},
      {path: 'create-race', component: CreateRaceComponent},
      {path: 'race-details/:id', component: RaceDetailsComponent},
      {path: 'create-checkpoint/:id', component: CreateCheckpointComponent},
      {path: 'checkpoint-details/:id', component: CheckpointDetailsComponent}
    ]
  },
  {path: '**', redirectTo: '/404'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}]
})

export class AppRoutingModule {
}
