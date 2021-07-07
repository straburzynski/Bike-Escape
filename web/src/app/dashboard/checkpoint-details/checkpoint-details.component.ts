import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { RaceService } from '../../service/race.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { ConfigService } from '../../service/config.service';
import { CropperSettings, ImageCropperComponent } from 'ng2-img-cropper';
import { MapsAPILoader } from '@agm/core';
import {} from '@types/googlemaps';
import { ImageService } from '../../service/image.service';
import { DialogsService } from '../../service/dialog.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationType, UtilsService } from '../../service/utils.service';
import { utils } from 'protractor';

declare let google: any;

@Component({
  selector: 'app-checkpoint-details',
  templateUrl: './checkpoint-details.component.html',
  styleUrls: ['./checkpoint-details.component.scss']
})
export class CheckpointDetailsComponent implements OnInit {

  checkpointId: number;
  checkpoint: any;
  route$: Subscription;
  checkpointForm: FormGroup;
  searchControl: FormControl;
  zoom: number;
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
        this.checkpointId = params['id']; // cast to number
      }
    );
    this.cropperSettings = this.config.cropperSettings;
    this.data = {};
    this.zoom = 12;
  }

  ngOnInit() {
    this.loadCheckpointById(this.checkpointId);
    this.searchControl = new FormControl();
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
          this.checkpoint.latitude = place.geometry.location.lat();
          this.checkpoint.longitude = place.geometry.location.lng();
        });
      });
    });
  }

  mapDragEnd($event) {
    this.checkpoint.latitude = $event.coords.lat;
    this.checkpoint.longitude = $event.coords.lng;
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

  loadCheckpointById(checkpointId) {
    this.utils.startLoading();
    return this.raceService.getCheckpointById(checkpointId).subscribe(
      res => {
      this.utils.stopLoading();
      this.checkpoint = res;
      this.checkpoint.longitude = parseFloat(res.longitude);
      this.checkpoint.latitude = parseFloat(res.latitude);
      this.fillForm();
    }, err => {
        this.utils.stopLoading();
        this.utils.handleError(err);
      })
  }

  saveCheckpointById() {
    this.utils.startLoading();
    this.checkpointForm.value.latitude = this.checkpoint.latitude;
    this.checkpointForm.value.longitude = this.checkpoint.longitude;
    return this.raceService.saveCheckpointById(this.checkpointId, this.checkpointForm.value).subscribe(
      res => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.OK, 'OK', res._body);
      },
      () => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.ERROR, 'Error', 'Error saving checkpoint');
      });
  }

  fillForm() {
    this.checkpointForm = this.formBuilder.group({
      id: [this.checkpointId],
      raceId: [this.checkpoint.raceId, Validators.required],
      number: [this.checkpoint.number, Validators.required],
      name: [this.checkpoint.name, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(30)
      ]],
      description: [this.checkpoint.description, [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(1000)
      ]],
      question: [this.checkpoint.question, [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(50)
      ]],
      answerType: [this.checkpoint.answerType, [
        Validators.required
      ]],
      answer: [this.checkpoint.answer, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(30)
      ]],
      hint: [this.checkpoint.hint, [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(255)
      ]],
      latitude: [this.checkpoint.latitude, Validators.required],
      longitude: [this.checkpoint.longitude, Validators.required],
      checkpointImage: [this.checkpoint.checkpointImage]
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
