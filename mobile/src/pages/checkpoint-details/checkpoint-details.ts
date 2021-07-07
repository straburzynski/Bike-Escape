import { Component } from '@angular/core';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';
import { NavController, NavParams } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { AlertController } from 'ionic-angular';
import { RaceService } from '../../providers/race-service/race-service';
import { App } from 'ionic-angular';
import { RaceFinishPage } from '../race-finish/race-finish';
import { CURRENT_CHECKPOINT_MARKER, CURRENT_RACE_CHECKPOINTS_JSON, CURRENT_RACE_ID } from '../../config';
import { GlobalProvider } from '../../providers/global/global';
import { TranslateService } from '@ngx-translate/core';
import { ImageService } from '../../providers/image-service/image-service';
import { CheckpointListPage } from '../checkpoint-list/checkpoint-list';
import { TimeService } from '../../providers/time-service/time-service';
import _ from 'lodash';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-checkpoint-details',
    templateUrl: 'checkpoint-details.html',
})

export class CheckpointDetailsPage {

    selectedCheckpoint: any;
    answer: any = '';
    inputVisible: boolean;
    allCheckpointsNumber: any;
    checkpointImageUrl: string;

    constructor(public navCtrl: NavController,
                public navParams: NavParams,
                public alertCtrl: AlertController,
                public raceService: RaceService,
                public storage: Storage,
                public app: App,
                public global: GlobalProvider,
                private barcodeScanner: BarcodeScanner,
                public translate: TranslateService,
                private imageService: ImageService,
                private timeService: TimeService,
                private smartAudio: SmartAudioProvider) {
        this.loadCheckpoint(navParams.get('checkpointNumber'));
        this.allCheckpointsNumber = navParams.get('allCheckpointsNumber');
    }

    popView() {
        this.smartAudio.play('click');
        this.app.getRootNav().setRoot(CheckpointListPage, {}, {animate: true, direction: 'back'});
    }

    showHint() {
        this.smartAudio.play('click');
        const alert = this.alertCtrl.create({
            title: this.translate.instant('Hint'),
            subTitle: this.selectedCheckpoint.hint,
            buttons: [this.translate.instant('Ok')]
        });
        alert.present();
    }

    badAnswer() {
        this.answer = '';
        const alert = this.alertCtrl.create({
            title: this.translate.instant('Nope'),
            subTitle: this.translate.instant('Wrong answer'),
            buttons: [this.translate.instant('Ok')]
        });
        alert.present();
    }

    clickSendButton() {
        this.sendAnswer(this.answer);
    }

    sendAnswer(text) {
        if (text.toLowerCase() === this.selectedCheckpoint.answer.toLowerCase()) {
            if (!this.isLastCheckpoint()) {
                this.smartAudio.play('good_answer');
                this.raceService.setCurrentCheckpointNumber(this.selectedCheckpoint.number + 1);
                this.navCtrl.push(CheckpointDetailsPage, {
                    checkpointNumber: this.selectedCheckpoint.number + 1,
                    allCheckpointsNumber: this.allCheckpointsNumber
                });
            } else {
                this.smartAudio.play('end_game');
                this.app.getRootNav().setRoot(RaceFinishPage);

            }
        } else {
            this.smartAudio.play('bad_answer');
            this.badAnswer();
        }
    }

    scanQrCode() {
        this.smartAudio.play('click');
        this.barcodeScanner.scan().then((data) => {
            this.sendAnswer(data.text);
        }, (err) => {
            this.global.showToast(err);
        });
    }

    showInput() {
        this.inputVisible = !this.inputVisible;
    }

    isLastCheckpoint() {
        return this.selectedCheckpoint.number === this.allCheckpointsNumber;
    }

    loadCheckpoint(checkpointNumber) {
        this.storage.get(CURRENT_RACE_CHECKPOINTS_JSON).then(data => {
            this.selectedCheckpoint = _.find(data, ['number', checkpointNumber]);
            const currentMarker = ({
                latitude: this.selectedCheckpoint.latitude,
                longitude: this.selectedCheckpoint.longitude,
                name: this.selectedCheckpoint.name
            });
            this.inputVisible = this.selectedCheckpoint.answerType === 'text';
            this.saveCurrentMarker(currentMarker);
        });
        this.storage.get(CURRENT_RACE_ID).then(data => {
            this.checkpointImageUrl = this.imageService.getCheckpointImagePath(data, this.selectedCheckpoint.id);
        });
    }

    isCheckpointDone(checkpointNumber) {
        return checkpointNumber < this.global.currentCheckpointNumber;
    }

    goToCurrentCheckpoint() {
        this.navCtrl.push(CheckpointDetailsPage, {
            checkpointNumber: this.global.currentCheckpointNumber,
            allCheckpointsNumber: this.allCheckpointsNumber
        });
    }

    saveCurrentMarker(position) {
        this.storage.set(CURRENT_CHECKPOINT_MARKER, position);
        this.global.currentCheckpointMarker = position;
        this.raceService.sendCurrentLocation(position);
    }

}
