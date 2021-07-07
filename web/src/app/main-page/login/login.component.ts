import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../service/auth.service';
import { NotificationType, UtilsService } from '../../service/utils.service';
import { TranslateService } from '@ngx-translate/core';
import { DialogsService } from '../../service/dialog.service';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

  loginResponse = '';
  model: any = {};
  loading = false;
  isLoggedIn: boolean;

  constructor(private router: Router,
              private authService: AuthService,
              public utils: UtilsService,
              private translate: TranslateService,
              public auth: AuthService,
              private dialogService: DialogsService) {
  }

  ngOnInit(): void {
    this.isLoggedIn = this.auth.getSession().authenticated;
  }

  login(event: any, email: string, password: string) {
    event.preventDefault();
    if (this.utils.isEmpty(email) || this.utils.isEmpty(password)) {
      this.utils.showToast(NotificationType.WARNING, this.translate.instant('Warning'),
        this.translate.instant('Email and password required'));
      return;
    }
    this.utils.startLoading();

    const success: Function = () => {
      this.utils.stopLoading();
      this.navigateToDashboard();
    };
    const fail: Function = () => {
      this.utils.stopLoading();
      this.utils.showToast(NotificationType.WARNING, this.translate.instant('Warning'),
        this.translate.instant(this.authService.errorMessage));
    };

    this.authService.doLogin(email, password, success, fail);
  }

  showLogoutDialog() {
    this.dialogService
      .confirm(this.translate.instant('Confirm'), this.translate.instant('Are you sure you want to log out?'))
      .subscribe(res => {
        {
          if (res) {
            this.auth.doLogout();
            this.isLoggedIn = false;
          }
        }
      });
  }

  navigateToDashboard() {
    this.router.navigate(['dashboard'])
  }

}
