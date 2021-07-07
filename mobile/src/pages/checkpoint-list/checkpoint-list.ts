import { Component } from '@angular/core';
import { AlertController, App, NavController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { CURRENT_RACE_CHECKPOINTS_JSON, CURRENT_RACE_JSON } from '../../config';
import { GlobalProvider } from '../../providers/global/global';
import { HomePage } from '../home/home';
import { ImageService } from '../../providers/image-service/image-service';
import { TabsPage } from '../tabs/tabs';
import { RaceService } from '../../providers/race-service/race-service';
import { TranslateService } from '@ngx-translate/core';
import { TimeService } from '../../providers/time-service/time-service';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';
import { RaceStatus } from '../../models/enums';

@Component({
    selector: 'checkpoint-list',
    templateUrl: 'checkpoint-list.html',
})

export class CheckpointListPage {

    public checkpoints: any;
    public currentRace: any;
    private raceImageUrl: string;

    constructor(public navCtrl: NavController,
                public storage: Storage,
                public global: GlobalProvider,
                public alertCtrl: AlertController,
                private imageService: ImageService,
                private raceService: RaceService,
                private timeService: TimeService,
                public translate: TranslateService,
                public app: App,
                private smartAudio: SmartAudioProvider) {
    }

    ionViewWillEnter() {
        this.loadCurrentRace();
        this.loadCheckpoints();
    }

    checkpointTapped(event, checkpointNumber) {
        this.smartAudio.play('click');
        this.navCtrl.push(TabsPage, {
            checkpointNumber: checkpointNumber,
            allCheckpointsNumber: this.checkpoints.length
        }, {animate: true, direction: 'forward'});
    }

    loadCurrentRace() {
        this.storage.get(CURRENT_RACE_JSON).then(data => {
            this.currentRace = data;
            this.raceImageUrl = this.imageService.getRaceImagePath(this.currentRace.id);
        });
    }

    loadCheckpoints() {
        this.storage.get(CURRENT_RACE_CHECKPOINTS_JSON).then(data => {
            this.checkpoints = data;
        });
    }

    isCheckpointVisible(checkpointNumber) {
        return checkpointNumber <= this.global.currentCheckpointNumber;
    }

    launchHomPage() {
        this.smartAudio.play('click');
        this.app.getRootNav().setRoot(HomePage, {}, {animate: true, direction: 'back'});
    }

    showExitConfirmation() {
        this.smartAudio.play('click');
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
                            this.app.getRootNav().setRoot(HomePage);
                            this.global.showToast('Race result saved successfully');
                        });
                    }
                }
            ]
        });
        confirm.present();
    }

    resetRace() {
        this.raceService.resetRace();
        this.app.getRootNav().setRoot(HomePage);
    }

}
