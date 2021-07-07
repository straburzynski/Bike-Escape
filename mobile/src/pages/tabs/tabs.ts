import { Component } from '@angular/core';
import { MapPage } from '../map/map';
import { CheckpointDetailsPage } from '../checkpoint-details/checkpoint-details';
import { NavParams } from 'ionic-angular';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-tabs',
    templateUrl: 'tabs.html'
})
export class TabsPage {

    tabCheckpointDetails = CheckpointDetailsPage;
    tabMap = MapPage;

    constructor(public navParams: NavParams,
                private smartAudio: SmartAudioProvider) {
    }

    changeTab() {
        this.smartAudio.play('click');
    }
}
