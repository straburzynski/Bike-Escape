import { Injectable } from '@angular/core';
import { ToastNotifications } from 'ngx-toast-notifications';
import { TranslateService } from '@ngx-translate/core';
import { isNullOrUndefined } from 'util';

export enum NotificationType {
  OK = 'success',
  ERROR = 'danger',
  WARNING = 'warning',
  INFO = 'info'
}

@Injectable()
export class UtilsService {

  isLoading = false;

  constructor(public toast: ToastNotifications,
              private translate: TranslateService) {
  }

  showToast(type: NotificationType, title: string, message: string, time?: number): void {
    this.toast.next({
      caption: this.translate.instant(title),
      text: this.translate.instant(message),
      type: type,
      lifetime: time
    });
  }

  isEmpty(value) {
    return value === '' || isNullOrUndefined(value);
  }

  startLoading(): void {
    this.isLoading = true;
  }

  stopLoading(): void {
    this.isLoading = false;
  }

  handleError(error) {
    if (error.name === 'TimeoutError') {
      this.showToast(NotificationType.ERROR, this.translate.instant('Error'), 'Timeout error, check your Internet connection', 5000);
    } else {
      try {
        const message = JSON.parse(error._body).message;
        this.showToast(NotificationType.ERROR, this.translate.instant('Error'), this.translate.instant(message), 5000);
      } catch {
        this.showToast(NotificationType.ERROR, this.translate.instant('Error'), this.translate.instant('An unknown error occurred'), 5000);
      }
    }
  }
}
