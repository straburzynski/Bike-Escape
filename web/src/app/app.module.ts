import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule, RequestOptions, XHRBackend } from '@angular/http';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './main-page/home';
import { LoginComponent } from './main-page/login';
import { AuthGuard } from './guard';
import { NotFoundComponent } from './main-page/not-found';
import { DashboardModule } from './dashboard/dashboard.module';
import { RaceService } from './service/race.service';
import { ImageService } from './service/image.service';
import { AuthService } from './service/auth.service';
import { UtilsService } from './service/utils.service';
import { ConfigService } from './service/config.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppMaterialModule } from './app-material.module';
import { AuthHttp } from './service/auth.http';
import { Router } from '@angular/router';
import { ImageCropperModule } from 'ng2-img-cropper';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { ToastNotificationClientModule, ToastNotificationCoreModule } from 'ngx-toast-notifications';
import { StatisticsService } from './service/statistics.service';
import { LoaderComponent } from './component/loader/loader.component';

export function httpFactory(backend: XHRBackend,
                            defaultOptions: RequestOptions,
                            router: Router,
                            authService: AuthService) {
  return new AuthHttp(backend, defaultOptions, router, authService);
}

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    NotFoundComponent,
    LoaderComponent
  ],
  imports: [
    BrowserAnimationsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    AppMaterialModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    AppRoutingModule,
    ImageCropperModule,
    DashboardModule,
    ToastNotificationCoreModule.forRoot({lifetime: 3000}),
    ToastNotificationClientModule
  ],
  providers: [
    AuthGuard,
    AuthService,
    {
      provide: AuthHttp,
      useFactory: httpFactory,
      deps: [XHRBackend, RequestOptions, Router, AuthService]
    },
    UtilsService,
    ConfigService,
    RaceService,
    ImageService,
    StatisticsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
