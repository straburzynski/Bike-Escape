import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../../service/statistics.service';
import { UtilsService } from '../../../service/utils.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {

  statistics: any;

  constructor(private statisticsService: StatisticsService,
              private utils: UtilsService) {
  }

  ngOnInit() {
    this.getStatistics();
  }

  getStatistics() {
    this.utils.startLoading();
    this.statisticsService.getStatistics().subscribe(
      res => {
        this.utils.stopLoading();
        this.statistics = res;
      }, err => {
        this.utils.stopLoading();
        this.utils.handleError(err);
      })
  }

}
