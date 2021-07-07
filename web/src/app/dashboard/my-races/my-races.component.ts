import { Component, OnInit } from '@angular/core';
import { RaceService } from '../../service/race.service';
import { AuthService } from '../../service/auth.service';
import { UtilsService } from '../../service/utils.service';

@Component({
  selector: 'app-my-races',
  templateUrl: './my-races.component.html',
  styleUrls: ['./my-races.component.scss']
})
export class MyRacesComponent implements OnInit {

  races: any;

  constructor(private raceService: RaceService,
              private authService: AuthService,
              private utils: UtilsService) {
  }

  ngOnInit() {
    this.loadAllCurrentUserRaces()
  }

  loadAllCurrentUserRaces() {
    this.utils.startLoading();
    this.raceService.getAllRacesByAuthorId(this.authService.currentUser.id).subscribe(
      res => {
        this.utils.stopLoading();
        this.races = res;
      }, err => {
        this.utils.stopLoading();
        this.utils.handleError(err);
      })
  }

}
