import { Injectable } from '@angular/core';
import { CropperSettings } from 'ng2-img-cropper';

@Injectable()
export class ConfigService {

  get cropperSettings(): CropperSettings {
    return this._cropperSettings;
  }

  private _cropperSettings: CropperSettings;

  constructor() {
    this._cropperSettings = new CropperSettings();
    this._cropperSettings.width = 300;
    this._cropperSettings.height = 200;
    this._cropperSettings.noFileInput = true;

    this._cropperSettings.croppedWidth = 1024;
    this._cropperSettings.croppedHeight = 684;

    this._cropperSettings.canvasWidth = 300;
    this._cropperSettings.canvasHeight = 200;

    this._cropperSettings.minWidth = 512;
    this._cropperSettings.minHeight = 342;

    this._cropperSettings.compressRatio = 0.8;

    this._cropperSettings.cropperDrawSettings.strokeColor = 'rgba(255,255,255,1)';
    this._cropperSettings.cropperDrawSettings.strokeWidth = 2;
  }

}

export const VERSION = '0.1.5';
export const TIMEOUT = 15000;

export const SERVER_URL = 'http://145.239.92.240:8080/api';
// export const SERVER_URL = 'http://localhost:8080/api';
// export const SERVER_URL = 'http://192.168.1.106:8080/api';

