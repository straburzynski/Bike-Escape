import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  isUserLoggedIn: boolean;

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
    this.isUserLoggedIn = this.authService.getSession().authenticated;
  }

  logout() {
    this.authService.doLogout();
    this.isUserLoggedIn = false;
  }

}
