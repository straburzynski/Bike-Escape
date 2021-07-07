import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Storage } from '@ionic/storage';
import { CURRENT_LANGUAGE, CURRENT_SETTINGS_SOUND } from '../../config';
import { LoadingController } from 'ionic-angular';
import { ToastController } from 'ionic-angular';

@Injectable()
export class GlobalProvider {

    public loggedIn = false;
    public currentUser: any;
    public token: string;
    public currentRaceStatus: number; // 0 - not selected, 1 - selected, 2 - in progress
    public currentCheckpointNumber: number;
    public currentLanguage: string;
    public currentCheckpointMarker: any;
    private loading: any;
    public isSoundEnabled: boolean;

    constructor(public loadingCtrl: LoadingController,
                public translate: TranslateService,
                public storage: Storage,
                private readonly toastCtrl: ToastController) {
    }

    checkCurrentLanguage() {
        this.storage.get(CURRENT_LANGUAGE).then((lang) => {
            if (lang != null) {
                this.currentLanguage = lang;
                this.translate.setDefaultLang(lang);

            } else {
                this.currentLanguage = 'en';
                this.translate.setDefaultLang('en');
            }
        });
    }

    checkCurrentSoundSetting() {
        this.storage.get(CURRENT_SETTINGS_SOUND).then(isSoundEnabled => {
            if (isSoundEnabled != null) {
                this.isSoundEnabled = isSoundEnabled;
            } else {
                this.isSoundEnabled = true;
            }
        });
    }

    startLoading(message?): void {
        if (message != null) {
            this.loading = this.loadingCtrl.create({
                spinner: 'hide',
                content: '<img src="assets/img/loader.svg" />' +
                            '<div class="loading-background"><p>' + this.translate.instant(message) + '</p></div>',
                showBackdrop: true
            });
        } else {
            this.loading = this.loadingCtrl.create({
                spinner: 'hide',
                content: '<img src="assets/img/loader.svg" />',
                showBackdrop: true
            });
        }
        this.loading.present();
    }

    endLoading(): void {
        if (this.loading) {
            this.loading.dismiss();
            this.loading = null;
        }
    }

    showToast(message) {
        const toast = this.toastCtrl.create({
            message: this.translate.instant(message),
            duration: 3000,
            position: 'bottom'
        });
        toast.present();
    }

}
