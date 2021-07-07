import { Component } from '@angular/core';
import { App, MenuController } from 'ionic-angular';
import { RaceService } from '../../providers/race-service/race-service';
import { HomePage } from '../home/home';
import { Storage } from '@ionic/storage';
import { CURRENT_RACE_JSON } from '../../config';
import { ImageService } from '../../providers/image-service/image-service';
import { GlobalProvider } from '../../providers/global/global';
import { TimeService } from '../../providers/time-service/time-service';
import { RaceStatus } from '../../models/enums';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-race-finish',
    templateUrl: 'race-finish.html',
})
export class RaceFinishPage {

    currentRace: any;
    summaryImageUrl: string;
    totalTime: number;

    constructor(public menu: MenuController,
                public raceService: RaceService,
                public storage: Storage,
                private imageService: ImageService,
                public app: App,
                public global: GlobalProvider,
                public timeService: TimeService,
                private smartAudio: SmartAudioProvider) {
        this.global.startLoading();
        this.loadCurrentRace();
        this.timeService.deleteCountDownTimer();
    }

    ionViewWillEnter() {
        this.menu.enable(false);
        this.menu.swipeEnable(false);
        this.totalTime = this.calculateTotalTime();
    }

    ionViewWillLeave() {
        this.menu.enable(true);
        this.menu.swipeEnable(true);
    }

    resetRaceAndBack() {
        this.smartAudio.play('click');
        this.raceService.endRace(RaceStatus.FINISHED, this.totalTime).then(raceEntryUpdated => {
            if (raceEntryUpdated) {
                this.raceService.resetRace();
                this.app.getRootNav().setRoot(HomePage);
                this.global.showToast('Race result saved successfully');
            } else {
                this.global.showToast('Error saving race result');
            }
        });
    }

    loadCurrentRace() {
        this.storage.get(CURRENT_RACE_JSON).then(data => {
            this.currentRace = data;
            this.summaryImageUrl = this.imageService.getSummaryImagePath(this.currentRace.id);
            this.global.endLoading();
        });
    }

    calculateTotalTime() {
        return Math.floor(Date.now() / 1000) - this.timeService.raceTime.startTimestamp;
    }

}
