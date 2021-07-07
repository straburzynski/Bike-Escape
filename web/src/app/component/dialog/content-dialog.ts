import { MdDialogRef } from '@angular/material';
import { Component } from '@angular/core';

@Component({
  selector: 'content-dialog',
  template: `
    <div class="text-center">
      <h4>{{ title }}</h4>
      <hr/>
      <p class="text-justify">{{ message }}</p>
      <div [innerHTML]="content"></div>
      <button type="button" md-raised-button color="primary" (click)="dialogRef.close()">{{"OK" | translate}}</button>
    </div>
  `,
})
export class ContentDialog {

  public title?: string;
  public message?: string;
  public content?: any;

  constructor(public dialogRef: MdDialogRef<ContentDialog>) {
  }
  
}
