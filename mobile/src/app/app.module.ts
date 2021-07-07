import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { MyApp } from './app.component';
import { IonicStorageModule } from '@ionic/storage';
import { Http, HttpModule } from '@angular/http';
import { CustomFormsModule } from 'ng2-validation';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { GoogleMaps } from '@ionic-native/google-maps';
import { Geolocation } from '@ionic-native/geolocation';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';
import { Camera } from '@ionic-native/camera';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { GlobalProvider } from '../providers/global/global';
import { AuthProvider } from '../providers/auth/auth';
import { RaceService } from '../providers/race-service/race-service';
import { TimeService } from '../providers/time-service/time-service';
import { HomePage } from '../pages/home/home';
import { RaceFailPage } from '../pages/race-fail/race-fail';
import { RaceFinishPage } from '../pages/race-finish/race-finish';
import { RaceListPage } from '../pages/race-list/race-list';
import { TabsPage } from '../pages/tabs/tabs';
import { MapPage } from '../pages/map/map';
import { RaceDetailsPage } from '../pages/race-details/race-details';
import { SignupPage } from '../pages/signup/signup';
import { LoginPage } from '../pages/login/login';
import { CheckpointListPage } from '../pages/checkpoint-list/checkpoint-list';
import { CheckpointDetailsPage } from '../pages/checkpoint-details/checkpoint-details';
import { HelpPage } from '../pages/help/help';
import { SettingsPage } from '../pages/settings/settings';
import { ImageService } from '../providers/image-service/image-service';
import { SimpleTimer } from 'ng2-simple-timer';
import { AuthHttp } from '../providers/auth/auth.http';
import { RequestOptions, XHRBackend } from '@angular/http';
import { PipesModule } from '../pipes/pipes.module';
import { SmartAudioProvider } from '../providers/smart-audio/smart-audio';
import { NativeAudio } from '@ionic-native/native-audio';

export function createTranslateLoader(http: Http) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function httpFactory(backend: XHRBackend, defaultOptions: RequestOptions, global: GlobalProvider) {
    return new AuthHttp(backend, defaultOptions, global);
}

@NgModule({
    declarations: [
        MyApp,
        HomePage,
        RaceListPage,
        RaceDetailsPage,
        TabsPage,
        MapPage,
        HelpPage,
        SettingsPage,
        LoginPage,
        SignupPage,
        CheckpointListPage,
        CheckpointDetailsPage,
        RaceFinishPage,
        RaceFailPage
    ],
    imports: [
        BrowserModule,
        IonicModule.forRoot(MyApp, {
            pageTransition: 'md-transition', // 300ms hack
            platforms: {
                ios: {
                    statusbarPadding: true,
                    tabsHideOnSubPages: false
                }
            }
        }),
        HttpModule,
        IonicStorageModule.forRoot({
            name: 'myapp',
            driverOrder: ['sqlite', 'indexeddb', 'websql']
        }),
        CustomFormsModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: (createTranslateLoader),
                deps: [Http]
            }
        }),
        PipesModule
    ],
    bootstrap: [IonicApp],
    entryComponents: [
        MyApp,
        HomePage,
        RaceListPage,
        RaceDetailsPage,
        TabsPage,
        MapPage,
        HelpPage,
        SettingsPage,
        LoginPage,
        SignupPage,
        CheckpointListPage,
        CheckpointDetailsPage,
        RaceFinishPage,
        RaceFailPage
    ],
    providers: [
        StatusBar,
        SplashScreen,
        GlobalProvider,
        {provide: ErrorHandler, useClass: IonicErrorHandler},
        {
            provide: AuthHttp,
            useFactory: httpFactory,
            deps: [XHRBackend, RequestOptions, GlobalProvider]
        },
        AuthProvider,
        RaceService,
        TimeService,
        ImageService,
        Camera,
        BarcodeScanner,
        GoogleMaps,
        Geolocation,
        SimpleTimer,
        SmartAudioProvider,
        NativeAudio
    ]
})

export class AppModule {
}
