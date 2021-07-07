import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { SignupPage } from '../signup/signup';
import { AuthProvider } from '../../providers/auth/auth';
import { HomePage } from '../home/home';
import { TranslateService } from '@ngx-translate/core';
import { AlertController } from 'ionic-angular';
import { ActionSheetController } from 'ionic-angular';
import { GlobalProvider } from '../../providers/global/global';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-login',
    templateUrl: 'login.html'
})
export class LoginPage {

    constructor(public navCtrl: NavController,
                private readonly authProvider: AuthProvider,
                public translate: TranslateService,
                public alertCtrl: AlertController,
                public actionSheetCtrl: ActionSheetController,
                public global: GlobalProvider,
                private smartAudio: SmartAudioProvider) {
        this.authProvider.authUser.subscribe(jwt => {
            if (jwt) {
                this.navCtrl.setRoot(HomePage);
            }
        });
    }

    private playClick() {
        this.smartAudio.play('click');
    }

    signup() {
        this.playClick();
        this.navCtrl.push(SignupPage);
    }

    login(value: any): void {
        this.playClick();
        this.global.startLoading(this.translate.instant('Logging in'));
        const success: Function = () => {
            this.global.endLoading();
        };
        const fail: Function = () => {
            this.global.endLoading();
        };
        this.authProvider.doLogin(value, success, fail);
    }

    sendResetPin(email: any): void {
        this.playClick();
        this.global.startLoading(this.translate.instant('Sending reset e-mail'));
        const success: Function = () => {
            this.global.endLoading();
        };
        const fail: Function = () => {
            this.global.endLoading();
        };
        this.authProvider.doSendResetPin(email, success, fail);
    }

    changePasswordWithPin(password: any): void {
        this.playClick();
        this.global.startLoading(this.translate.instant('Saving new password'));
        const success: Function = () => {
            this.global.endLoading();
        };
        const fail: Function = () => {
            this.global.endLoading();
        };
        this.authProvider.doChangePasswordWithPin(password, success, fail);
    }

    launchHomPage() {
        this.playClick();
        this.navCtrl.setRoot(HomePage, {}, {animate: true, direction: 'back'});
    }

    showSendEmailAlert() {
        this.playClick();
        const prompt = this.alertCtrl.create({
            title: this.translate.instant('Reset password'),
            message: this.translate.instant('Enter e-mail, we will send you PIN number for reseting password'),
            inputs: [
                {
                    name: 'email',
                    placeholder: this.translate.instant('E-mail')
                },
            ],
            buttons: [
                {
                    text: this.translate.instant('Cancel'),
                },
                {
                    text: this.translate.instant('Send'),
                    handler: data => {
                        this.sendResetPin(data);
                    }
                }
            ]
        });
        prompt.present();
    }

    showEnterPinAlert() {
        this.playClick();
        const prompt = this.alertCtrl.create({
            title: this.translate.instant('Enter PIN'),
            message: this.translate.instant('Enter PIN from e-mail and set new password for your account'),
            inputs: [
                {
                    name: 'token',
                    placeholder: 'PIN'
                },
                {
                    name: 'password',
                    placeholder: this.translate.instant('Password'),
                    type: 'password'
                },
            ],
            buttons: [
                {
                    text: this.translate.instant('Cancel'),
                },
                {
                    text: this.translate.instant('Send'),
                    handler: data => {
                        this.changePasswordWithPin(data);
                    }
                }
            ]
        });
        prompt.present();
    }

    openResetPassword() {
        this.playClick();
        const actionSheet = this.actionSheetCtrl.create({
            title: this.translate.instant('Reset password'),
            buttons: [
                {
                    text: this.translate.instant('1. Enter your e-mail'),
                    handler: () => {
                        this.showSendEmailAlert();
                    }
                }, {
                    text: this.translate.instant('2. Reset password with PIN'),
                    handler: () => {
                        this.showEnterPinAlert();
                    }
                }, {
                    text: this.translate.instant('Cancel'),
                    role: 'cancel'
                }
            ]
        });
        actionSheet.present();
    }
}
