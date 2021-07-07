import { Component } from '@angular/core';
import { UtilsService } from '../../service/utils.service';

@Component({
  selector: 'loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent {

  constructor(public utils: UtilsService) {
  }

}
