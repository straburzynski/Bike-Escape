import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { SERVER_URL } from './config.service';
import { AuthHttp } from './auth.http';

@Injectable()
export class StatisticsService {

  constructor(private http: AuthHttp) {
  }

  getStatistics() {
    return this.http.get(`${SERVER_URL}/statistics`)
      .map(res => res.json());
  }

}
