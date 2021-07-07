import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { RaceService } from '../../service/race.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DialogsService } from '../../service/dialog.service';
import { Location } from '@angular/common';
import { CropperSettings, ImageCropperComponent } from 'ng2-img-cropper';
import { ConfigService } from '../../service/config.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationType, UtilsService } from '../../service/utils.service';
import { ImageService } from '../../service/image.service';
import * as _ from 'lodash';

@Component({
  selector: 'app-race-details',
  templateUrl: './race-details.component.html',
  styleUrls: ['./race-details.component.scss']
})
export class RaceDetailsComponent implements OnInit {

  id: number;
  race: any;
  route$: Subscription;
  raceForm: FormGroup;

  data1: any;
  data2: any;
  data3: any;
  cropperSettings: CropperSettings;

  @ViewChild('cropper1', undefined) cropper1: ImageCropperComponent;
  @ViewChild('cropper2', undefined) cropper2: ImageCropperComponent;
  @ViewChild('cropper3', undefined) cropper3: ImageCropperComponent;

  constructor(private route: ActivatedRoute,
              private raceService: RaceService,
              private formBuilder: FormBuilder,
              private dialogsService: DialogsService,
              private location: Location,
              private config: ConfigService,
              private translate: TranslateService,
              private utils: UtilsService,
              private imageService: ImageService,
              private router: Router) {
    this.route$ = this.route.params.subscribe(
      (params: Params) => {
        this.id = params['id']; // cast to number
      }
    );
    this.cropperSettings = this.config.cropperSettings;
    this.data1 = {};
    this.data2 = {};
    this.data3 = {};
  }

  ngOnInit() {
    this.loadRaceById(this.id);
  }

  onCropListener1() {
    this.raceForm.setControl('raceImage', new FormControl(this.data1.image));
  }

  onCropListener2() {
    this.raceForm.setControl('summaryImage', new FormControl(this.data2.image));
  }

  onCropListener3() {
    this.raceForm.setControl('failImage', new FormControl(this.data3.image));
  }

  fileChangeListener1($event) {
    const image: any = new Image();
    const file: File = $event.target.files[0];
    const myReader: FileReader = new FileReader();
    const that = this;
    myReader.onloadend = function (loadEvent: any) {
      image.src = loadEvent.target.result;
      that.cropper1.setImage(image);
    };
    myReader.readAsDataURL(file);
  }

  fileChangeListener2($event) {
    const image: any = new Image();
    const file: File = $event.target.files[0];
    const myReader: FileReader = new FileReader();
    const that = this;
    myReader.onloadend = function (loadEvent: any) {
      image.src = loadEvent.target.result;
      that.cropper2.setImage(image);
    };
    myReader.readAsDataURL(file);
  }

  fileChangeListener3($event) {
    const image: any = new Image();
    const file: File = $event.target.files[0];
    const myReader: FileReader = new FileReader();
    const that = this;
    myReader.onloadend = function (loadEvent: any) {
      image.src = loadEvent.target.result;
      that.cropper3.setImage(image);
    };
    myReader.readAsDataURL(file);
  }

  loadRaceById(raceId) {
    this.resetRaceModel();
    this.utils.startLoading();
    return this.raceService.getRaceById(raceId).subscribe(
      res => {
        this.utils.stopLoading();
        this.race = res;
        this.race.raceCheckpoints = _.sortBy(res.raceCheckpoints, 'number');
        if (this.race.checkpoints === 0) {
          this.race.active = false;
          this.utils.showToast(NotificationType.INFO, this.translate.instant('Warning'),
            this.translate.instant('Race have no checkpoints. Add some on form below.'), 5000)
        }
        this.fillForm();
      }, err => {
        this.utils.stopLoading();
        this.utils.handleError(err);
      })
  }

  saveRaceById() {
    this.utils.startLoading();
    return this.raceService.saveRaceById(this.id, this.raceForm.value).subscribe(
      res => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.OK, 'OK', res._body);
        this.goBack();
      },
      err => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.ERROR, 'Error saving race', err.json().message);
      })
  }

  public openDeleteCheckpointDialog(checkpointId) {
    this.dialogsService
      .confirm(this.translate.instant('Confirm'), this.translate.instant('Are you sure you want to delete this checkpoint?'))
      .subscribe(res => {
        {
          if (res) {
            this.deleteCheckpointById(checkpointId)
          }
        }
      });
  }

  public openDeleteRaceDialog(raceId) {
    this.dialogsService
      .confirm(this.translate.instant('Confirm'), this.translate.instant('Are you sure you want to delete this race with all checkpoints?'))
      .subscribe(res => {
        {
          if (res) {
            this.deleteRaceById(raceId)
          }
        }
      });
  }

  deleteCheckpointById(checkpointId) {
    this.utils.startLoading();
    return this.raceService.deleteCheckpointById(checkpointId).subscribe(
      res => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.OK, 'OK', res._body);
        this.loadRaceById(this.id);
      },
      () => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.ERROR, 'Error', 'Error deleting checkpoint');
      });
  }

  deleteRaceById(raceId) {
    this.utils.startLoading();
    return this.raceService.deleteRaceById(raceId).subscribe(
      res => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.OK, 'OK', res._body);
        this.router.navigate(['/dashboard/my-races']);
      },
      () => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.ERROR, 'Error', 'Error deleting race');
      });
  }

  fillForm() {
    this.raceForm = this.formBuilder.group({
      id: [this.id],
      name: [this.race.name, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(30)
      ]],
      description: [this.race.description, [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(2000)
      ]],
      summary: [this.race.summary, [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(2000)
      ]],
      failDescription: [this.race.failDescription, [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(2000)
      ]],
      estimatedTime: [this.race.estimatedTime, [
        Validators.required,
        Validators.min(10),
        Validators.max(240)]],
      city: [this.race.city, [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(30)
      ]],
      difficulty: [this.race.difficulty.name, Validators.required],
      raceType: [this.race.raceType.name, Validators.required],
      checkpoints: [this.race.checkpoints, Validators.required],
      public: [this.race.public],
      active: new FormControl({value: this.race.active, disabled: this.race.checkpoints === 0}),
      raceImage: [''],
      summaryImage: [''],
      failImage: ['']
    });
  }

  goBack() {
    this.location.back();
  }

  generateQrCode() {
    const content = `<img src="${this.imageService.getQrCodeImage(this.id)}" />`;
    this.dialogsService.content(this.translate.instant('QR code'), null, content)
  }

  private resetRaceModel() {
    this.race = null;
    this.raceForm = null;
  }

}
