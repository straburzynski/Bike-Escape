import { Component, OnInit } from '@angular/core';
import { RaceService } from '../../service/race.service';
import { UtilsService } from '../../service/utils.service';

@Component({
  selector: 'app-my-races-results',
  templateUrl: './my-races-results.component.html',
  styleUrls: ['./my-races-results.component.scss']
})
export class MyRacesResultsComponent implements OnInit {

  userRacesResults: any;

  constructor(private raceService: RaceService,
              private utils: UtilsService) {
  }

  ngOnInit() {
    this.utils.startLoading();
    this.raceService.getAllUserRacesByLoggedInUser().subscribe(
      res => {
        this.utils.stopLoading();
      this.userRacesResults = res;
    }, err => {
        this.utils.stopLoading();
        this.utils.handleError(err);
      })
  }

  secondsToTime(seconds: number) {
    function z(n) {
      return (n < 10 ? '0' : '') + n;
    }

    const s = seconds % 60;
    const m = Math.floor(seconds % 3600 / 60);
    const h = Math.floor(seconds / 3600);
    return (z(h) + ':' + z(m) + ':' + z(s));
  }


}
