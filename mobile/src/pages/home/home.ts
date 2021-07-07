import { Component } from '@angular/core';
import { AlertController, NavController } from 'ionic-angular';
import { LoginPage } from '../login/login';
import { RaceListPage } from '../race-list/race-list';
import { AuthProvider } from '../../providers/auth/auth';
import { GlobalProvider } from '../../providers/global/global';
import { Storage } from '@ionic/storage';
import { CURRENT_RACE_IMAGE, CURRENT_RACE_JSON } from '../../config';
import { RaceService } from '../../providers/race-service/race-service';
import { DomSanitizer } from '@angular/platform-browser';
import { HelpPage } from '../help/help';
import { CheckpointListPage } from '../checkpoint-list/checkpoint-list';
import { TimeService } from '../../providers/time-service/time-service';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'page-home',
    templateUrl: 'home.html'
})
export class HomePage {

    currentRace: any;
    currentRaceImage: any;

    constructor(public navCtrl: NavController,
                public authProvider: AuthProvider,
                public global: GlobalProvider,
                public storage: Storage,
                public raceService: RaceService,
                private domSanitizer: DomSanitizer,
                public timeService: TimeService,
                private smartAudio: SmartAudioProvider,
                private alertCtrl: AlertController,
                private translate: TranslateService) {
    }

    ionViewWillEnter() {
        this.storage.get(CURRENT_RACE_JSON).then(data => {
            if (data !== null) {
                this.currentRace = data;
            }
        });
        this.storage.get(CURRENT_RACE_IMAGE).then(data => {
            if (data !== null) {
                this.currentRaceImage = this.domSanitizer.bypassSecurityTrustStyle('url(' + data + ')');
            }
        });
    }

    playClick() {
        this.smartAudio.play('click');
    }

    logout() {
        this.playClick();
        this.authProvider.logout();
        this.navCtrl.setRoot(HomePage);
    }

    launchHelpPage() {
        this.playClick();
        this.navCtrl.push(HelpPage);
    }

    launchLoginPage() {
        this.playClick();
        this.navCtrl.push(LoginPage);
    }

    launchRaceListPage() {
        this.playClick();
        this.navCtrl.push(RaceListPage);
    }

    launchCurrentRacePage() {
        this.playClick();
        this.navCtrl.push(CheckpointListPage);
    }

    startRace(): any {
        this.smartAudio.play('start_game');
        this.raceService.startRace().then(canStartRace => {
            if (canStartRace) {
                this.navCtrl.push(CheckpointListPage);
            }
        });
    }

    unloadRace() {
        this.playClick();
        const confirm = this.alertCtrl.create({
            title: this.translate.instant('Unload race?'),
            message: this.translate.instant('Do you want unload current race?'),
            buttons: [
                {
                    text: this.translate.instant('No'),
                },
                {
                    text: this.translate.instant('Yes'),
                    handler: () => {
                        this.raceService.resetRace();
                    }
                }
            ]
        });
        confirm.present();
    }

}
