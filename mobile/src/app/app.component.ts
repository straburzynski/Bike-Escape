import { Component, ViewChild } from '@angular/core';
import { AlertController, Events, MenuController, Nav, Platform } from 'ionic-angular';
import { HomePage } from '../pages/home/home';
import { RaceListPage } from '../pages/race-list/race-list';
import { HelpPage } from '../pages/help/help';
import { LoginPage } from '../pages/login/login';
import { SettingsPage } from '../pages/settings/settings';
import { AuthProvider } from '../providers/auth/auth';
import { GlobalProvider } from '../providers/global/global';
import { RaceService } from '../providers/race-service/race-service';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { CheckpointListPage } from '../pages/checkpoint-list/checkpoint-list';
import { CURRENT_USER_IMAGE } from '../config';
import { Storage } from '@ionic/storage';
import { ImageService } from '../providers/image-service/image-service';
import { SmartAudioProvider } from '../providers/smart-audio/smart-audio';
import { TranslateService } from '@ngx-translate/core';

@Component({
    templateUrl: 'app.html',
    providers: [AuthProvider]
})

export class MyApp {
    @ViewChild(Nav) nav: Nav;

    rootPage: any;
    homePage: any = HomePage;
    loginPage: any = LoginPage;
    raceListPage: any = RaceListPage;
    helpPage: any = HelpPage;
    currentRace: any = CheckpointListPage;
    settingsPage: any = SettingsPage;
    userImage: string;

    constructor(private readonly authProvider: AuthProvider,
                public platform: Platform,
                public menu: MenuController,
                public statusBar: StatusBar,
                public splashScreen: SplashScreen,
                public global: GlobalProvider,
                public raceService: RaceService,
                public events: Events,
                public storage: Storage,
                private imageService: ImageService,
                private smartAudio: SmartAudioProvider,
                public translate: TranslateService,
                private alertCtrl: AlertController) {
        this.initializeApp();
        this.subscribeToEvents();
        this.rootPage = HomePage;
        this.authProvider.checkLogin();
        this.raceService.checkCurrentRaces();
        this.authProvider.authUser.subscribe();
        // todo: get all settings;
        this.global.checkCurrentLanguage();
        this.global.checkCurrentSoundSetting();
    }

    initializeApp() {
        this.platform.ready().then(() => {
            this.statusBar.styleLightContent();
            this.splashScreen.hide();
            this.smartAudio.preloadAllSounds();
        });
    }

    subscribeToEvents() {
        this.events.subscribe('userImage', image => {
            this.userImage = image;
        });
        this.events.subscribe('loggedIn', () => {
            this.getUserImage();
        });
    }

    setPage(page) {
        this.smartAudio.play('click');
        this.menu.close();
        this.nav.setRoot(page);
    }

    startRace() {
        this.smartAudio.play('startGame');
        this.menu.close();
        this.raceService.startRace().then(canStartRace => {
            if (canStartRace) {
                this.nav.setRoot(CheckpointListPage);
            }
        });
    }

    logout() {
        this.authProvider.logout();
        this.menu.close();
        this.nav.setRoot(HomePage);
        this.raceService.resetRace();
    }

    showLogoutConfirmation() {
        this.smartAudio.play('click');
        const confirm = this.alertCtrl.create({
            title: this.translate.instant('Logout?'),
            message: this.translate.instant('Do you really want to log out? All progress will NOT be saved.'),
            buttons: [
                {
                    text: this.translate.instant('No'),
                    handler: () => {}
                },
                {
                    text: this.translate.instant('Yes'),
                    handler: () => {
                        this.logout();
                    }
                }
            ]
        });
        confirm.present();
    }

    getUserImage() {
        const userId = this.global.currentUser.id;
        this.storage.get(CURRENT_USER_IMAGE).then(image => {
            if (image != null) {
                this.userImage = image;
            } else {
                this.userImage = this.imageService.getUserImagePath(userId);
                this.imageService.setUserImage(userId);
            }
        });
    }

}
