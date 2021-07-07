import { Component } from '@angular/core';
import { ActionSheetController, NavController, AlertController } from 'ionic-angular';
import { HomePage } from '../home/home';
import { TranslateService } from '@ngx-translate/core';
import { Storage } from '@ionic/storage';
import { CURRENT_LANGUAGE, CURRENT_SETTINGS_SOUND, CURRENT_USER_IMAGE } from '../../config';
import { GlobalProvider } from '../../providers/global/global';
import { AuthProvider } from '../../providers/auth/auth';
import { ImageService } from '../../providers/image-service/image-service';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-settings',
    templateUrl: 'settings.html',
})
export class SettingsPage {

    language: any;
    languages: any;
    name: string;
    userImage: any;

    constructor(public navCtrl: NavController,
                public translate: TranslateService,
                public storage: Storage,
                public global: GlobalProvider,
                private authProvider: AuthProvider,
                private imageService: ImageService,
                private actionSheetCtrl: ActionSheetController,
                private smartAudio: SmartAudioProvider,
                private alertCtrl: AlertController) {
        this.languages = [
            {key: 'en', value: 'English'},
            {key: 'pl', value: 'Polish'}
        ];
    }

    ionViewWillEnter() {
        if (this.global.loggedIn) {
            const userId = this.global.currentUser.id;
            this.name = this.global.currentUser.firstName;
            this.storage.get(CURRENT_USER_IMAGE).then(image => {
                if (image != null) {
                    this.userImage = image;
                } else {
                    this.userImage = this.imageService.getUserImagePath(userId);
                    this.imageService.setUserImage(userId);
                }
            });
        }
    }

    private playClick() {
        this.smartAudio.play('click');
    }

    saveSoundSetting(event) {
        this.storage.set(CURRENT_SETTINGS_SOUND, event.value);
    }

    launchHomPage() {
        this.playClick();
        this.navCtrl.setRoot(HomePage, {}, {animate: true, direction: 'back'});
    }

    changeLanguage() {
        this.translate.use(this.language);
        this.storage.set(CURRENT_LANGUAGE, this.language);
        this.global.currentLanguage = this.language;
    }

    editUser(): void {
        this.playClick();
        this.global.startLoading(this.translate.instant('Saving user profile'));
        const success: Function = () => {
            this.global.endLoading();
        };
        const fail: Function = () => {
            this.global.endLoading();
        };
        this.authProvider.doEditUser(this.name, success, fail);
    }

    openSelectPhoto() {
        this.playClick();
        const actionSheet = this.actionSheetCtrl.create({
            // todo: translate!
            title: this.translate.instant('Choose or take a picture'),
            buttons: [
                {
                    text: this.translate.instant('Take a picture'),
                    handler: () => {
                        this.takePicture();
                    }
                },
                {
                    text: this.translate.instant('Choose pictures'),
                    handler: () => {
                        this.openImagePicker();
                    }
                }, {
                    text: this.translate.instant('Cancel'),
                    role: 'cancel'
                }
            ]
        });
        actionSheet.present();
    }

    openImagePicker() {
        this.imageService.saveUserImageFromGallery().then(image => {
            this.userImage = image;
            this.saveUserImage(image);
        });
    }

    takePicture() {
        this.imageService.saveUserImageFromCamera().then(image => {
            this.userImage = image;
            this.saveUserImage(image);
        });
    }

    saveUserImage(image) {
        this.storage.set(CURRENT_USER_IMAGE, image);
    }

    openFirstRemoveAccountConfirmation() {
        this.playClick();
        const actionSheet = this.actionSheetCtrl.create({
            title: this.translate.instant('Do you want to remove your account?'),
            buttons: [
                {
                    text: this.translate.instant('Yes, remove my account'),
                    handler: () => {
                        this.openSecondAccountConfirmation();
                    }
                },
                {
                    text: this.translate.instant('No'),
                    role: 'cancel'
                }
            ]
        });
        actionSheet.present();
    }

    private openSecondAccountConfirmation() {
        this.playClick();
        const alert = this.alertCtrl.create({
            title: this.translate.instant('Confirm removing account'),
            subTitle: this.translate.instant('You will loose all your user data, are you sure?'),
            buttons: [
                {
                    text: this.translate.instant('Yes, remove'),
                    handler: () => {
                        this.authProvider.doRemoveUser();
                    }
                },
                {
                    text: this.translate.instant('Cancel'),
                    role: 'cancel',
                    handler: () => {
                    }
                }
            ]
        });
        alert.present();
    }
}
