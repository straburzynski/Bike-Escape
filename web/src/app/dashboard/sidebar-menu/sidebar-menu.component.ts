import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { VERSION } from '../../service/config.service';

@Component({
  selector: 'app-sidebar-menu',
  templateUrl: './sidebar-menu.component.html',
  styleUrls: ['./sidebar-menu.component.scss']
})
export class SidebarMenuComponent {

  public version;

  constructor(public authService: AuthService) {
    this.version = VERSION;
  }

}
