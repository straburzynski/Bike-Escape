import { Component, OnDestroy } from '@angular/core';
import { Platform } from 'ionic-angular';
import {
    GoogleMaps,
    GoogleMap,
    GoogleMapsEvent,
    MarkerOptions,
    LatLng
} from '@ionic-native/google-maps';
import { Geolocation } from '@ionic-native/geolocation';
import { Subscription } from 'rxjs/Subscription';
import { RaceService } from '../../providers/race-service/race-service';
import { GlobalProvider } from '../../providers/global/global';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';

@Component({
    selector: 'page-map',
    templateUrl: 'map.html',
})
export class MapPage implements OnDestroy {

    subscription: Subscription;
    map: GoogleMap;

    constructor(public platform: Platform,
                private geolocation: Geolocation,
                public raceService: RaceService,
                public global: GlobalProvider,
                private googleMaps: GoogleMaps,
                private smartAudio: SmartAudioProvider) {
        this.global.startLoading();
        platform.ready().then(() => {
            this.loadMap();
        });
        this.subscription = this.raceService.getCurrentCheckpointLocation().subscribe(location => {
            this.global.currentCheckpointMarker = location;
            this.addCheckpointMarker(location.latitude, location.longitude, location.name);
        });
    }

    loadMap() {
        this.geolocation.getCurrentPosition({
            maximumAge: 5000,
            timeout: 10000,
            enableHighAccuracy: true
        }).then((resp) => {
            const location = new LatLng(resp.coords.latitude, resp.coords.longitude);

            this.map = this.googleMaps.create('map', {
                'controls': {
                    'compass': true,
                    'myLocation': true,
                    'myLocationButton': true,
                    'zoom': true,
                },
                camera: {
                    target: {
                        lat: location.lat,
                        lng: location.lng
                    },
                    tilt: 0,
                    zoom: 18,
                    bearing: 50
                }
            });

            this.map.on(GoogleMapsEvent.MAP_READY).subscribe(() => {
                this.global.endLoading();
                this.addCheckpointMarker(
                    this.global.currentCheckpointMarker.latitude,
                    this.global.currentCheckpointMarker.longitude,
                    this.global.currentCheckpointMarker.name);
            });
        }).catch((error) => {
            this.global.endLoading();
        });
    }

    addCheckpointMarker(lat, lng, title) {
        if (this.map) {
            this.map.clear();

            const markerOptions: MarkerOptions = {
                position: {lat: parseFloat(lat), lng: parseFloat(lng)},
                title: ' ' + title
            };
            this.map.addMarker(markerOptions);
            // .then((marker) => {
            //     marker.showInfoWindow();
            // });
        }
    }

    ngOnDestroy(): void {
        if (this.map) {
            this.map.clear();
        }
        this.subscription.unsubscribe();
    }

    removeCurrentCheckpoint() {
        if (this.map) {
            this.map.clear();
        }
        this.raceService.clearCurrentCheckpointLocation();
    }

    goToCurrentCheckpointMarker() {
        this.smartAudio.play('click');
        if (this.map) {
            const position = new LatLng(this.global.currentCheckpointMarker.latitude, this.global.currentCheckpointMarker.longitude);
            this.map.setCameraTarget(position);
        }
    }

}
