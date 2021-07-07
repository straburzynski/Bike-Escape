import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { TranslateService } from '@ngx-translate/core';
import { DialogsService } from '../../service/dialog.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(public auth: AuthService,
              private translate: TranslateService,
              private dialogService: DialogsService) {
  }

  changeLang(lang: string) {
    this.translate.use(lang);
  }

  showLogoutDialog() {
    this.dialogService
      .confirm(this.translate.instant('Confirm'), this.translate.instant('Are you sure you want to log out?'))
      .subscribe(res => {
        {
          if (res) {
            this.auth.doLogout();
          }
        }
      });
  }

  navigateToHome() {
    window.location.href = this.getHostName(window.location.href);
  }

  getHostName(url) {
    const regex = /(\/)?([a-zA-Z]*)(\/#)(.*)/i;
    return url.replace(regex, '');
  }

}
