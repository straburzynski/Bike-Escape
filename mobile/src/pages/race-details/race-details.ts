import { AlertController, App, NavController, NavParams } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { Component } from '@angular/core';
import { RaceService } from '../../providers/race-service/race-service';
import { HomePage } from '../home/home';
import { CURRENT_RACE_STATUS, SERVER_URL } from '../../config';
import { ImageService } from '../../providers/image-service/image-service';
import { GlobalProvider } from '../../providers/global/global';
import { AuthHttp } from '../../providers/auth/auth.http';
import { TranslateService } from '@ngx-translate/core';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';
import { RaceStatus } from '../../models/enums';

@Component({
    selector: 'page-race-details',
    templateUrl: 'race-details.html',
})

export class RaceDetailsPage {

    selectedRace: any;
    noRaceFound: boolean;
    raceImageUrl: string;

    constructor(public navCtrl: NavController,
                public navParams: NavParams,
                public raceService: RaceService,
                public app: App,
                public http: AuthHttp,
                private imageService: ImageService,
                public global: GlobalProvider,
                public translate: TranslateService,
                private smartAudio: SmartAudioProvider,
                private alertCtrl: AlertController,
                public storage: Storage) {
        this.global.startLoading();
        this.raceImageUrl = this.imageService.getRaceImagePath(navParams.get('race'));
        this.loadRace(navParams.get('race'));
    }

    closeModal() {
        this.smartAudio.play('click');
        this.navCtrl.pop();
    }

    selectRace(race) {
        this.smartAudio.play('click');
        this.storage.get(CURRENT_RACE_STATUS).then(status => {
            if (status === 2) {
                this.showExitConfirmation(race);
            } else {
                this.setRaceAndRedirect(race);
            }
        });
    }

    setRaceAndRedirect(race) {
        this.raceService.isRaceAlreadyDone(race.id).subscribe(response => {
            if (response.json()) {
                this.global.showToast('Race is already done');
            } else {
                const goToHomePage: Function = () => {
                    this.app.getRootNav().setRoot(HomePage);
                };
                this.raceService.resetRace();
                this.raceService.setCurrentRace(race, goToHomePage);
            }
        });
    }

    showExitConfirmation(race) {
        const confirm = this.alertCtrl.create({
            title: this.translate.instant('Exit game?'),
            message: this.translate.instant('Do you really want to exit game? All progress will NOT be saved.'),
            buttons: [
                {
                    text: this.translate.instant('No'),
                },
                {
                    text: this.translate.instant('Yes'),
                    handler: () => {
                        this.raceService.endRace(RaceStatus.FAILED).then(() => {
                            this.raceService.resetRace();
                            const goToHomePage: Function = () => {
                                this.app.getRootNav().setRoot(HomePage);
                            };
                            this.raceService.setCurrentRace(race, goToHomePage);
                            this.global.showToast('Race result saved successfully');
                        });
                    }
                }
            ]
        });
        confirm.present();
    }

    loadRace(raceId) {
        return new Promise(() => {
            this.http.get(SERVER_URL + '/race/' + raceId)
                .map(res => {
                    if (res.status === 204) {
                        throw new Error(this.translate.instant('No race found') + ' ' + res.status);
                    } else {
                        return res.json();
                    }
                })
                .subscribe(
                    (data) => {
                        this.selectedRace = data;
                        this.global.endLoading();
                    },
                    () => {
                        this.noRaceFound = true;
                        this.global.endLoading();
                    });
        });
    }

    popView() {
        this.smartAudio.play('click');
        this.navCtrl.pop();
    }

}
