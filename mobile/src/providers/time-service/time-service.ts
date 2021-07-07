import 'rxjs/add/operator/map';
import { Injectable } from '@angular/core';
import { Storage } from '@ionic/storage';
import { CURRENT_RACE_JSON, CURRENT_RACE_TIME } from '../../config';
import { SimpleTimer } from 'ng2-simple-timer';
import { App } from 'ionic-angular';
import { RaceFailPage } from '../../pages/race-fail/race-fail';

@Injectable()
export class TimeService {

    public raceTime: any;
    public timeLeft: any;
    timerId: string;

    constructor(public storage: Storage,
                private st: SimpleTimer,
                public app: App) {
    }

    startCountDownTimer() {
        this.storage.get(CURRENT_RACE_JSON).then(data => {
            this.saveRaceTime(data.estimatedTime * 60); // convert to seconds
            this.createCountDownTimer();
            this.subscribeTimer();
        });
    }

    resumeCountDownTimer() {
        this.storage.get(CURRENT_RACE_TIME).then(data => {
            this.loadRaceTime(data);
            this.createCountDownTimer();
            this.subscribeTimer();
        });
    }

    saveRaceTime(duration: number) {
        this.raceTime = {
            'startTimestamp': Math.floor(Date.now() / 1000),
            'endTimestamp': Math.floor(Date.now() / 1000 + duration),
            'duration': duration
        };
        this.storage.set(CURRENT_RACE_TIME, this.raceTime);
    }

    loadRaceTime(time) {
        this.raceTime = {
            'startTimestamp': time.startTimestamp,
            'endTimestamp': time.endTimestamp,
            'duration': time.duration
        };
    }

    createCountDownTimer() {
        this.st.newTimer('raceCounter', 1);
    }

    subscribeTimer() {
        if (this.timerId) {
            this.st.unsubscribe(this.timerId);
            this.timerId = undefined;
        }
        this.timerId = this.st.subscribe('raceCounter', () => this.timerCallback());
    }

    deleteCountDownTimer() {
        this.st.delTimer('raceCounter');
    }

    timerCallback() {
        const timestamp = Math.floor(Date.now() / 1000);
        this.timeLeft = this.raceTime.endTimestamp - timestamp;
        if (timestamp >= this.raceTime.endTimestamp) {
            this.st.delTimer('raceCounter');
            this.app.getRootNav().setRoot(RaceFailPage);
        }
    }

}
