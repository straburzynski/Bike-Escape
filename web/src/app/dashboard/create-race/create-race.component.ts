import { Component, ViewChild } from '@angular/core';
import { RaceService } from '../../service/race.service';
import { AuthService } from '../../service/auth.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ImageCropperComponent, CropperSettings } from 'ng2-img-cropper';
import { ConfigService } from '../../service/config.service';
import { NotificationType, UtilsService } from '../../service/utils.service';

@Component({
  selector: 'app-create-race',
  templateUrl: './create-race.component.html',
  styleUrls: ['./create-race.component.scss']
})
export class CreateRaceComponent {

  raceForm: FormGroup;
  data1: any;
  data2: any;
  data3: any;
  cropperSettings: CropperSettings;

  @ViewChild('cropper1', undefined) cropper1: ImageCropperComponent;
  @ViewChild('cropper2', undefined) cropper2: ImageCropperComponent;
  @ViewChild('cropper3', undefined) cropper3: ImageCropperComponent;

  constructor(private raceService: RaceService,
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private route: ActivatedRoute,
              private config: ConfigService,
              private utils: UtilsService) {
    this.fillForm();
    this.data1 = {};
    this.data2 = {};
    this.data3 = {};
    this.cropperSettings = this.config.cropperSettings;
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

  createRace() {
    this.utils.startLoading();
    return this.raceService.createRace(this.raceForm.value).subscribe(
      res => {
        this.utils.stopLoading();
        this.resetForm();
        this.utils.showToast(NotificationType.OK, 'OK', res._body);
        this.router.navigate(['../my-races'], {relativeTo: this.route});
      },
      (err) => {
        this.utils.stopLoading();
        this.utils.showToast(NotificationType.ERROR, 'Error saving race', err.json().message);
      })
  }

  fillForm() {
    this.raceForm = this.formBuilder.group({
      name: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(30)
      ]],
      description: ['', [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(2000)
      ]],
      summary: ['', [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(2000)
      ]],
      failDescription: ['', [
        Validators.required,
        Validators.minLength(20),
        Validators.maxLength(2000)
      ]],
      estimatedTime: ['', [
        Validators.required,
        Validators.min(10),
        Validators.max(240)
      ]],
      city: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(30)
      ]],
      difficulty: ['', Validators.required],
      raceType: ['', Validators.required],
      checkpoints: [''],
      public: [''],
      active: [''],
      raceImage: [''],
      summaryImage: [''],
      failImage: ['']
    });
  }

  resetForm() {
    this.raceForm.reset();
    this.raceForm.markAsPristine();
    this.data1 = {};
    this.data2 = {};
    this.data3 = {};
  }

}

