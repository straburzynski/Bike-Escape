import { Injectable } from '@angular/core';
import { Platform } from 'ionic-angular';
import { NativeAudio } from '@ionic-native/native-audio';
import { GlobalProvider } from '../global/global';

@Injectable()
export class SmartAudioProvider {

    audioType = 'html5';
    sounds: any = [];

    constructor(public nativeAudio: NativeAudio,
                private platform: Platform,
                private global: GlobalProvider) {
        this.checkPlatform();
    }

    preloadAllSounds() {
        this.preload('click', 'assets/audio/click.wav');
        this.preload('start_game', 'assets/audio/start_game.wav');
        this.preload('good_answer', 'assets/audio/good_answer.wav');
        this.preload('bad_answer', 'assets/audio/bad_answer.wav');
        this.preload('end_game', 'assets/audio/end_game.wav');
    }

    private checkPlatform() {
        if (this.platform.is('cordova')) {
            this.audioType = 'native';
        }
    }

    preload(key, asset) {
        if (this.audioType === 'html5') {
            const audio = {
                key: key,
                asset: asset,
                type: 'html5'
            };
            this.sounds.push(audio);
        } else {
            this.nativeAudio.preloadSimple(key, asset);
            const audio = {
                key: key,
                asset: key,
                type: 'native'
            };
            this.sounds.push(audio);
        }
    }

    play(key) {
        if (this.global.isSoundEnabled) {
            const audio = this.sounds.find((sound) => {
                return sound.key === key;
            });
            if (audio.type === 'html5') {
                const audioAsset = new Audio(audio.asset);
                audioAsset.play();
            } else {
                this.nativeAudio.play(audio.asset);
            }
        }
    }

}
