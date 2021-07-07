import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { SERVER_URL } from './config.service';
import { AuthHttp } from './auth.http';

@Injectable()
export class ImageService {

  constructor(private http: AuthHttp) {
  }

  getRaceImageThumbnailPath(raceId) {
    return SERVER_URL + '/image/raceImageThumbnail?raceId=' + raceId;
  }

  getRaceImagePath(raceId) {
    return SERVER_URL + '/image/raceImage?raceId=' + raceId;
  }

  getSummaryImagePath(raceId) {
    return SERVER_URL + '/image/summaryImage?raceId=' + raceId;
  }

  getFailImagePath(raceId) {
    return SERVER_URL + '/image/failImage?raceId=' + raceId;
  }

  getCheckpointImagePath(raceId, checkpointId) {
    return SERVER_URL + '/image/checkpointImage?raceId=' + raceId + '&checkpointId=' + checkpointId;
  }

  getDefaultImagePath() {
    return SERVER_URL + '/image/defaultImage';
  }

  getQrCodeImage(text) {
    return SERVER_URL + '/image/qrCode?text=' + text;
  }

  download(url) {
    return this.http.get(url).map((res: any) => res);
  }

  saveSummaryImage(checkpointId) {
    return this.http.get(`${SERVER_URL}/race/checkpoint/${checkpointId}`)
      .map((res: any) => res.json());
  }

  deleteRaceImage(checkpointId) {
    return this.http.delete(`${SERVER_URL}/race/checkpoint/${checkpointId}`)
      .map((res: any) => res)
  }

}
