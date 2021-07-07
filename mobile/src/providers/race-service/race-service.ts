import 'rxjs/add/operator/map';
import { Subject } from 'rxjs/Subject';
import { Injectable } from '@angular/core';
import { Storage } from '@ionic/storage';
import {
    CURRENT_CHECKPOINT_MARKER,
    CURRENT_CHECKPOINT_NUMBER,
    CURRENT_RACE_CHECKPOINTS_JSON,
    CURRENT_RACE_ID,
    CURRENT_RACE_IMAGE,
    CURRENT_RACE_JSON,
    CURRENT_RACE_STATUS,
    CURRENT_RACE_TIME,
    SERVER_URL
} from '../../config';
import { GlobalProvider } from '../global/global';
import { App } from 'ionic-angular';
import { RaceFailPage } from '../../pages/race-fail/race-fail';
import * as moment from 'moment';
import { TimeService } from '../time-service/time-service';
import { ImageService } from '../image-service/image-service';
import { AuthHttp } from '../auth/auth.http';
import { RaceStatus } from '../../models/enums';
import { Observable } from 'rxjs/Observable';
import { SearchFilters } from '../../models/search-filters';
import { URLSearchParams } from '@angular/http';

@Injectable()
export class RaceService {

    selectedRaceCheckpoints: any;
    private currentCheckpointLocation = new Subject<any>();

    constructor(public http: AuthHttp,
                public storage: Storage,
                public global: GlobalProvider,
                private timeService: TimeService,
                private imageService: ImageService,
                public app: App) {
    }

    loadByFilters(searchFilters: SearchFilters): Observable<any> {

        const params: URLSearchParams = new URLSearchParams();
        params.append('page', searchFilters.page.toString());
        params.append('size', searchFilters.size.toString());
        if (searchFilters.name) {
            params.set('name', searchFilters.name);
        }
        if (searchFilters.city) {
            params.set('city', searchFilters.city);
        }
        if (searchFilters.difficulty) {
            params.set('difficulty', searchFilters.difficulty);
        }
        if (searchFilters.direction) {
            params.set('direction', searchFilters.direction);
        }
        if (searchFilters.column) {
            params.set('column', searchFilters.column);
        }
        return this.http.get(`${SERVER_URL}/race/filters`, {search: params})
            .map(res => res.json());
    }

    startRace() {
        return this.getCurrentRaceId().then(raceId => {
            return new Promise(resolve => {
                this.createRaceEntry(raceId).subscribe(() => {
                    this.setCurrentRaceStatus(2);
                    this.timeService.startCountDownTimer();
                    resolve(true);
                }, error => {
                    if (error['_body']) {
                        this.global.showToast(error.json().message);
                    }
                    resolve(false);
                });
            });
        }, () => {
            this.global.showToast('Error getting race');
        });
    }

    endRace(raceStatus: RaceStatus, totalTime?: number) {
        return this.getCurrentRaceId().then(raceId => {
            return new Promise(resolve => {
                this.updateRaceEntry(raceId, totalTime, raceStatus).subscribe(() => {
                    resolve(true);
                }, error => {
                    if (error['_body']) {
                        this.global.showToast(error.json().message);
                    }
                    resolve(false);
                });
            });
        }, () => {
            this.global.showToast('Error getting race');
        });
    }

    createRaceEntry(raceId: number) {
        const raceEntryJson = {
            'userId': this.global.currentUser.id,
            'raceId': raceId,
            'raceStatus': RaceStatus.IN_PROGRESS
        };
        return this.http.post(SERVER_URL + '/race/history', raceEntryJson);
    }

    updateRaceEntry(raceId: number, totalTime?: number, raceStatus?: RaceStatus) {
        const raceEntryJson = {
            'userId': this.global.currentUser.id,
            'raceId': raceId,
            'totalTime': totalTime,
            'raceStatus': raceStatus
        };
        return this.http.put(SERVER_URL + '/race/history', raceEntryJson);
    }

