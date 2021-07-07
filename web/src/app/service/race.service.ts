import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { SERVER_URL } from './config.service';
import { AuthHttp } from './auth.http';

@Injectable()
export class RaceService {

  constructor(private http: AuthHttp) {
  }

  getAllRaces() {
    return this.http.get(`${SERVER_URL}/race`)
      .map((res: Response) => res.json());
  }

  getAllRacesHeaders() {
    return this.http.get(`${SERVER_URL}/race/header`)
      .map((res: Response) => res.json());
  }

  getRaceById(raceId) {
    return this.http.get(`${SERVER_URL}/race/${raceId}`)
      .map((res: Response) => res.json());
  }

  getCheckpointById(checkpointId) {
    return this.http.get(`${SERVER_URL}/race/checkpoint/${checkpointId}`)
      .map((res: Response) => res.json());
  }

  getAllRacesByAuthorId(authorId) {
    return this.http.get(`${SERVER_URL}/race/author/${authorId}`)
      .map((res: Response) => res.json())
  }

  getAllUserRacesByLoggedInUser() {
    return this.http.get(`${SERVER_URL}/race/history/user`)
      .map((res: Response) => res.json())
  }

  saveRaceById(raceId, raceJson) {
    return this.http.put(`${SERVER_URL}/race/${raceId}`, raceJson)
      .map((res: any) => res)
  }

  saveCheckpointById(checkpointId, checkpointJson) {
    return this.http.put(`${SERVER_URL}/race/checkpoint/${checkpointId}`, checkpointJson)
      .map((res: any) => res)
  }

  deleteCheckpointById(checkpointId) {
    return this.http.delete(`${SERVER_URL}/race/checkpoint/${checkpointId}`)
      .map((res: any) => res)
  }

  deleteRaceById(raceId) {
    return this.http.delete(`${SERVER_URL}/race/${raceId}`)
      .map((res: any) => res)
  }

  createRace(raceJson) {
    return this.http.post(`${SERVER_URL}/race`, raceJson)
      .map((res: any) => res)
  }

  createCheckpoint(raceId, checkpointJson) {
    return this.http.post(`${SERVER_URL}/race/${raceId}/checkpoint`, checkpointJson)
      .map((res: any) => res)
  }

}
