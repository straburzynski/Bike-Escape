import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { UtilsService } from '../../service/utils.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  userProfile: any;

  constructor(private authService: AuthService,
              private utils: UtilsService) {
  }

  ngOnInit() {
    // todo: get user data with avatar
    this.userProfile = this.authService.currentUser;
  }

}
