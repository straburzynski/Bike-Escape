import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { AuthProvider } from '../../providers/auth/auth';
import { TranslateService } from '@ngx-translate/core';
import { GlobalProvider } from '../../providers/global/global';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-signup',
    templateUrl: 'signup.html'
})
export class SignupPage {

    constructor(private readonly authProvider: AuthProvider,
                public navCtrl: NavController,
                public translate: TranslateService,
                public global: GlobalProvider,
                private smartAudio: SmartAudioProvider) {
    }

    popView() {
        this.smartAudio.play('click');
        this.navCtrl.pop();
    }

    signup(value: any) {
        this.smartAudio.play('click');
        this.global.startLoading(this.translate.instant('Signing in'));
        const success: Function = () => {
            this.global.endLoading();
        };
        const fail: Function = () => {
            this.global.endLoading();
        };
        this.authProvider.doSignup(value, success, fail);
    }

}
