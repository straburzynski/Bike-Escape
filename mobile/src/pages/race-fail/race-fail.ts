import { Component, Injector } from '@angular/core';
import { App, MenuController } from 'ionic-angular';
import { RaceService } from '../../providers/race-service/race-service';
import { HomePage } from '../home/home';
import { Storage } from '@ionic/storage';
import { CURRENT_RACE_JSON } from '../../config';
import { ImageService } from '../../providers/image-service/image-service';
import { GlobalProvider } from '../../providers/global/global';
import { RaceStatus } from '../../models/enums';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-race-fail',
    templateUrl: 'race-fail.html',
})
export class RaceFailPage {

    currentRace: any;
    failImageUrl: string;
    private raceService: RaceService;

    constructor(injector: Injector,
                public menu: MenuController,
                public storage: Storage,
                private imageService: ImageService,
                public app: App,
                public global: GlobalProvider,
                private smartAudio: SmartAudioProvider) {
        this.global.startLoading();
        // circular dependency fix
        setTimeout(() => this.raceService = injector.get(RaceService));
        this.loadCurrentRace();
    }

    ionViewDidEnter() {
        this.menu.enable(false);
        this.menu.swipeEnable(false);
    }

    ionViewDidLeave() {
        this.menu.enable(true);
        this.menu.swipeEnable(true);
    }

    resetRaceAndBack() {
        this.smartAudio.play('click');
        this.raceService.endRace(RaceStatus.FAILED).then(
            () => {
                this.raceService.resetRace();
                this.app.getRootNav().setRoot(HomePage);
                this.global.showToast('Race result saved successfully');
            }, () => {
                this.global.showToast('Error saving race result');
            });
    }

    loadCurrentRace() {
        this.storage.get(CURRENT_RACE_JSON).then(data => {
            this.currentRace = data;
            this.failImageUrl = this.imageService.getFailImagePath(this.currentRace.id);
            this.global.endLoading();
        });
    }
}
