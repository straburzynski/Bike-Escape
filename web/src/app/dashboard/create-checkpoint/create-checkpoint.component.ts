import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { RaceService } from '../../service/race.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { CropperSettings, ImageCropperComponent } from 'ng2-img-cropper';
import { ConfigService } from '../../service/config.service';
import { MapsAPILoader } from '@agm/core';
import { DialogsService } from '../../service/dialog.service';
import { ImageService } from '../../service/image.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationType, UtilsService } from '../../service/utils.service';

declare let google: any;

@Component({
  selector: 'app-create-checkpoint',
  templateUrl: './create-checkpoint.component.html',
  styleUrls: ['./create-checkpoint.component.scss']
})
export class CreateCheckpointComponent implements OnInit {

  raceId: number;
  route$: Subscription;
  checkpointForm: FormGroup;
  searchControl: FormControl;
  zoom: number;
  latitude: number;
  longitude: number;
  data: any;
  cropperSettings: CropperSettings;
  @ViewChild('search') public searchElementRef: ElementRef;
  @ViewChild('cropper', undefined) cropper: ImageCropperComponent;

  constructor(private route: ActivatedRoute,
              private raceService: RaceService,
              private formBuilder: FormBuilder,
              private location: Location,
              private config: ConfigService,
              private mapsAPILoader: MapsAPILoader,
              private ngZone: NgZone,
              public dialogsService: DialogsService,
              private imageService: ImageService,
              private translate: TranslateService,
              private utils: UtilsService) {
    this.route$ = this.route.params.subscribe(
      (params: Params) => {
        this.raceId = params['id']; // cast to number
      }
    );
    this.cropperSettings = this.config.cropperSettings;
    this.data = {};
    this.latitude = 52.40833753763057;
    this.longitude = 16.93453024232781;
    this.zoom = 4;
  }

  onCropListener() {
    this.checkpointForm.setControl('checkpointImage', new FormControl(this.data.image));
  }

  fileChangeListener($event) {
    const image: any = new Image();
    const file: File = $event.target.files[0];
    const myReader: FileReader = new FileReader();
    const that = this;
    myReader.onloadend = function (loadEvent: any) {
      image.src = loadEvent.target.result;
      that.cropper.setImage(image);
    };
    myReader.readAsDataURL(file);
  }

  ngOnInit() {
    this.fillForm();
    this.searchControl = new FormControl();
    this.setCurrentPosition();
    setTimeout(() => {
      this.initializeMap()
    }, 1000);
  }

  initializeMap() {
    this.mapsAPILoader.load().then(() => {
      const autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement);
      autocomplete.addListener('place_changed', () => {
        this.ngZone.run(() => {
          const place: google.maps.places.PlaceResult = autocomplete.getPlace();
          if (place.geometry === undefined || place.geometry === null) {
            return;
          }
          this.zoom = 12;
          this.latitude = place.geometry.location.lat();
          this.longitude = place.geometry.location.lng();
        });
      });
    });
  }

  mapDragEnd($event) {
    this.latitude = $event.coords.lat;
    this.longitude = $event.coords.lng;
  }

  private setCurrentPosition() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition(
        position => {
          this.latitude = position.coords.latitude;
          this.longitude = position.coords.longitude;
          this.zoom = 12;
        }, () => {
          this.utils.showToast(NotificationType.WARNING, this.translate.instant('Warning'),
            this.translate.instant('Error getting geolocation'));
        });
    }
  }

  createCheckpoint() {
    this.utils.startLoading();
    this.checkpointForm.value.latitude = this.latitude;
    this.checkpointForm.value.longitude = this.longitude;
    return this.raceService.createCheckpoint(this.raceId, this.checkpointForm.value).subscribe(
      res => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.OK, 'OK', res._body);
        this.goBack();
      },
      () => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.ERROR, 'Error', 'Error saving checkpoint');
      })
  }

  fillForm() {
    this.checkpointForm = this.formBuilder.group({
      raceId: [this.raceId],
      name: ['', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(30)
      ]],
      description: ['', [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(1000)
      ]],
      question: ['', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(50)
      ]],
      answerType: ['',
        Validators.required
      ],
      answer: ['', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(30)
      ]],
      hint: ['', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(255)
      ]],
      latitude: [this.latitude, Validators.required],
      longitude: [this.longitude, Validators.required],
      checkpointImage: ['']
    });
  }

  generateQrCode() {
    const content = `<img src="${this.imageService.getQrCodeImage(this.checkpointForm.value.answer)}" />`;
    this.dialogsService.content(this.translate.instant('QR code'), null, content)
  }

  goBack() {
    this.location.back();
  }

}
