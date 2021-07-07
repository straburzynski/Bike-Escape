import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HomePage } from '../home/home';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-help',
    templateUrl: 'help.html',
})
export class HelpPage {

    constructor(public navCtrl: NavController,
                private smartAudio: SmartAudioProvider) {
    }

    launchHomPage() {
        this.smartAudio.play('click');
        this.navCtrl.setRoot(HomePage, {}, {animate: true, direction: 'back'});
    }
}
