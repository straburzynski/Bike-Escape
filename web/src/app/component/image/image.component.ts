import { Component, Input, ViewEncapsulation } from '@angular/core';
import { ImageService } from '../../service/image.service';
import { DialogsService } from '../../service/dialog.service';

@Component({
  selector: 'image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss'],
  encapsulation: ViewEncapsulation.Emulated
})
export class ImageComponent {

  public imageUrl: string;
  private _imageData: any;

  @Input()
  set imageData(value: any) {
    this._imageData = value;
    this.loadImage(this._imageData[0])
  }

  get imageData(): any {
    return this._imageData
  }

  constructor(private imageService: ImageService,
              public dialogsService: DialogsService) {
  }

  loadImage(imageType) {
    switch (imageType) {
      case 'raceImageThumbnail': {
        this.imageUrl = this.imageService.getRaceImageThumbnailPath(this.imageData[1]);
        break;
      }
      case 'raceImage': {
        this.imageUrl = this.imageService.getRaceImagePath(this.imageData[1]);
        break;
      }
      case 'summaryImage': {
        this.imageUrl = this.imageService.getSummaryImagePath(this.imageData[1]);
        break;
      }
      case 'failImage': {
        this.imageUrl = this.imageService.getFailImagePath(this.imageData[1]);
        break;
      }
      case 'checkpointImage': {
        this.imageUrl = this.imageService.getCheckpointImagePath(this.imageData[1], this.imageData[2]);
        break;
      }
      default: {
        this.imageUrl = this.imageService.getDefaultImagePath();
        break;
      }
    }
  }

  openImage(image) {
    const content = `<img src='${image}' class="img-fluid mb-3" />`;
    this.dialogsService.content(null, null, content);
  }
}
