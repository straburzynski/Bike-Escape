import { Component, OnInit } from '@angular/core';
import { RaceService } from '../../../service/race.service';
import { ImageService } from '../../../service/image.service';
import { Location } from '@angular/common';
import { UtilsService } from '../../../service/utils.service';

@Component({
  selector: 'app-all-races',
  templateUrl: './all-races.component.html',
  styleUrls: ['./all-races.component.scss']
})
export class AllRacesComponent implements OnInit {

  races: any;
  raceImage: string;

  constructor(private raceService: RaceService,
              private imageService: ImageService,
              private location: Location,
              private utils: UtilsService) {
  }

  ngOnInit() {
    this.loadAllRacesHeaders()
  }

  loadAllRacesHeaders() {
    this.utils.startLoading();
    this.raceService.getAllRacesHeaders().subscribe(
      res => {
        this.utils.stopLoading();
        this.races = res;
      }, err => {
        this.utils.stopLoading();
        this.utils.handleError(err);
      })
  }

  loadRaceImage(raceId) {
    return this.imageService.getRaceImageThumbnailPath(raceId);
  }

  goBack() {
    this.location.back();
  }

}
