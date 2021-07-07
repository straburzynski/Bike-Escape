import { MdDialogRef } from '@angular/material';
import { Component } from '@angular/core';

@Component({
  selector: 'confirm-dialog',
  template: `
    <div class="text-center">
      <h4>{{ title }}</h4>
      <hr/>
      <p class="text-justify">{{ message }}</p>
      <button type="button" md-raised-button color="primary" (click)="dialogRef.close(true)">{{"OK" | translate}}</button>
      <button type="button" md-raised-button (click)="dialogRef.close(false)">{{"Cancel" | translate}}</button>
    </div>
  `,
})
export class ConfirmDialog {

  public title: string;
  public message: string;

  constructor(public dialogRef: MdDialogRef<ConfirmDialog>) {
  }
  
}
