import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarModule } from 'ng-sidebar';
import { AppRoutingModule } from '../app-routing.module';
import { DashboardComponent } from './dashboard.component';
import { AllRacesComponent } from './admin/all-races/all-races.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { RaceDetailsComponent } from './race-details/race-details.component';
import { MyRacesComponent } from './my-races/my-races.component';
import { SidebarMenuComponent } from './sidebar-menu/sidebar-menu.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormatDatePipe } from '../pipe/format-date.pipe';
import { CreateRaceComponent } from './create-race/create-race.component';
import { CreateCheckpointComponent } from './create-checkpoint/create-checkpoint.component';
import { CheckpointDetailsComponent } from './checkpoint-details/checkpoint-details.component';
import { DialogsModule } from '../component/dialog/dialog.module';
import { TruncatePipe } from '../pipe/truncate';
import { ImageModule } from '../component/image/image.module';
import { AgmCoreModule } from '@agm/core';
import { AppMaterialModule } from '../app-material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from '../component/header';
import { FooterComponent } from '../component/footer';
import { ImageCropperModule } from 'ng2-img-cropper';
import { MyRacesResultsComponent } from './my-races-results/my-races-results.component';
import { SecondsToTimePipe } from '../pipe/seconds-to-time';
import { StatisticsComponent } from './admin/statistics/statistics.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  imports: [
    CommonModule,
    SidebarModule.forRoot(),
    BrowserAnimationsModule,
    AppMaterialModule,
    ImageCropperModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    DialogsModule,
    ImageModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyARmhZ63Ws1z56-iwBW4UPdCJYyJtseBhs',
      libraries: ['places']
    }),
    TranslateModule,
  ],
  exports: [
    TranslateModule,
  ],
  declarations: [
    HeaderComponent,
    FooterComponent,
    FormatDatePipe,
    TruncatePipe,
    SecondsToTimePipe,
    DashboardComponent,
    UserProfileComponent,
    AllRacesComponent,
    StatisticsComponent,
    MyRacesComponent,
    CreateRaceComponent,
    RaceDetailsComponent,
    SidebarMenuComponent,
    CreateCheckpointComponent,
    CheckpointDetailsComponent,
    MyRacesResultsComponent,
  ]
})
export class DashboardModule {
}
