import { Observable } from 'rxjs/Rx';
import { ConfirmDialog } from '../component/dialog/confirm-dialog';
import { MdDialogRef, MdDialog } from '@angular/material';
import { Injectable } from '@angular/core';
import { ContentDialog } from '../component/dialog/content-dialog';

@Injectable()
export class DialogsService {

  constructor(private dialog: MdDialog) { }

  public confirm(title: string, message: string): Observable<boolean> {

    let dialogRef: MdDialogRef<ConfirmDialog>;

    dialogRef = this.dialog.open(ConfirmDialog);
    dialogRef.componentInstance.title = title;
    dialogRef.componentInstance.message = message;

    return dialogRef.afterClosed();
  }
  
  public content(title: string, message: string, content: any): Observable<boolean> {

    let dialogRef: MdDialogRef<ContentDialog>;

    dialogRef = this.dialog.open(ContentDialog);
    dialogRef.componentInstance.title = title;
    dialogRef.componentInstance.message = message;
    dialogRef.componentInstance.content = content;
    
    return dialogRef.afterClosed();
  }
  
}