    isRaceAlreadyDone(raceId: number) {
        return this.http.get(SERVER_URL + '/race/history/' + raceId);
    }

    loadSelectedRace(raceId) {
        return new Promise(resolve => {
            this.http.get(SERVER_URL + '/race/' + raceId + '/checkpoint')
                .map(res => res.json())
                .subscribe(data => {
                    this.selectedRaceCheckpoints = data;
                    resolve(this.selectedRaceCheckpoints);
                    this.storage.set(CURRENT_RACE_CHECKPOINTS_JSON, this.selectedRaceCheckpoints);
                });
        });
    }

    getCurrentRaceId() {
        return this.storage.get(CURRENT_RACE_ID);
    }

    removeCurrentRaceId() {
        this.storage.remove(CURRENT_RACE_ID);
    }

    setCurrentRace(race, goToHomePage: Function) {
        this.storage.set(CURRENT_RACE_ID, race.id);
        this.loadSelectedRace(race.id);
        this.setCurrentCheckpointNumber(1);
        this.storage.set(CURRENT_RACE_JSON, race);
        this.setCurrentRaceStatus(1);
        this.toDataURL(this.imageService.getRaceImagePath(race.id), (data) => {
            this.storage.set(CURRENT_RACE_IMAGE, data).then(() => {
                goToHomePage();
            });
        });
    }

    toDataURL(url, callback) {
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

    checkCurrentRaces() {
        this.storage.get(CURRENT_RACE_STATUS).then(status => {
            if (status != null && this.global.currentRaceStatus == null) {
                this.global.currentRaceStatus = status;
            } else {
                this.global.currentRaceStatus = 0;
            }
        });
        this.storage.get(CURRENT_CHECKPOINT_NUMBER).then(number => {
            if (status != null && this.global.currentCheckpointNumber == null) {
                this.global.currentCheckpointNumber = number;
            } else {
                this.global.currentCheckpointNumber = 1;
            }
        });
        this.storage.get(CURRENT_CHECKPOINT_MARKER).then(position => {
            if (position != null && this.global.currentCheckpointMarker == null) {
                this.global.currentCheckpointMarker = position;
            }
        });
        this.storage.get(CURRENT_RACE_TIME).then(time => {
            if (time != null && moment().isBetween(moment.unix(time.startTimestamp), moment.unix(time.endTimestamp))) {
                this.timeService.resumeCountDownTimer();
            } else if (time != null && !moment().isBetween(moment.unix(time.startTimestamp), moment.unix(time.endTimestamp))) {
                this.app.getRootNav().setRoot(RaceFailPage);
            }
        });
    }

    setCurrentRaceStatus(status) {
        this.storage.set(CURRENT_RACE_STATUS, status).then((data) => {
            this.global.currentRaceStatus = data;
        });
    }

    setCurrentCheckpointNumber(number) {
        this.storage.set(CURRENT_CHECKPOINT_NUMBER, number).then((data) => {
            this.global.currentCheckpointNumber = data;
        });
    }

    sendCurrentLocation(location) {
        this.currentCheckpointLocation.next({
            latitude: location.latitude,
            longitude: location.longitude,
            name: location.name
        });
    }

    getCurrentCheckpointLocation(): Observable<any> {
        return this.currentCheckpointLocation.asObservable();
    }

    clearCurrentCheckpointLocation() {
        this.storage.remove(CURRENT_CHECKPOINT_MARKER);
        this.global.currentCheckpointMarker = null;
        this.currentCheckpointLocation.next();
    }

    resetRace() {
        this.clearCurrentCheckpointLocation();
        this.setCurrentRaceStatus(0);
        this.storage.remove(CURRENT_RACE_ID);
        this.storage.remove(CURRENT_CHECKPOINT_NUMBER);
        this.storage.remove(CURRENT_RACE_JSON);
        this.storage.remove(CURRENT_RACE_CHECKPOINTS_JSON);
        this.storage.remove(CURRENT_RACE_TIME);
        this.storage.remove(CURRENT_RACE_IMAGE);
    }
}
