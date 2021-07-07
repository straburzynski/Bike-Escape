import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { RaceService } from '../../providers/race-service/race-service';
import { RaceDetailsPage } from '../race-details/race-details';
import { DomSanitizer } from '@angular/platform-browser';
import { HomePage } from '../home/home';
import { BarcodeScanner, BarcodeScannerOptions } from '@ionic-native/barcode-scanner';
import { ImageService } from '../../providers/image-service/image-service';
import { GlobalProvider } from '../../providers/global/global';
import { TranslateService } from '@ngx-translate/core';
import { SmartAudioProvider } from '../../providers/smart-audio/smart-audio';
import { SearchFilters } from '../../models/search-filters';

@Component({
    selector: 'page-race-list',
    templateUrl: 'race-list.html'
})

export class RaceListPage {

    races: any = [];
    isLast = false;
    options: BarcodeScannerOptions;

    searchFilters: SearchFilters = new SearchFilters();

    constructor(public navCtrl: NavController,
                public raceService: RaceService,
                public domSanitizer: DomSanitizer,
                private barcodeScanner: BarcodeScanner,
                private imageService: ImageService,
                public global: GlobalProvider,
                public translate: TranslateService,
                private smartAudio: SmartAudioProvider) {
        this.initSearchFilters();
        this.initialLoad();
    }

    initSearchFilters() {
        this.searchFilters.page = 0;
        this.searchFilters.size = 10;
        this.searchFilters.name = '';
    }

    initialLoad() {
        this.global.startLoading();
        this.raceService.loadByFilters(this.searchFilters).subscribe(
            res => {
                this.races = res.content;
                this.isLast = res.last;
            },
            err => {
                console.error('Error searching races', err);
            }, () => {
                this.global.endLoading();
            });
    }

    loadBySearch() {
        this.searchFilters.page = 0;
        this.raceService.loadByFilters(this.searchFilters).subscribe(
            res => {
                this.races = res.content;
                this.isLast = res.last;
            },
            err => {
                console.error('Error searching races', err);
            }, () => {
            });
    }

    doInfinite(infiniteScroll: any) {
        this.global.startLoading();
        this.searchFilters.page++;
        this.raceService.loadByFilters(this.searchFilters).subscribe(
            res => {
                res.content.forEach(race => this.races.push(race));
                this.isLast = res.last;
                this.handleLastPage();
            },
            () => {
                this.global.showToast('Error searching races');
            },
            () => {
                this.global.endLoading();
                infiniteScroll.complete();
            });
    }

    handleLastPage() {
        if (this.isLast) {
            this.global.showToast('That\'s all races we got');
        }
    }

    sanitizeImage(raceId) {
        return this.domSanitizer.bypassSecurityTrustStyle('url(' + this.imageService.getRaceImageThumbnailPath(raceId) + ')');
    }

    launchRaceDetailsPage(event, race) {
        this.smartAudio.play('click');
        this.navCtrl.push(RaceDetailsPage, {race: race});

    }

    doRefresh(refresher) {
        this.searchFilters.page = 0;
        this.initialLoad();
        refresher.complete();
    }

    launchHomPage() {
        this.smartAudio.play('click');
        this.navCtrl.setRoot(HomePage, {}, {animate: true, direction: 'back'});
    }

    scanQrCode() {
        this.smartAudio.play('click');
        this.options = {
            prompt: this.translate.instant('Scan QR code')
        };
        this.barcodeScanner.scan(this.options).then(data => {
            if (!data.cancelled) {
                this.launchRaceDetailsPage(event, data.text);
            }
        });
    }

}
