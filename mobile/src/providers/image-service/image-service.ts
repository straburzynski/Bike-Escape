import 'rxjs/add/operator/map';
import { Injectable } from '@angular/core';
import { CURRENT_USER_IMAGE, SERVER_URL } from '../../config';
import { AuthHttp } from '../auth/auth.http';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { GlobalProvider } from '../global/global';
import { Storage } from '@ionic/storage';
import { Events } from 'ionic-angular';

@Injectable()
export class ImageService {

    private base64JpgPrefix = 'data:image/jpeg;base64,';

    constructor(public http: AuthHttp,
                private global: GlobalProvider,
                public camera: Camera,
                private storage: Storage,
                public events: Events) {
    }

    getUserImagePath(userId) {
        return SERVER_URL + '/image/userImage?userId=' + userId;
    }

    getRaceImagePath(raceId) {
        return SERVER_URL + '/image/raceImage?raceId=' + raceId;
    }

    getRaceImageThumbnailPath(raceId) {
        return SERVER_URL + '/image/raceImageThumbnail?raceId=' + raceId;
    }

    getSummaryImagePath(raceId) {
        return SERVER_URL + '/image/summaryImage?raceId=' + raceId;
    }

    getFailImagePath(raceId) {
        return SERVER_URL + '/image/failImage?raceId=' + raceId;
    }

    getCheckpointImagePath(raceId, checkpointId) {
        return SERVER_URL + '/image/checkpointImage?raceId=' + raceId + '&checkpointId=' + checkpointId;
    }

    getImageFromUrl(url, callback) {
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            const reader = new FileReader();
            reader.onloadend = function () {
                callback(reader.result);
            };
            reader.readAsDataURL(xhr.response);
        };
        xhr.open('GET', url);
        xhr.responseType = 'blob';
        xhr.send();
    }

    sendUserImage(image) {
        const imageJson = {'userImage': image};
        return this.http.post(`${SERVER_URL}/image/userImage`, imageJson);
    }

    saveUserImageFromGallery(): any {
        const galleryOptions: CameraOptions = {
            destinationType: this.camera.DestinationType.DATA_URL,
            sourceType: this.camera.PictureSourceType.PHOTOLIBRARY,
            quality: 100,
            mediaType: this.camera.MediaType.PICTURE,
            allowEdit: true,
            targetHeight: 300,
            targetWidth: 300
        };
        return this.camera.getPicture(galleryOptions)
            .then((base64) => {
                return this.save(base64);
            }, () => {
                this.global.showToast('Error picking image');
            });
    }

    saveUserImageFromCamera(): any {
        const galleryOptions: CameraOptions = {
            destinationType: this.camera.DestinationType.DATA_URL,
            sourceType: this.camera.PictureSourceType.CAMERA,
            quality: 100,
            allowEdit: true,
            targetHeight: 300,
            targetWidth: 300
        };
        return this.camera.getPicture(galleryOptions)
            .then((base64) => {
                return this.save(base64);
            }, () => {
                this.global.showToast('Error taking picture');
            });
    }

    save(base64) {
        const base64Image = this.base64JpgPrefix + base64;
        this.global.startLoading();
        this.sendUserImage(base64Image).subscribe(() => {
                this.global.showToast('Image saved');
                this.global.endLoading();
            },
            (err) => {
                console.error(err);
                this.global.showToast('Error saving image');
                this.global.endLoading();
            });
        this.events.publish('userImage', base64Image);
        return base64Image;
    }

    setUserImage(userId) {
        const userImageUrl = this.getUserImagePath(userId);
        this.getImageFromUrl(userImageUrl, (image) => {
            this.storage.set(CURRENT_USER_IMAGE, image);
            this.events.publish('userImage', image);
        });
    }

}
